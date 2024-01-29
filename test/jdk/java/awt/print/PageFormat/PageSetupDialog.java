/*
 * Copyright (c) 2007, 2024, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

import java.awt.Button;
import java.awt.Checkbox;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

import jtreg.SkippedException;

/*
 * @test
 * @bug 4197377
 * @bug 4299145
 * @bug 6358747
 * @bug 6574633
 * @key printer
 * @summary Page setup dialog settings
 * @library /java/awt/regtesthelpers
 * @library /test/lib
 * @build PassFailJFrame
 * @build jtreg.SkippedException
 * @run main/manual PageSetupDialog
 */
public class PageSetupDialog extends Frame implements Printable {

    PrinterJob myPrinterJob;
    PageFormat myPageFormat;
    Label pw, ph, pglm, pgiw, pgrm, pgtm, pgih, pgbm;
    Label myWidthLabel;
    Label myHeightLabel;
    Label myImageableXLabel;
    Label myImageableYLabel;
    Label myImageableRightLabel;
    Label myImageableBottomLabel;
    Label myImageableWidthLabel;
    Label myImageableHeightLabel;
    Label myOrientationLabel;
    Checkbox reverseCB;
    boolean alpha = false;
    boolean reverse = false;

    private static final String instructions =
             " You must have a printer available to perform this test\n" +
             "\n" +
             " This test is very flexible and requires much interaction.\n" +
             " If the platform print dialog supports it, adjust orientation\n" +
             " and margins and print pages and compare the results with the request.";

    protected void displayPageFormatAttributes() {

        myWidthLabel.setText("Format Width = " + (float) myPageFormat.getWidth());
        myHeightLabel.setText("Format Height = " + (float) myPageFormat.getHeight());
        myImageableXLabel.setText
                ("Format Left Margin = " + (float) myPageFormat.getImageableX());
        myImageableRightLabel.setText
                ("Format Right Margin = " + (float) (myPageFormat.getWidth() -
                        (myPageFormat.getImageableX() + myPageFormat.getImageableWidth())));
        myImageableWidthLabel.setText
                ("Format ImageableWidth = " + (float) myPageFormat.getImageableWidth());
        myImageableYLabel.setText
                ("Format Top Margin = " + (float) myPageFormat.getImageableY());
        myImageableBottomLabel.setText
                ("Format Bottom Margin = " + (float) (myPageFormat.getHeight() -
                        (myPageFormat.getImageableY() + myPageFormat.getImageableHeight())));
        myImageableHeightLabel.setText
                ("Format ImageableHeight = " + (float) myPageFormat.getImageableHeight());
        int o = myPageFormat.getOrientation();
        if (o == PageFormat.LANDSCAPE && reverse) {
            o = PageFormat.REVERSE_LANDSCAPE;
            myPageFormat.setOrientation(PageFormat.REVERSE_LANDSCAPE);
        } else if (o == PageFormat.REVERSE_LANDSCAPE && !reverse) {
            o = PageFormat.LANDSCAPE;
            myPageFormat.setOrientation(PageFormat.LANDSCAPE);
        }
        myOrientationLabel.setText
                ("Format Orientation = " +
                        (o == PageFormat.PORTRAIT ? "PORTRAIT" :
                                o == PageFormat.LANDSCAPE ? "LANDSCAPE" :
                                        o == PageFormat.REVERSE_LANDSCAPE ? "REVERSE_LANDSCAPE" :
                                                "<invalid>"));
        Paper p = myPageFormat.getPaper();
        pw.setText("Paper Width = " + (float) p.getWidth());
        ph.setText("Paper Height = " + (float) p.getHeight());
        pglm.setText("Paper Left Margin = " + (float) p.getImageableX());
        pgiw.setText("Paper Imageable Width = " + (float) p.getImageableWidth());
        pgrm.setText("Paper Right Margin = " +
                (float) (p.getWidth() - (p.getImageableX() + p.getImageableWidth())));
        pgtm.setText("Paper Top Margin = " + (float) p.getImageableY());
        pgih.setText("Paper Imageable Height = " + (float) p.getImageableHeight());
        pgbm.setText("Paper Bottom Margin = " +
                (float) (p.getHeight() - (p.getImageableY() + p.getImageableHeight())));
    }

