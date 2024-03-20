/*
 * Copyright (c) 2013, 2024, Oracle and/or its affiliates. All rights reserved.
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


import java.awt.GridLayout;
import java.awt.TextArea;
import java.awt.TextField;
import javax.swing.JPanel;

/*
 * @test
 * @bug 8000423 7197619 8025649
 * @summary Check if diacritical signs could be typed for TextArea and TextField
 * @library /java/awt/regtesthelpers
 * @build PassFailJFrame
 * @run main/manual DiacriticsTest
*/

public class DiacriticsTest {

    //TODO instructions per platform
    private static final String INSTRUCTIONS = """
    Test run requires the following keyboard layouts to be installed:
    Linux OS: English (US, alternative international)
    Windows OS: Hungarian
    A keyboard layout having compose function or compose-like key. Programmer
    Dvorak (http://www.kaufmann.no/roland/dvorak/) is suggested to use.

    To test JDK-8000423 fix (Linux only!):
    please switch to US alternative international layout and try to type diacritics
    (using the following combinations: `+e; `+u; etc.)

    To test JDK-7197619 fix (Windows only!):
    please switch to Hungarian keyboard layout and try to type diacritics
    (Ctrl+Alt+2 e; Ctrl+Alt+2 E)

    To test JDK-8139189 fix:
    please switch to Programmer Dvorak keyboard layout try to type diacritics
    using compose combinations (Compose+z+d, Compose+z+Shift+d). The Compose key
    in Programmer Dvorak layout is OEM102 the key which is located between
    Left Shift and Z keys on the standard 102-key keyboard.
    
    If you can do that then the test is passed; otherwise failed.
    """;

    public static void main(String[] args) throws Exception {
        PassFailJFrame
                .builder()
                .title("DiacriticsTest Instructions")
                .instructions(INSTRUCTIONS)
                .splitUIBottom(DiacriticsTest::createPanel)
                .rows(15)
                .columns(40)
                .build()
                .awaitAndCheck();
    }

    public static JPanel createPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 1));

        TextArea txtArea = new TextArea();
        panel.add(txtArea);

        TextField txtField = new TextField();
        panel.add(txtField);

        return panel;
    }
}