    public PageSetupDialog() {
        super("Page Dialog Test");
        myPrinterJob = PrinterJob.getPrinterJob();
        myPageFormat = new PageFormat();
        Paper p = new Paper();
        double margin = 1.5 * 72;
        p.setImageableArea(margin, margin,
                p.getWidth() - 2 * margin, p.getHeight() - 2 * margin);
        myPageFormat.setPaper(p);
        Panel c = new Panel();
        c.setLayout(new GridLayout(9, 2, 0, 0));
        c.add(reverseCB = new Checkbox("reverse if landscape"));
        c.add(myOrientationLabel = new Label());
        c.add(myWidthLabel = new Label());
        c.add(pw = new Label());
        c.add(myImageableXLabel = new Label());
        c.add(pglm = new Label());
        c.add(myImageableRightLabel = new Label());
        c.add(pgrm = new Label());
        c.add(myImageableWidthLabel = new Label());
        c.add(pgiw = new Label());
        c.add(myHeightLabel = new Label());
        c.add(ph = new Label());
        c.add(myImageableYLabel = new Label());
        c.add(pgtm = new Label());
        c.add(myImageableHeightLabel = new Label());
        c.add(pgih = new Label());
        c.add(myImageableBottomLabel = new Label());
        c.add(pgbm = new Label());

        reverseCB.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                reverse = e.getStateChange() == ItemEvent.SELECTED;
                int o = myPageFormat.getOrientation();
                if (o == PageFormat.LANDSCAPE ||
                        o == PageFormat.REVERSE_LANDSCAPE) {
                    displayPageFormatAttributes();
                }
            }
        });

        add("Center", c);
        displayPageFormatAttributes();
        Panel panel = new Panel();
        Button pageButton = new Button("Page Setup...");
        pageButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                myPageFormat = myPrinterJob.pageDialog(myPageFormat);
                displayPageFormatAttributes();
            }
        });
        Button printButton = new Button("Print ...");
        printButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    if (myPrinterJob.printDialog()) {
                        myPrinterJob.setPrintable(PageSetupDialog.this,
                                myPageFormat);
                        alpha = false;
                        myPrinterJob.print();
                    }
                } catch (PrinterException pe) {
                    pe.printStackTrace();
                }
            }
        });
        Button printAlphaButton = new Button("Print w/Alpha...");
        printAlphaButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    if (myPrinterJob.printDialog()) {
                        myPrinterJob.setPrintable(PageSetupDialog.this,
                                myPageFormat);
                        alpha = true;
                        myPrinterJob.print();
                    }
                } catch (PrinterException pe) {
                    pe.printStackTrace();
                }
            }
        });
        panel.add(pageButton);
        panel.add(printButton);
        panel.add(printAlphaButton);
        add("South", panel);
        pack();
    }

    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) {

        if (pageIndex > 0) {
            return Printable.NO_SUCH_PAGE;
        }

        Graphics2D g2d = (Graphics2D) graphics;
        g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
        g2d.drawString("ORIGIN(" + pageFormat.getImageableX() + "," +
                pageFormat.getImageableY() + ")", 20, 20);
        g2d.drawString("X THIS WAY", 200, 50);
        g2d.drawString("Y THIS WAY", 60, 200);
        g2d.drawString("Graphics is " + g2d.getClass().getName(), 100, 100);
        g2d.drawRect(0, 0, (int) pageFormat.getImageableWidth(),
                (int) pageFormat.getImageableHeight());
        if (alpha) {
            g2d.setColor(new Color(0, 0, 255, 192));
        } else {
            g2d.setColor(Color.blue);
        }
        g2d.drawRect(1, 1, (int) pageFormat.getImageableWidth() - 2,
                (int) pageFormat.getImageableHeight() - 2);

        return Printable.PAGE_EXISTS;
    }

    public static void main(String[] args) throws Exception {

        if (PrinterJob.lookupPrintServices().length == 0) {
            throw new SkippedException("Printer not configured or available."
                    + " Test cannot continue.");
        }

        PassFailJFrame.builder()
                .title("PageSetupDialog Test Instructions")
                .instructions(instructions)
                .testUI(PageSetupDialog::new)
                .testTimeOut(5)
                .rows((int) instructions.lines().count() + 1)
                .columns(45)
                .build()
                .awaitAndCheck();
    }
}
