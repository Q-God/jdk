/*
 * Copyright (c) 2023, Oracle and/or its affiliates. All rights reserved.
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

/*
 * @test
 * @bug 0000000
 * @summary Exercise format builder.
 * @enablePreview true
 */

import java.util.FormatProcessor;
import java.util.Objects;
import java.util.Locale;
import java.util.MissingFormatArgumentException;
import java.util.UnknownFormatConversionException;

import static java.util.FormatProcessor.FMT;

public class FormatterBuilder {
    public static void main(String... args) {
        Locale.setDefault(Locale.US);
        suite(FMT);
        Locale thai = Locale.forLanguageTag("th-TH-u-nu-thai");
        FormatProcessor thaiFormat = FormatProcessor.create(thai);
        Locale.setDefault(thai);
        suite(thaiFormat);
    }

    static void test(String a, String b) {
        if (!Objects.equals(a, b)) {
            throw new RuntimeException("format and FMT do not match: " + a + " : " + b);
        }
    }

    public interface Executable {
        void execute() throws Throwable;
    }

    static <T extends Throwable> void assertThrows(Class<T> expectedType, Executable executable, String message) {
        Throwable actualException = null;
        try {
            executable.execute();
        } catch (Throwable e) {
            actualException = e;
        }
        if (actualException == null) {
            throw new RuntimeException("Expected " + expectedType + " to be thrown, but nothing was thrown.");
        }
        if (!expectedType.isInstance(actualException)) {
            throw new RuntimeException("Expected " + expectedType + " to be thrown, but was thrown " + actualException.getClass());
        }
        if (message != null && !message.equals(actualException.getMessage())) {
            throw new RuntimeException("Expected " + message + " to be thrown, but was thrown " + actualException.getMessage());
        }
    }

    static void suite(FormatProcessor fmt) {
        Object nullObject = null;
        test(String.format("%b", false), fmt."%b\{false}");
        test(String.format("%b", true), fmt."%b\{true}");
        test(String.format("%10b", false), fmt."%10b\{false}");
        test(String.format("%10b", true), fmt."%10b\{true}");
        test(String.format("%-10b", false), fmt."%-10b\{false}");
        test(String.format("%-10b", true), fmt."%-10b\{true}");
        test(String.format("%B", false), fmt."%B\{false}");
        test(String.format("%B", true), fmt."%B\{true}");
        test(String.format("%10B", false), fmt."%10B\{false}");
        test(String.format("%10B", true), fmt."%10B\{true}");
        test(String.format("%-10B", false), fmt."%-10B\{false}");
        test(String.format("%-10B", true), fmt."%-10B\{true}");

        // utf16
        test(String.format("\u8336%b", false), fmt."\u8336%b\{false}");
        test(String.format("\u8336%b", true), fmt."\u8336%b\{true}");
        test(String.format("\u8336%10b", false), fmt."\u8336%10b\{false}");
        test(String.format("\u8336%10b", true), fmt."\u8336%10b\{true}");
        test(String.format("\u8336%-10b", false), fmt."\u8336%-10b\{false}");
        test(String.format("\u8336%-10b", true), fmt."\u8336%-10b\{true}");
        test(String.format("\u8336%B", false), fmt."\u8336%B\{false}");
        test(String.format("\u8336%B", true), fmt."\u8336%B\{true}");
        test(String.format("\u8336%10B", false), fmt."\u8336%10B\{false}");
        test(String.format("\u8336%10B", true), fmt."\u8336%10B\{true}");
        test(String.format("\u8336%-10B", false), fmt."\u8336%-10B\{false}");
        test(String.format("\u8336%-10B", true), fmt."\u8336%-10B\{true}");

        test(String.format("%h", 12345), fmt."%h\{12345}");
        test(String.format("%h", 0xABCDE), fmt."%h\{0xABCDE}");
        test(String.format("%10h", 12345), fmt."%10h\{12345}");
        test(String.format("%10h", 0xABCDE), fmt."%10h\{0xABCDE}");
        test(String.format("%-10h", 12345), fmt."%-10h\{12345}");
        test(String.format("%-10h", 0xABCDE), fmt."%-10h\{0xABCDE}");
        test(String.format("%H", 12345), fmt."%H\{12345}");
        test(String.format("%H", 0xABCDE), fmt."%H\{0xABCDE}");
        test(String.format("%10H", 12345), fmt."%10H\{12345}");
        test(String.format("%10H", 0xABCDE), fmt."%10H\{0xABCDE}");
        test(String.format("%-10H", 12345), fmt."%-10H\{12345}");
        test(String.format("%-10H", 0xABCDE), fmt."%-10H\{0xABCDE}");

        test(String.format("%s", (byte)0xFF), fmt."%s\{(byte)0xFF}");
        test(String.format("%s", (short)0xFFFF), fmt."%s\{(short)0xFFFF}");
        test(String.format("%s", 12345), fmt."%s\{12345}");
        test(String.format("%s", 12345L), fmt."%s\{12345L}");
        test(String.format("%s", 1.33f), fmt."%s\{1.33f}");
        test(String.format("%s", 1.33), fmt."%s\{1.33}");
        test(String.format("%s", "abcde"), fmt."%s\{"abcde"}");
        test(String.format("%s", nullObject), fmt."%s\{nullObject}");
        test(String.format("\u8336%s", nullObject), fmt."\u8336%s\{nullObject}"); // utf16
        test(String.format("%10s", (byte)0xFF), fmt."%10s\{(byte)0xFF}");
        test(String.format("%10s", (short)0xFFFF), fmt."%10s\{(short)0xFFFF}");
        test(String.format("%10s", 12345), fmt."%10s\{12345}");
        test(String.format("%10s", 12345L), fmt."%10s\{12345L}");
        test(String.format("%10s", 1.33f), fmt."%10s\{1.33f}");
        test(String.format("%10s", 1.33), fmt."%10s\{1.33}");
        test(String.format("%10s", "abcde"), fmt."%10s\{"abcde"}");
        test(String.format("%10s", nullObject), fmt."%10s\{nullObject}");
        test(String.format("%-10s", (byte)0xFF), fmt."%-10s\{(byte)0xFF}");
        test(String.format("%-10s", (short)0xFFFF), fmt."%-10s\{(short)0xFFFF}");
        test(String.format("%-10s", 12345), fmt."%-10s\{12345}");
        test(String.format("%-10s", 12345L), fmt."%-10s\{12345L}");
        test(String.format("%-10s", 1.33f), fmt."%-10s\{1.33f}");
        test(String.format("%-10s", 1.33), fmt."%-10s\{1.33}");
        test(String.format("%-10s", "abcde"), fmt."%-10s\{"abcde"}");
        test(String.format("%-10s", nullObject), fmt."%-10s\{nullObject}");
        test(String.format("%S", (byte)0xFF), fmt."%S\{(byte)0xFF}");
        test(String.format("%S", (short)0xFFFF), fmt."%S\{(short)0xFFFF}");
        test(String.format("%S", 12345), fmt."%S\{12345}");
        test(String.format("%S", 12345L), fmt."%S\{12345L}");
        test(String.format("%S", 1.33f), fmt."%S\{1.33f}");
        test(String.format("%S", 1.33), fmt."%S\{1.33}");
        test(String.format("%S", "abcde"), fmt."%S\{"abcde"}");
        test(String.format("%S", nullObject), fmt."%S\{nullObject}");
        test(String.format("%10S", (byte)0xFF), fmt."%10S\{(byte)0xFF}");
        test(String.format("%10S", (short)0xFFFF), fmt."%10S\{(short)0xFFFF}");
        test(String.format("%10S", 12345), fmt."%10S\{12345}");
        test(String.format("%10S", 12345L), fmt."%10S\{12345L}");
        test(String.format("%10S", 1.33f), fmt."%10S\{1.33f}");
        test(String.format("%10S", 1.33), fmt."%10S\{1.33}");
        test(String.format("%10S", "abcde"), fmt."%10S\{"abcde"}");
        test(String.format("%10S", nullObject), fmt."%10S\{nullObject}");
        test(String.format("%-10S", (byte)0xFF), fmt."%-10S\{(byte)0xFF}");
        test(String.format("%-10S", (short)0xFFFF), fmt."%-10S\{(short)0xFFFF}");
        test(String.format("%-10S", 12345), fmt."%-10S\{12345}");
        test(String.format("%-10S", 12345L), fmt."%-10S\{12345L}");
        test(String.format("%-10S", 1.33f), fmt."%-10S\{1.33f}");
        test(String.format("%-10S", 1.33), fmt."%-10S\{1.33}");
        test(String.format("%-10S", "abcde"), fmt."%-10S\{"abcde"}");
        test(String.format("%-10S", nullObject), fmt."%-10S\{nullObject}");

        test(String.format("%c", 'a'), fmt."%c\{'a'}");
        test(String.format("\u8336%c", 'a'), fmt."\u8336%c\{'a'}"); // utf16
        test(String.format("%10c", 'a'), fmt."%10c\{'a'}");
        test(String.format("%-10c", 'a'), fmt."%-10c\{'a'}");
        test(String.format("%C", 'a'), fmt."%C\{'a'}");
        test(String.format("%10C", 'a'), fmt."%10C\{'a'}");
        test(String.format("%-10C", 'a'), fmt."%-10C\{'a'}");

        test(String.format("%d", -12345), fmt."%d\{-12345}");
        test(String.format("%d", 0), fmt."%d\{0}");
        test(String.format("%d", 12345), fmt."%d\{12345}");
        test(String.format("%10d", -12345), fmt."%10d\{-12345}");
        test(String.format("%10d", 0), fmt."%10d\{0}");
        test(String.format("%10d", 12345), fmt."%10d\{12345}");
        test(String.format("%-10d", -12345), fmt."%-10d\{-12345}");
        test(String.format("%-10d", 0), fmt."%-10d\{0}");
        test(String.format("%-10d", 12345), fmt."%-10d\{12345}");
        test(String.format("%,d", -12345), fmt."%,d\{-12345}");
        test(String.format("%,d", 0), fmt."%,d\{0}");
        test(String.format("%,d", 12345), fmt."%,d\{12345}");
        test(String.format("%,10d", -12345), fmt."%,10d\{-12345}");
        test(String.format("%,10d", 0), fmt."%,10d\{0}");
        test(String.format("%,10d", 12345), fmt."%,10d\{12345}");
        test(String.format("%,-10d", -12345), fmt."%,-10d\{-12345}");
        test(String.format("%,-10d", 0), fmt."%,-10d\{0}");
        test(String.format("%,-10d", 12345), fmt."%,-10d\{12345}");
        test(String.format("%010d", -12345), fmt."%010d\{-12345}");
        test(String.format("%010d", 0), fmt."%010d\{0}");
        test(String.format("%010d", 12345), fmt."%010d\{12345}");
        test(String.format("%,010d", -12345), fmt."%,010d\{-12345}");
        test(String.format("%,010d", 0), fmt."%,010d\{0}");
        test(String.format("%,010d", 12345), fmt."%,010d\{12345}");

        test(String.format("%d", -12345), fmt."%d\{-12345}");
        test(String.format("%d", 0), fmt."%d\{0}");
        test(String.format("%d", 12345), fmt."%d\{12345}");
        test(String.format("%10d", -12345), fmt."%10d\{-12345}");
        test(String.format("\u8336%10d", -12345), fmt."\u8336%10d\{-12345}"); // utf16
        test(String.format("%10d", 0), fmt."%10d\{0}");
        test(String.format("%10d", 12345), fmt."%10d\{12345}");
        test(String.format("%-10d", -12345), fmt."%-10d\{-12345}");
        test(String.format("%-10d", 0), fmt."%-10d\{0}");
        test(String.format("%-10d", 12345), fmt."%-10d\{12345}");
        test(String.format("%,d", -12345), fmt."%,d\{-12345}");
        test(String.format("%,d", 0), fmt."%,d\{0}");
        test(String.format("%,d", 12345), fmt."%,d\{12345}");
        test(String.format("%,10d", -12345), fmt."%,10d\{-12345}");
        test(String.format("%,10d", 0), fmt."%,10d\{0}");
        test(String.format("%,10d", 12345), fmt."%,10d\{12345}");
        test(String.format("%,-10d", -12345), fmt."%,-10d\{-12345}");
        test(String.format("%,-10d", 0), fmt."%,-10d\{0}");
        test(String.format("%,-10d", 12345), fmt."%,-10d\{12345}");
        test(String.format("% d", -12345), fmt."% d\{-12345}");
        test(String.format("% d", 0), fmt."% d\{0}");
        test(String.format("% d", 12345), fmt."% d\{12345}");
        test(String.format("% 10d", -12345), fmt."% 10d\{-12345}");
        test(String.format("% 10d", 0), fmt."% 10d\{0}");
        test(String.format("% 10d", 12345), fmt."% 10d\{12345}");
        test(String.format("% -10d", -12345), fmt."% -10d\{-12345}");
        test(String.format("% -10d", 0), fmt."% -10d\{0}");
        test(String.format("% -10d", 12345), fmt."% -10d\{12345}");
        test(String.format("%, d", -12345), fmt."%, d\{-12345}");
        test(String.format("%, d", 0), fmt."%, d\{0}");
        test(String.format("%, d", 12345), fmt."%, d\{12345}");
        test(String.format("%, 10d", -12345), fmt."%, 10d\{-12345}");
        test(String.format("%, 10d", 0), fmt."%, 10d\{0}");
        test(String.format("%, 10d", 12345), fmt."%, 10d\{12345}");
        test(String.format("%, -10d", -12345), fmt."%, -10d\{-12345}");
        test(String.format("%, -10d", 0), fmt."%, -10d\{0}");
        test(String.format("%, -10d", 12345), fmt."%, -10d\{12345}");
        test(String.format("%010d", -12345), fmt."%010d\{-12345}");
        test(String.format("%010d", 0), fmt."%010d\{0}");
        test(String.format("%010d", 12345), fmt."%010d\{12345}");
        test(String.format("%,010d", -12345), fmt."%,010d\{-12345}");
        test(String.format("%,010d", 0), fmt."%,010d\{0}");
        test(String.format("%,010d", 12345), fmt."%,010d\{12345}");
        test(String.format("% 010d", -12345), fmt."% 010d\{-12345}");
        test(String.format("% 010d", 0), fmt."% 010d\{0}");
        test(String.format("% 010d", 12345), fmt."% 010d\{12345}");
        test(String.format("%, 010d", -12345), fmt."%, 010d\{-12345}");
        test(String.format("%, 010d", 0), fmt."%, 010d\{0}");
        test(String.format("%, 010d", 12345), fmt."%, 010d\{12345}");

        test(String.format("%d", -12345), fmt."%d\{-12345}");
        test(String.format("%d", 0), fmt."%d\{0}");
        test(String.format("%d", 12345), fmt."%d\{12345}");
        test(String.format("%10d", -12345), fmt."%10d\{-12345}");
        test(String.format("%10d", 0), fmt."%10d\{0}");
        test(String.format("%10d", 12345), fmt."%10d\{12345}");
        test(String.format("%-10d", -12345), fmt."%-10d\{-12345}");
        test(String.format("%-10d", 0), fmt."%-10d\{0}");
        test(String.format("%-10d", 12345), fmt."%-10d\{12345}");
        test(String.format("%,d", -12345), fmt."%,d\{-12345}");
        test(String.format("%,d", 0), fmt."%,d\{0}");
        test(String.format("%,d", 12345), fmt."%,d\{12345}");
        test(String.format("%,10d", -12345), fmt."%,10d\{-12345}");
        test(String.format("%,10d", 0), fmt."%,10d\{0}");
        test(String.format("%,10d", 12345), fmt."%,10d\{12345}");
        test(String.format("%,-10d", -12345), fmt."%,-10d\{-12345}");
        test(String.format("%,-10d", 0), fmt."%,-10d\{0}");
        test(String.format("%,-10d", 12345), fmt."%,-10d\{12345}");
        test(String.format("%+d", -12345), fmt."%+d\{-12345}");
        test(String.format("%+d", 0), fmt."%+d\{0}");
        test(String.format("%+d", 12345), fmt."%+d\{12345}");
        test(String.format("%+10d", -12345), fmt."%+10d\{-12345}");
        test(String.format("%+10d", 0), fmt."%+10d\{0}");
        test(String.format("%+10d", 12345), fmt."%+10d\{12345}");
        test(String.format("%+-10d", -12345), fmt."%+-10d\{-12345}");
        test(String.format("%+-10d", 0), fmt."%+-10d\{0}");
        test(String.format("%+-10d", 12345), fmt."%+-10d\{12345}");
        test(String.format("%,+d", -12345), fmt."%,+d\{-12345}");
        test(String.format("%,+d", 0), fmt."%,+d\{0}");
        test(String.format("%,+d", 12345), fmt."%,+d\{12345}");
        test(String.format("%,+10d", -12345), fmt."%,+10d\{-12345}");
        test(String.format("%,+10d", 0), fmt."%,+10d\{0}");
        test(String.format("%,+10d", 12345), fmt."%,+10d\{12345}");
        test(String.format("%,+-10d", -12345), fmt."%,+-10d\{-12345}");
        test(String.format("%,+-10d", 0), fmt."%,+-10d\{0}");
        test(String.format("%,+-10d", 12345), fmt."%,+-10d\{12345}");
        test(String.format("%010d", -12345), fmt."%010d\{-12345}");
        test(String.format("%010d", 0), fmt."%010d\{0}");
        test(String.format("%010d", 12345), fmt."%010d\{12345}");
        test(String.format("%,010d", -12345), fmt."%,010d\{-12345}");
        test(String.format("%,010d", 0), fmt."%,010d\{0}");
        test(String.format("%,010d", 12345), fmt."%,010d\{12345}");
        test(String.format("%+010d", -12345), fmt."%+010d\{-12345}");
        test(String.format("%+010d", 0), fmt."%+010d\{0}");
        test(String.format("%+010d", 12345), fmt."%+010d\{12345}");
        test(String.format("%,+010d", -12345), fmt."%,+010d\{-12345}");
        test(String.format("%,+010d", 0), fmt."%,+010d\{0}");
        test(String.format("%,+010d", 12345), fmt."%,+010d\{12345}");

        test(String.format("%d", -12345), fmt."%d\{-12345}");
        test(String.format("%d", 0), fmt."%d\{0}");
        test(String.format("%d", 12345), fmt."%d\{12345}");
        test(String.format("%10d", -12345), fmt."%10d\{-12345}");
        test(String.format("%10d", 0), fmt."%10d\{0}");
        test(String.format("%10d", 12345), fmt."%10d\{12345}");
        test(String.format("%-10d", -12345), fmt."%-10d\{-12345}");
        test(String.format("%-10d", 0), fmt."%-10d\{0}");
        test(String.format("%-10d", 12345), fmt."%-10d\{12345}");
        test(String.format("%,d", -12345), fmt."%,d\{-12345}");
        test(String.format("%,d", 0), fmt."%,d\{0}");
        test(String.format("%,d", 12345), fmt."%,d\{12345}");
        test(String.format("%,10d", -12345), fmt."%,10d\{-12345}");
        test(String.format("%,10d", 0), fmt."%,10d\{0}");
        test(String.format("%,10d", 12345), fmt."%,10d\{12345}");
        test(String.format("%,-10d", -12345), fmt."%,-10d\{-12345}");
        test(String.format("%,-10d", 0), fmt."%,-10d\{0}");
        test(String.format("%,-10d", 12345), fmt."%,-10d\{12345}");
        test(String.format("%(d", -12345), fmt."%(d\{-12345}");
        test(String.format("%(d", 0), fmt."%(d\{0}");
        test(String.format("%(d", 12345), fmt."%(d\{12345}");
        test(String.format("%(10d", -12345), fmt."%(10d\{-12345}");
        test(String.format("%(10d", 0), fmt."%(10d\{0}");
        test(String.format("%(10d", 12345), fmt."%(10d\{12345}");
        test(String.format("%(-10d", -12345), fmt."%(-10d\{-12345}");
        test(String.format("%(-10d", 0), fmt."%(-10d\{0}");
        test(String.format("%(-10d", 12345), fmt."%(-10d\{12345}");
        test(String.format("%,(d", -12345), fmt."%,(d\{-12345}");
        test(String.format("%,(d", 0), fmt."%,(d\{0}");
        test(String.format("%,(d", 12345), fmt."%,(d\{12345}");
        test(String.format("%,(10d", -12345), fmt."%,(10d\{-12345}");
        test(String.format("%,(10d", 0), fmt."%,(10d\{0}");
        test(String.format("%,(10d", 12345), fmt."%,(10d\{12345}");
        test(String.format("%,(-10d", -12345), fmt."%,(-10d\{-12345}");
        test(String.format("%,(-10d", 0), fmt."%,(-10d\{0}");
        test(String.format("%,(-10d", 12345), fmt."%,(-10d\{12345}");
        test(String.format("%010d", -12345), fmt."%010d\{-12345}");
        test(String.format("%010d", 0), fmt."%010d\{0}");
        test(String.format("%010d", 12345), fmt."%010d\{12345}");
        test(String.format("%,010d", -12345), fmt."%,010d\{-12345}");
        test(String.format("%,010d", 0), fmt."%,010d\{0}");
        test(String.format("%,010d", 12345), fmt."%,010d\{12345}");
        test(String.format("%(010d", -12345), fmt."%(010d\{-12345}");
        test(String.format("%(010d", 0), fmt."%(010d\{0}");
        test(String.format("%(010d", 12345), fmt."%(010d\{12345}");
        test(String.format("%,(010d", -12345), fmt."%,(010d\{-12345}");
        test(String.format("%,(010d", 0), fmt."%,(010d\{0}");
        test(String.format("%,(010d", 12345), fmt."%,(010d\{12345}");

        test(String.format("%o", -12345), fmt."%o\{-12345}");
        test(String.format("\u8336%o", -12345), fmt."\u8336%o\{-12345}"); // utf16
        test(String.format("%o", 0), fmt."%o\{0}");
        test(String.format("%o", 12345), fmt."%o\{12345}");
        test(String.format("%10o", -12345), fmt."%10o\{-12345}");
        test(String.format("%10o", 0), fmt."%10o\{0}");
        test(String.format("%10o", 12345), fmt."%10o\{12345}");
        test(String.format("%-10o", -12345), fmt."%-10o\{-12345}");
        test(String.format("%-10o", 0), fmt."%-10o\{0}");
        test(String.format("%-10o", 12345), fmt."%-10o\{12345}");
        test(String.format("%#o", -12345), fmt."%#o\{-12345}");
        test(String.format("%#o", 0), fmt."%#o\{0}");
        test(String.format("%#o", 12345), fmt."%#o\{12345}");
        test(String.format("%#10o", -12345), fmt."%#10o\{-12345}");
        test(String.format("%#10o", 0), fmt."%#10o\{0}");
        test(String.format("%#10o", 12345), fmt."%#10o\{12345}");
        test(String.format("%#-10o", -12345), fmt."%#-10o\{-12345}");
        test(String.format("%#-10o", 0), fmt."%#-10o\{0}");
        test(String.format("%#-10o", 12345), fmt."%#-10o\{12345}");
        test(String.format("%010o", -12345), fmt."%010o\{-12345}");
        test(String.format("%010o", 0), fmt."%010o\{0}");
        test(String.format("%010o", 12345), fmt."%010o\{12345}");
        test(String.format("%#010o", -12345), fmt."%#010o\{-12345}");
        test(String.format("%#010o", 0), fmt."%#010o\{0}");
        test(String.format("%#010o", 12345), fmt."%#010o\{12345}");

        test(String.format("%x", -12345), fmt."%x\{-12345}");
        test(String.format("\u8336%x", -12345), fmt."\u8336%x\{-12345}");
        test(String.format("%x", 0), fmt."%x\{0}");
        test(String.format("%x", 12345), fmt."%x\{12345}");
        test(String.format("%10x", -12345), fmt."%10x\{-12345}");
        test(String.format("%10x", 0), fmt."%10x\{0}");
        test(String.format("%10x", 12345), fmt."%10x\{12345}");
        test(String.format("%-10x", -12345), fmt."%-10x\{-12345}");
        test(String.format("%-10x", 0), fmt."%-10x\{0}");
        test(String.format("%-10x", 12345), fmt."%-10x\{12345}");
        test(String.format("%X", -12345), fmt."%X\{-12345}");
        test(String.format("%X", 0), fmt."%X\{0}");
        test(String.format("%X", 12345), fmt."%X\{12345}");
        test(String.format("%10X", -12345), fmt."%10X\{-12345}");
        test(String.format("%10X", 0), fmt."%10X\{0}");
        test(String.format("%10X", 12345), fmt."%10X\{12345}");
        test(String.format("%-10X", -12345), fmt."%-10X\{-12345}");
        test(String.format("%-10X", 0), fmt."%-10X\{0}");
        test(String.format("%-10X", 12345), fmt."%-10X\{12345}");
        test(String.format("%#x", -12345), fmt."%#x\{-12345}");
        test(String.format("%#x", 0), fmt."%#x\{0}");
        test(String.format("%#x", 12345), fmt."%#x\{12345}");
        test(String.format("%#10x", -12345), fmt."%#10x\{-12345}");
        test(String.format("%#10x", 0), fmt."%#10x\{0}");
        test(String.format("%#10x", 12345), fmt."%#10x\{12345}");
        test(String.format("%#-10x", -12345), fmt."%#-10x\{-12345}");
        test(String.format("%#-10x", 0), fmt."%#-10x\{0}");
        test(String.format("%#-10x", 12345), fmt."%#-10x\{12345}");
        test(String.format("%#X", -12345), fmt."%#X\{-12345}");
        test(String.format("%#X", 0), fmt."%#X\{0}");
        test(String.format("%#X", 12345), fmt."%#X\{12345}");
        test(String.format("%#10X", -12345), fmt."%#10X\{-12345}");
        test(String.format("%#10X", 0), fmt."%#10X\{0}");
        test(String.format("%#10X", 12345), fmt."%#10X\{12345}");
        test(String.format("%#-10X", -12345), fmt."%#-10X\{-12345}");
        test(String.format("%#-10X", 0), fmt."%#-10X\{0}");
        test(String.format("%#-10X", 12345), fmt."%#-10X\{12345}");
        test(String.format("%010x", -12345), fmt."%010x\{-12345}");
        test(String.format("%010x", 0), fmt."%010x\{0}");
        test(String.format("%010x", 12345), fmt."%010x\{12345}");
        test(String.format("%010X", -12345), fmt."%010X\{-12345}");
        test(String.format("%010X", 0), fmt."%010X\{0}");
        test(String.format("%010X", 12345), fmt."%010X\{12345}");
        test(String.format("%#010x", -12345), fmt."%#010x\{-12345}");
        test(String.format("%#010x", 0), fmt."%#010x\{0}");
        test(String.format("%#010x", 12345), fmt."%#010x\{12345}");
        test(String.format("%#010X", -12345), fmt."%#010X\{-12345}");
        test(String.format("%#010X", 0), fmt."%#010X\{0}");
        test(String.format("%#010X", 12345), fmt."%#010X\{12345}");

        test(String.format("%f", -12345.6), fmt."%f\{-12345.6}");
        test(String.format("%f", 0.0), fmt."%f\{0.0}");
        test(String.format("%f", 12345.6), fmt."%f\{12345.6}");
        test(String.format("%10f", -12345.6), fmt."%10f\{-12345.6}");
        test(String.format("%10f", 0.0), fmt."%10f\{0.0}");
        test(String.format("%10f", 12345.6), fmt."%10f\{12345.6}");
        test(String.format("%-10f", -12345.6), fmt."%-10f\{-12345.6}");
        test(String.format("%-10f", 0.0), fmt."%-10f\{0.0}");
        test(String.format("%-10f", 12345.6), fmt."%-10f\{12345.6}");
        test(String.format("%,f", -12345.6), fmt."%,f\{-12345.6}");
        test(String.format("%,f", 0.0), fmt."%,f\{0.0}");
        test(String.format("%,f", 12345.6), fmt."%,f\{12345.6}");
        test(String.format("%,10f", -12345.6), fmt."%,10f\{-12345.6}");
        test(String.format("%,10f", 0.0), fmt."%,10f\{0.0}");
        test(String.format("%,10f", 12345.6), fmt."%,10f\{12345.6}");
        test(String.format("%,-10f", -12345.6), fmt."%,-10f\{-12345.6}");
        test(String.format("%,-10f", 0.0), fmt."%,-10f\{0.0}");
        test(String.format("%,-10f", 12345.6), fmt."%,-10f\{12345.6}");

        test(String.format("%f", -12345.6), fmt."%f\{-12345.6}");
        test(String.format("%f", 0.0), fmt."%f\{0.0}");
        test(String.format("%f", 12345.6), fmt."%f\{12345.6}");
        test(String.format("%10f", -12345.6), fmt."%10f\{-12345.6}");
        test(String.format("%10f", 0.0), fmt."%10f\{0.0}");
        test(String.format("%10f", 12345.6), fmt."%10f\{12345.6}");
        test(String.format("%-10f", -12345.6), fmt."%-10f\{-12345.6}");
        test(String.format("%-10f", 0.0), fmt."%-10f\{0.0}");
        test(String.format("%-10f", 12345.6), fmt."%-10f\{12345.6}");
        test(String.format("%,f", -12345.6), fmt."%,f\{-12345.6}");
        test(String.format("%,f", 0.0), fmt."%,f\{0.0}");
        test(String.format("%,f", 12345.6), fmt."%,f\{12345.6}");
        test(String.format("%,10f", -12345.6), fmt."%,10f\{-12345.6}");
        test(String.format("%,10f", 0.0), fmt."%,10f\{0.0}");
        test(String.format("%,10f", 12345.6), fmt."%,10f\{12345.6}");
        test(String.format("%,-10f", -12345.6), fmt."%,-10f\{-12345.6}");
        test(String.format("%,-10f", 0.0), fmt."%,-10f\{0.0}");
        test(String.format("%,-10f", 12345.6), fmt."%,-10f\{12345.6}");
        test(String.format("% f", -12345.6), fmt."% f\{-12345.6}");
        test(String.format("% f", 0.0), fmt."% f\{0.0}");
        test(String.format("% f", 12345.6), fmt."% f\{12345.6}");
        test(String.format("% 10f", -12345.6), fmt."% 10f\{-12345.6}");
        test(String.format("% 10f", 0.0), fmt."% 10f\{0.0}");
        test(String.format("% 10f", 12345.6), fmt."% 10f\{12345.6}");
        test(String.format("% -10f", -12345.6), fmt."% -10f\{-12345.6}");
        test(String.format("% -10f", 0.0), fmt."% -10f\{0.0}");
        test(String.format("% -10f", 12345.6), fmt."% -10f\{12345.6}");
        test(String.format("%, f", -12345.6), fmt."%, f\{-12345.6}");
        test(String.format("%, f", 0.0), fmt."%, f\{0.0}");
        test(String.format("%, f", 12345.6), fmt."%, f\{12345.6}");
        test(String.format("%, 10f", -12345.6), fmt."%, 10f\{-12345.6}");
        test(String.format("%, 10f", 0.0), fmt."%, 10f\{0.0}");
        test(String.format("%, 10f", 12345.6), fmt."%, 10f\{12345.6}");
        test(String.format("%, -10f", -12345.6), fmt."%, -10f\{-12345.6}");
        test(String.format("%, -10f", 0.0), fmt."%, -10f\{0.0}");
        test(String.format("%, -10f", 12345.6), fmt."%, -10f\{12345.6}");

        test(String.format("%f", -12345.6), fmt."%f\{-12345.6}");
        test(String.format("%f", 0.0), fmt."%f\{0.0}");
        test(String.format("%f", 12345.6), fmt."%f\{12345.6}");
        test(String.format("%10f", -12345.6), fmt."%10f\{-12345.6}");
        test(String.format("%10f", 0.0), fmt."%10f\{0.0}");
        test(String.format("%10f", 12345.6), fmt."%10f\{12345.6}");
        test(String.format("%-10f", -12345.6), fmt."%-10f\{-12345.6}");
        test(String.format("%-10f", 0.0), fmt."%-10f\{0.0}");
        test(String.format("%-10f", 12345.6), fmt."%-10f\{12345.6}");
        test(String.format("%,f", -12345.6), fmt."%,f\{-12345.6}");
        test(String.format("%,f", 0.0), fmt."%,f\{0.0}");
        test(String.format("%,f", 12345.6), fmt."%,f\{12345.6}");
        test(String.format("%,10f", -12345.6), fmt."%,10f\{-12345.6}");
        test(String.format("%,10f", 0.0), fmt."%,10f\{0.0}");
        test(String.format("%,10f", 12345.6), fmt."%,10f\{12345.6}");
        test(String.format("%,-10f", -12345.6), fmt."%,-10f\{-12345.6}");
        test(String.format("%,-10f", 0.0), fmt."%,-10f\{0.0}");
        test(String.format("%,-10f", 12345.6), fmt."%,-10f\{12345.6}");
        test(String.format("%+f", -12345.6), fmt."%+f\{-12345.6}");
        test(String.format("%+f", 0.0), fmt."%+f\{0.0}");
        test(String.format("%+f", 12345.6), fmt."%+f\{12345.6}");
        test(String.format("%+10f", -12345.6), fmt."%+10f\{-12345.6}");
        test(String.format("%+10f", 0.0), fmt."%+10f\{0.0}");
        test(String.format("%+10f", 12345.6), fmt."%+10f\{12345.6}");
        test(String.format("%+-10f", -12345.6), fmt."%+-10f\{-12345.6}");
        test(String.format("%+-10f", 0.0), fmt."%+-10f\{0.0}");
        test(String.format("%+-10f", 12345.6), fmt."%+-10f\{12345.6}");
        test(String.format("%,+f", -12345.6), fmt."%,+f\{-12345.6}");
        test(String.format("%,+f", 0.0), fmt."%,+f\{0.0}");
        test(String.format("%,+f", 12345.6), fmt."%,+f\{12345.6}");
        test(String.format("%,+10f", -12345.6), fmt."%,+10f\{-12345.6}");
        test(String.format("%,+10f", 0.0), fmt."%,+10f\{0.0}");
        test(String.format("%,+10f", 12345.6), fmt."%,+10f\{12345.6}");
        test(String.format("%,+-10f", -12345.6), fmt."%,+-10f\{-12345.6}");
        test(String.format("%,+-10f", 0.0), fmt."%,+-10f\{0.0}");
        test(String.format("%,+-10f", 12345.6), fmt."%,+-10f\{12345.6}");

        test(String.format("%f", -12345.6), fmt."%f\{-12345.6}");
        test(String.format("%f", 0.0), fmt."%f\{0.0}");
        test(String.format("%f", 12345.6), fmt."%f\{12345.6}");
        test(String.format("%10f", -12345.6), fmt."%10f\{-12345.6}");
        test(String.format("%10f", 0.0), fmt."%10f\{0.0}");
        test(String.format("%10f", 12345.6), fmt."%10f\{12345.6}");
        test(String.format("%-10f", -12345.6), fmt."%-10f\{-12345.6}");
        test(String.format("%-10f", 0.0), fmt."%-10f\{0.0}");
        test(String.format("%-10f", 12345.6), fmt."%-10f\{12345.6}");
        test(String.format("%,f", -12345.6), fmt."%,f\{-12345.6}");
        test(String.format("%,f", 0.0), fmt."%,f\{0.0}");
        test(String.format("%,f", 12345.6), fmt."%,f\{12345.6}");
        test(String.format("%,10f", -12345.6), fmt."%,10f\{-12345.6}");
        test(String.format("%,10f", 0.0), fmt."%,10f\{0.0}");
        test(String.format("%,10f", 12345.6), fmt."%,10f\{12345.6}");
        test(String.format("%,-10f", -12345.6), fmt."%,-10f\{-12345.6}");
        test(String.format("%,-10f", 0.0), fmt."%,-10f\{0.0}");
        test(String.format("%,-10f", 12345.6), fmt."%,-10f\{12345.6}");
        test(String.format("%(f", -12345.6), fmt."%(f\{-12345.6}");
        test(String.format("%(f", 0.0), fmt."%(f\{0.0}");
        test(String.format("%(f", 12345.6), fmt."%(f\{12345.6}");
        test(String.format("%(10f", -12345.6), fmt."%(10f\{-12345.6}");
        test(String.format("%(10f", 0.0), fmt."%(10f\{0.0}");
        test(String.format("%(10f", 12345.6), fmt."%(10f\{12345.6}");
        test(String.format("%(-10f", -12345.6), fmt."%(-10f\{-12345.6}");
        test(String.format("%(-10f", 0.0), fmt."%(-10f\{0.0}");
        test(String.format("%(-10f", 12345.6), fmt."%(-10f\{12345.6}");
        test(String.format("%,(f", -12345.6), fmt."%,(f\{-12345.6}");
        test(String.format("%,(f", 0.0), fmt."%,(f\{0.0}");
        test(String.format("%,(f", 12345.6), fmt."%,(f\{12345.6}");
        test(String.format("%,(10f", -12345.6), fmt."%,(10f\{-12345.6}");
        test(String.format("%,(10f", 0.0), fmt."%,(10f\{0.0}");
        test(String.format("%,(10f", 12345.6), fmt."%,(10f\{12345.6}");
        test(String.format("%,(-10f", -12345.6), fmt."%,(-10f\{-12345.6}");
        test(String.format("%,(-10f", 0.0), fmt."%,(-10f\{0.0}");
        test(String.format("%,(-10f", 12345.6), fmt."%,(-10f\{12345.6}");
        test(String.format("%+f", -12345.6), fmt."%+f\{-12345.6}");
        test(String.format("%+f", 0.0), fmt."%+f\{0.0}");
        test(String.format("%+f", 12345.6), fmt."%+f\{12345.6}");
        test(String.format("%+10f", -12345.6), fmt."%+10f\{-12345.6}");
        test(String.format("%+10f", 0.0), fmt."%+10f\{0.0}");
        test(String.format("%+10f", 12345.6), fmt."%+10f\{12345.6}");
        test(String.format("%+-10f", -12345.6), fmt."%+-10f\{-12345.6}");
        test(String.format("%+-10f", 0.0), fmt."%+-10f\{0.0}");
        test(String.format("%+-10f", 12345.6), fmt."%+-10f\{12345.6}");
        test(String.format("%,+f", -12345.6), fmt."%,+f\{-12345.6}");
        test(String.format("%,+f", 0.0), fmt."%,+f\{0.0}");
        test(String.format("%,+f", 12345.6), fmt."%,+f\{12345.6}");
        test(String.format("%,+10f", -12345.6), fmt."%,+10f\{-12345.6}");
        test(String.format("%,+10f", 0.0), fmt."%,+10f\{0.0}");
        test(String.format("%,+10f", 12345.6), fmt."%,+10f\{12345.6}");
        test(String.format("%,+-10f", -12345.6), fmt."%,+-10f\{-12345.6}");
        test(String.format("%,+-10f", 0.0), fmt."%,+-10f\{0.0}");
        test(String.format("%,+-10f", 12345.6), fmt."%,+-10f\{12345.6}");
        test(String.format("%(+f", -12345.6), fmt."%(+f\{-12345.6}");
        test(String.format("%(+f", 0.0), fmt."%(+f\{0.0}");
        test(String.format("%(+f", 12345.6), fmt."%(+f\{12345.6}");
        test(String.format("%(+10f", -12345.6), fmt."%(+10f\{-12345.6}");
        test(String.format("%(+10f", 0.0), fmt."%(+10f\{0.0}");
        test(String.format("%(+10f", 12345.6), fmt."%(+10f\{12345.6}");
        test(String.format("%(+-10f", -12345.6), fmt."%(+-10f\{-12345.6}");
        test(String.format("%(+-10f", 0.0), fmt."%(+-10f\{0.0}");
        test(String.format("%(+-10f", 12345.6), fmt."%(+-10f\{12345.6}");
        test(String.format("%,(+f", -12345.6), fmt."%,(+f\{-12345.6}");
        test(String.format("%,(+f", 0.0), fmt."%,(+f\{0.0}");
        test(String.format("%,(+f", 12345.6), fmt."%,(+f\{12345.6}");
        test(String.format("%,(+10f", -12345.6), fmt."%,(+10f\{-12345.6}");
        test(String.format("%,(+10f", 0.0), fmt."%,(+10f\{0.0}");
        test(String.format("%,(+10f", 12345.6), fmt."%,(+10f\{12345.6}");
        test(String.format("%,(+-10f", -12345.6), fmt."%,(+-10f\{-12345.6}");
        test(String.format("%,(+-10f", 0.0), fmt."%,(+-10f\{0.0}");
        test(String.format("%,(+-10f", 12345.6), fmt."%,(+-10f\{12345.6}");

        test(String.format("%e", -12345.6), fmt."%e\{-12345.6}");
        test(String.format("%e", 0.0), fmt."%e\{0.0}");
        test(String.format("%e", 12345.6), fmt."%e\{12345.6}");
        test(String.format("%10e", -12345.6), fmt."%10e\{-12345.6}");
        test(String.format("%10e", 0.0), fmt."%10e\{0.0}");
        test(String.format("%10e", 12345.6), fmt."%10e\{12345.6}");
        test(String.format("%-10e", -12345.6), fmt."%-10e\{-12345.6}");
        test(String.format("%-10e", 0.0), fmt."%-10e\{0.0}");
        test(String.format("%-10e", 12345.6), fmt."%-10e\{12345.6}");
        test(String.format("%E", -12345.6), fmt."%E\{-12345.6}");
        test(String.format("%E", 0.0), fmt."%E\{0.0}");
        test(String.format("%E", 12345.6), fmt."%E\{12345.6}");
        test(String.format("%10E", -12345.6), fmt."%10E\{-12345.6}");
        test(String.format("%10E", 0.0), fmt."%10E\{0.0}");
        test(String.format("%10E", 12345.6), fmt."%10E\{12345.6}");
        test(String.format("%-10E", -12345.6), fmt."%-10E\{-12345.6}");
        test(String.format("%-10E", 0.0), fmt."%-10E\{0.0}");
        test(String.format("%-10E", 12345.6), fmt."%-10E\{12345.6}");

        test(String.format("%g", -12345.6), fmt."%g\{-12345.6}");
        test(String.format("%g", 0.0), fmt."%g\{0.0}");
        test(String.format("%g", 12345.6), fmt."%g\{12345.6}");
        test(String.format("%10g", -12345.6), fmt."%10g\{-12345.6}");
        test(String.format("%10g", 0.0), fmt."%10g\{0.0}");
        test(String.format("%10g", 12345.6), fmt."%10g\{12345.6}");
        test(String.format("%-10g", -12345.6), fmt."%-10g\{-12345.6}");
        test(String.format("%-10g", 0.0), fmt."%-10g\{0.0}");
        test(String.format("%-10g", 12345.6), fmt."%-10g\{12345.6}");
        test(String.format("%G", -12345.6), fmt."%G\{-12345.6}");
        test(String.format("%G", 0.0), fmt."%G\{0.0}");
        test(String.format("%G", 12345.6), fmt."%G\{12345.6}");
        test(String.format("%10G", -12345.6), fmt."%10G\{-12345.6}");
        test(String.format("%10G", 0.0), fmt."%10G\{0.0}");
        test(String.format("%10G", 12345.6), fmt."%10G\{12345.6}");
        test(String.format("%-10G", -12345.6), fmt."%-10G\{-12345.6}");
        test(String.format("%-10G", 0.0), fmt."%-10G\{0.0}");
        test(String.format("%-10G", 12345.6), fmt."%-10G\{12345.6}");
        test(String.format("%,g", -12345.6), fmt."%,g\{-12345.6}");
        test(String.format("%,g", 0.0), fmt."%,g\{0.0}");
        test(String.format("%,g", 12345.6), fmt."%,g\{12345.6}");
        test(String.format("%,10g", -12345.6), fmt."%,10g\{-12345.6}");
        test(String.format("%,10g", 0.0), fmt."%,10g\{0.0}");
        test(String.format("%,10g", 12345.6), fmt."%,10g\{12345.6}");
        test(String.format("%,-10g", -12345.6), fmt."%,-10g\{-12345.6}");
        test(String.format("%,-10g", 0.0), fmt."%,-10g\{0.0}");
        test(String.format("%,-10g", 12345.6), fmt."%,-10g\{12345.6}");
        test(String.format("%,G", -12345.6), fmt."%,G\{-12345.6}");
        test(String.format("%,G", 0.0), fmt."%,G\{0.0}");
        test(String.format("%,G", 12345.6), fmt."%,G\{12345.6}");
        test(String.format("%,10G", -12345.6), fmt."%,10G\{-12345.6}");
        test(String.format("%,10G", 0.0), fmt."%,10G\{0.0}");
        test(String.format("%,10G", 12345.6), fmt."%,10G\{12345.6}");
        test(String.format("%,-10G", -12345.6), fmt."%,-10G\{-12345.6}");
        test(String.format("%,-10G", 0.0), fmt."%,-10G\{0.0}");
        test(String.format("%,-10G", 12345.6), fmt."%,-10G\{12345.6}");

        test(String.format("%g", -12345.6), fmt."%g\{-12345.6}");
        test(String.format("%g", 0.0), fmt."%g\{0.0}");
        test(String.format("%g", 12345.6), fmt."%g\{12345.6}");
        test(String.format("%10g", -12345.6), fmt."%10g\{-12345.6}");
        test(String.format("%10g", 0.0), fmt."%10g\{0.0}");
        test(String.format("%10g", 12345.6), fmt."%10g\{12345.6}");
        test(String.format("%-10g", -12345.6), fmt."%-10g\{-12345.6}");
        test(String.format("%-10g", 0.0), fmt."%-10g\{0.0}");
        test(String.format("%-10g", 12345.6), fmt."%-10g\{12345.6}");
        test(String.format("%G", -12345.6), fmt."%G\{-12345.6}");
        test(String.format("%G", 0.0), fmt."%G\{0.0}");
        test(String.format("%G", 12345.6), fmt."%G\{12345.6}");
        test(String.format("%10G", -12345.6), fmt."%10G\{-12345.6}");
        test(String.format("%10G", 0.0), fmt."%10G\{0.0}");
        test(String.format("%10G", 12345.6), fmt."%10G\{12345.6}");
        test(String.format("%-10G", -12345.6), fmt."%-10G\{-12345.6}");
        test(String.format("%-10G", 0.0), fmt."%-10G\{0.0}");
        test(String.format("%-10G", 12345.6), fmt."%-10G\{12345.6}");
        test(String.format("%,g", -12345.6), fmt."%,g\{-12345.6}");
        test(String.format("%,g", 0.0), fmt."%,g\{0.0}");
        test(String.format("%,g", 12345.6), fmt."%,g\{12345.6}");
        test(String.format("%,10g", -12345.6), fmt."%,10g\{-12345.6}");
        test(String.format("%,10g", 0.0), fmt."%,10g\{0.0}");
        test(String.format("%,10g", 12345.6), fmt."%,10g\{12345.6}");
        test(String.format("%,-10g", -12345.6), fmt."%,-10g\{-12345.6}");
        test(String.format("%,-10g", 0.0), fmt."%,-10g\{0.0}");
        test(String.format("%,-10g", 12345.6), fmt."%,-10g\{12345.6}");
        test(String.format("%,G", -12345.6), fmt."%,G\{-12345.6}");
        test(String.format("%,G", 0.0), fmt."%,G\{0.0}");
        test(String.format("%,G", 12345.6), fmt."%,G\{12345.6}");
        test(String.format("%,10G", -12345.6), fmt."%,10G\{-12345.6}");
        test(String.format("%,10G", 0.0), fmt."%,10G\{0.0}");
        test(String.format("%,10G", 12345.6), fmt."%,10G\{12345.6}");
        test(String.format("%,-10G", -12345.6), fmt."%,-10G\{-12345.6}");
        test(String.format("%,-10G", 0.0), fmt."%,-10G\{0.0}");
        test(String.format("%,-10G", 12345.6), fmt."%,-10G\{12345.6}");
        test(String.format("% g", -12345.6), fmt."% g\{-12345.6}");
        test(String.format("% g", 0.0), fmt."% g\{0.0}");
        test(String.format("% g", 12345.6), fmt."% g\{12345.6}");
        test(String.format("% 10g", -12345.6), fmt."% 10g\{-12345.6}");
        test(String.format("% 10g", 0.0), fmt."% 10g\{0.0}");
        test(String.format("% 10g", 12345.6), fmt."% 10g\{12345.6}");
        test(String.format("% -10g", -12345.6), fmt."% -10g\{-12345.6}");
        test(String.format("% -10g", 0.0), fmt."% -10g\{0.0}");
        test(String.format("% -10g", 12345.6), fmt."% -10g\{12345.6}");
        test(String.format("% G", -12345.6), fmt."% G\{-12345.6}");
        test(String.format("% G", 0.0), fmt."% G\{0.0}");
        test(String.format("% G", 12345.6), fmt."% G\{12345.6}");
        test(String.format("% 10G", -12345.6), fmt."% 10G\{-12345.6}");
        test(String.format("% 10G", 0.0), fmt."% 10G\{0.0}");
        test(String.format("% 10G", 12345.6), fmt."% 10G\{12345.6}");
        test(String.format("% -10G", -12345.6), fmt."% -10G\{-12345.6}");
        test(String.format("% -10G", 0.0), fmt."% -10G\{0.0}");
        test(String.format("% -10G", 12345.6), fmt."% -10G\{12345.6}");
        test(String.format("%, g", -12345.6), fmt."%, g\{-12345.6}");
        test(String.format("%, g", 0.0), fmt."%, g\{0.0}");
        test(String.format("%, g", 12345.6), fmt."%, g\{12345.6}");
        test(String.format("%, 10g", -12345.6), fmt."%, 10g\{-12345.6}");
        test(String.format("%, 10g", 0.0), fmt."%, 10g\{0.0}");
        test(String.format("%, 10g", 12345.6), fmt."%, 10g\{12345.6}");
        test(String.format("%, -10g", -12345.6), fmt."%, -10g\{-12345.6}");
        test(String.format("%, -10g", 0.0), fmt."%, -10g\{0.0}");
        test(String.format("%, -10g", 12345.6), fmt."%, -10g\{12345.6}");
        test(String.format("%, G", -12345.6), fmt."%, G\{-12345.6}");
        test(String.format("%, G", 0.0), fmt."%, G\{0.0}");
        test(String.format("%, G", 12345.6), fmt."%, G\{12345.6}");
        test(String.format("%, 10G", -12345.6), fmt."%, 10G\{-12345.6}");
        test(String.format("%, 10G", 0.0), fmt."%, 10G\{0.0}");
        test(String.format("%, 10G", 12345.6), fmt."%, 10G\{12345.6}");
        test(String.format("%, -10G", -12345.6), fmt."%, -10G\{-12345.6}");
        test(String.format("%, -10G", 0.0), fmt."%, -10G\{0.0}");
        test(String.format("%, -10G", 12345.6), fmt."%, -10G\{12345.6}");

        test(String.format("%g", -12345.6), fmt."%g\{-12345.6}");
        test(String.format("%g", 0.0), fmt."%g\{0.0}");
        test(String.format("%g", 12345.6), fmt."%g\{12345.6}");
        test(String.format("%10g", -12345.6), fmt."%10g\{-12345.6}");
        test(String.format("%10g", 0.0), fmt."%10g\{0.0}");
        test(String.format("%10g", 12345.6), fmt."%10g\{12345.6}");
        test(String.format("%-10g", -12345.6), fmt."%-10g\{-12345.6}");
        test(String.format("%-10g", 0.0), fmt."%-10g\{0.0}");
        test(String.format("%-10g", 12345.6), fmt."%-10g\{12345.6}");
        test(String.format("%G", -12345.6), fmt."%G\{-12345.6}");
        test(String.format("%G", 0.0), fmt."%G\{0.0}");
        test(String.format("%G", 12345.6), fmt."%G\{12345.6}");
        test(String.format("%10G", -12345.6), fmt."%10G\{-12345.6}");
        test(String.format("%10G", 0.0), fmt."%10G\{0.0}");
        test(String.format("%10G", 12345.6), fmt."%10G\{12345.6}");
        test(String.format("%-10G", -12345.6), fmt."%-10G\{-12345.6}");
        test(String.format("%-10G", 0.0), fmt."%-10G\{0.0}");
        test(String.format("%-10G", 12345.6), fmt."%-10G\{12345.6}");
        test(String.format("%,g", -12345.6), fmt."%,g\{-12345.6}");
        test(String.format("%,g", 0.0), fmt."%,g\{0.0}");
        test(String.format("%,g", 12345.6), fmt."%,g\{12345.6}");
        test(String.format("%,10g", -12345.6), fmt."%,10g\{-12345.6}");
        test(String.format("%,10g", 0.0), fmt."%,10g\{0.0}");
        test(String.format("%,10g", 12345.6), fmt."%,10g\{12345.6}");
        test(String.format("%,-10g", -12345.6), fmt."%,-10g\{-12345.6}");
        test(String.format("%,-10g", 0.0), fmt."%,-10g\{0.0}");
        test(String.format("%,-10g", 12345.6), fmt."%,-10g\{12345.6}");
        test(String.format("%,G", -12345.6), fmt."%,G\{-12345.6}");
        test(String.format("%,G", 0.0), fmt."%,G\{0.0}");
        test(String.format("%,G", 12345.6), fmt."%,G\{12345.6}");
        test(String.format("%,10G", -12345.6), fmt."%,10G\{-12345.6}");
        test(String.format("%,10G", 0.0), fmt."%,10G\{0.0}");
        test(String.format("%,10G", 12345.6), fmt."%,10G\{12345.6}");
        test(String.format("%,-10G", -12345.6), fmt."%,-10G\{-12345.6}");
        test(String.format("%,-10G", 0.0), fmt."%,-10G\{0.0}");
        test(String.format("%,-10G", 12345.6), fmt."%,-10G\{12345.6}");
        test(String.format("%+g", -12345.6), fmt."%+g\{-12345.6}");
        test(String.format("%+g", 0.0), fmt."%+g\{0.0}");
        test(String.format("%+g", 12345.6), fmt."%+g\{12345.6}");
        test(String.format("%+10g", -12345.6), fmt."%+10g\{-12345.6}");
        test(String.format("%+10g", 0.0), fmt."%+10g\{0.0}");
        test(String.format("%+10g", 12345.6), fmt."%+10g\{12345.6}");
        test(String.format("%+-10g", -12345.6), fmt."%+-10g\{-12345.6}");
        test(String.format("%+-10g", 0.0), fmt."%+-10g\{0.0}");
        test(String.format("%+-10g", 12345.6), fmt."%+-10g\{12345.6}");
        test(String.format("%+G", -12345.6), fmt."%+G\{-12345.6}");
        test(String.format("%+G", 0.0), fmt."%+G\{0.0}");
        test(String.format("%+G", 12345.6), fmt."%+G\{12345.6}");
        test(String.format("%+10G", -12345.6), fmt."%+10G\{-12345.6}");
        test(String.format("%+10G", 0.0), fmt."%+10G\{0.0}");
        test(String.format("%+10G", 12345.6), fmt."%+10G\{12345.6}");
        test(String.format("%+-10G", -12345.6), fmt."%+-10G\{-12345.6}");
        test(String.format("%+-10G", 0.0), fmt."%+-10G\{0.0}");
        test(String.format("%+-10G", 12345.6), fmt."%+-10G\{12345.6}");
        test(String.format("%,+g", -12345.6), fmt."%,+g\{-12345.6}");
        test(String.format("%,+g", 0.0), fmt."%,+g\{0.0}");
        test(String.format("%,+g", 12345.6), fmt."%,+g\{12345.6}");
        test(String.format("%,+10g", -12345.6), fmt."%,+10g\{-12345.6}");
        test(String.format("%,+10g", 0.0), fmt."%,+10g\{0.0}");
        test(String.format("%,+10g", 12345.6), fmt."%,+10g\{12345.6}");
        test(String.format("%,+-10g", -12345.6), fmt."%,+-10g\{-12345.6}");
        test(String.format("%,+-10g", 0.0), fmt."%,+-10g\{0.0}");
        test(String.format("%,+-10g", 12345.6), fmt."%,+-10g\{12345.6}");
        test(String.format("%,+G", -12345.6), fmt."%,+G\{-12345.6}");
        test(String.format("%,+G", 0.0), fmt."%,+G\{0.0}");
        test(String.format("%,+G", 12345.6), fmt."%,+G\{12345.6}");
        test(String.format("%,+10G", -12345.6), fmt."%,+10G\{-12345.6}");
        test(String.format("%,+10G", 0.0), fmt."%,+10G\{0.0}");
        test(String.format("%,+10G", 12345.6), fmt."%,+10G\{12345.6}");
        test(String.format("%,+-10G", -12345.6), fmt."%,+-10G\{-12345.6}");
        test(String.format("%,+-10G", 0.0), fmt."%,+-10G\{0.0}");
        test(String.format("%,+-10G", 12345.6), fmt."%,+-10G\{12345.6}");

        test(String.format("%g", -12345.6), fmt."%g\{-12345.6}");
        test(String.format("%g", 0.0), fmt."%g\{0.0}");
        test(String.format("%g", 12345.6), fmt."%g\{12345.6}");
        test(String.format("%10g", -12345.6), fmt."%10g\{-12345.6}");
        test(String.format("%10g", 0.0), fmt."%10g\{0.0}");
        test(String.format("%10g", 12345.6), fmt."%10g\{12345.6}");
        test(String.format("%-10g", -12345.6), fmt."%-10g\{-12345.6}");
        test(String.format("%-10g", 0.0), fmt."%-10g\{0.0}");
        test(String.format("%-10g", 12345.6), fmt."%-10g\{12345.6}");
        test(String.format("%G", -12345.6), fmt."%G\{-12345.6}");
        test(String.format("%G", 0.0), fmt."%G\{0.0}");
        test(String.format("%G", 12345.6), fmt."%G\{12345.6}");
        test(String.format("%10G", -12345.6), fmt."%10G\{-12345.6}");
        test(String.format("%10G", 0.0), fmt."%10G\{0.0}");
        test(String.format("%10G", 12345.6), fmt."%10G\{12345.6}");
        test(String.format("%-10G", -12345.6), fmt."%-10G\{-12345.6}");
        test(String.format("%-10G", 0.0), fmt."%-10G\{0.0}");
        test(String.format("%-10G", 12345.6), fmt."%-10G\{12345.6}");
        test(String.format("%,g", -12345.6), fmt."%,g\{-12345.6}");
        test(String.format("%,g", 0.0), fmt."%,g\{0.0}");
        test(String.format("%,g", 12345.6), fmt."%,g\{12345.6}");
        test(String.format("%,10g", -12345.6), fmt."%,10g\{-12345.6}");
        test(String.format("%,10g", 0.0), fmt."%,10g\{0.0}");
        test(String.format("%,10g", 12345.6), fmt."%,10g\{12345.6}");
        test(String.format("%,-10g", -12345.6), fmt."%,-10g\{-12345.6}");
        test(String.format("%,-10g", 0.0), fmt."%,-10g\{0.0}");
        test(String.format("%,-10g", 12345.6), fmt."%,-10g\{12345.6}");
        test(String.format("%,G", -12345.6), fmt."%,G\{-12345.6}");
        test(String.format("%,G", 0.0), fmt."%,G\{0.0}");
        test(String.format("%,G", 12345.6), fmt."%,G\{12345.6}");
        test(String.format("%,10G", -12345.6), fmt."%,10G\{-12345.6}");
        test(String.format("%,10G", 0.0), fmt."%,10G\{0.0}");
        test(String.format("%,10G", 12345.6), fmt."%,10G\{12345.6}");
        test(String.format("%,-10G", -12345.6), fmt."%,-10G\{-12345.6}");
        test(String.format("%,-10G", 0.0), fmt."%,-10G\{0.0}");
        test(String.format("%,-10G", 12345.6), fmt."%,-10G\{12345.6}");
        test(String.format("%(g", -12345.6), fmt."%(g\{-12345.6}");
        test(String.format("%(g", 0.0), fmt."%(g\{0.0}");
        test(String.format("%(g", 12345.6), fmt."%(g\{12345.6}");
        test(String.format("%(10g", -12345.6), fmt."%(10g\{-12345.6}");
        test(String.format("%(10g", 0.0), fmt."%(10g\{0.0}");
        test(String.format("%(10g", 12345.6), fmt."%(10g\{12345.6}");
        test(String.format("%(-10g", -12345.6), fmt."%(-10g\{-12345.6}");
        test(String.format("%(-10g", 0.0), fmt."%(-10g\{0.0}");
        test(String.format("%(-10g", 12345.6), fmt."%(-10g\{12345.6}");
        test(String.format("%(G", -12345.6), fmt."%(G\{-12345.6}");
        test(String.format("%(G", 0.0), fmt."%(G\{0.0}");
        test(String.format("%(G", 12345.6), fmt."%(G\{12345.6}");
        test(String.format("%(10G", -12345.6), fmt."%(10G\{-12345.6}");
        test(String.format("%(10G", 0.0), fmt."%(10G\{0.0}");
        test(String.format("%(10G", 12345.6), fmt."%(10G\{12345.6}");
        test(String.format("%(-10G", -12345.6), fmt."%(-10G\{-12345.6}");
        test(String.format("%(-10G", 0.0), fmt."%(-10G\{0.0}");
        test(String.format("%(-10G", 12345.6), fmt."%(-10G\{12345.6}");
        test(String.format("%,(g", -12345.6), fmt."%,(g\{-12345.6}");
        test(String.format("%,(g", 0.0), fmt."%,(g\{0.0}");
        test(String.format("%,(g", 12345.6), fmt."%,(g\{12345.6}");
        test(String.format("%,(10g", -12345.6), fmt."%,(10g\{-12345.6}");
        test(String.format("%,(10g", 0.0), fmt."%,(10g\{0.0}");
        test(String.format("%,(10g", 12345.6), fmt."%,(10g\{12345.6}");
        test(String.format("%,(-10g", -12345.6), fmt."%,(-10g\{-12345.6}");
        test(String.format("%,(-10g", 0.0), fmt."%,(-10g\{0.0}");
        test(String.format("%,(-10g", 12345.6), fmt."%,(-10g\{12345.6}");
        test(String.format("%,(G", -12345.6), fmt."%,(G\{-12345.6}");
        test(String.format("%,(G", 0.0), fmt."%,(G\{0.0}");
        test(String.format("%,(G", 12345.6), fmt."%,(G\{12345.6}");
        test(String.format("%,(10G", -12345.6), fmt."%,(10G\{-12345.6}");
        test(String.format("%,(10G", 0.0), fmt."%,(10G\{0.0}");
        test(String.format("%,(10G", 12345.6), fmt."%,(10G\{12345.6}");
        test(String.format("%,(-10G", -12345.6), fmt."%,(-10G\{-12345.6}");
        test(String.format("%,(-10G", 0.0), fmt."%,(-10G\{0.0}");
        test(String.format("%,(-10G", 12345.6), fmt."%,(-10G\{12345.6}");
        test(String.format("%+g", -12345.6), fmt."%+g\{-12345.6}");
        test(String.format("%+g", 0.0), fmt."%+g\{0.0}");
        test(String.format("%+g", 12345.6), fmt."%+g\{12345.6}");
        test(String.format("%+10g", -12345.6), fmt."%+10g\{-12345.6}");
        test(String.format("%+10g", 0.0), fmt."%+10g\{0.0}");
        test(String.format("%+10g", 12345.6), fmt."%+10g\{12345.6}");
        test(String.format("%+-10g", -12345.6), fmt."%+-10g\{-12345.6}");
        test(String.format("%+-10g", 0.0), fmt."%+-10g\{0.0}");
        test(String.format("%+-10g", 12345.6), fmt."%+-10g\{12345.6}");
        test(String.format("%+G", -12345.6), fmt."%+G\{-12345.6}");
        test(String.format("%+G", 0.0), fmt."%+G\{0.0}");
        test(String.format("%+G", 12345.6), fmt."%+G\{12345.6}");
        test(String.format("%+10G", -12345.6), fmt."%+10G\{-12345.6}");
        test(String.format("%+10G", 0.0), fmt."%+10G\{0.0}");
        test(String.format("%+10G", 12345.6), fmt."%+10G\{12345.6}");
        test(String.format("%+-10G", -12345.6), fmt."%+-10G\{-12345.6}");
        test(String.format("%+-10G", 0.0), fmt."%+-10G\{0.0}");
        test(String.format("%+-10G", 12345.6), fmt."%+-10G\{12345.6}");
        test(String.format("%,+g", -12345.6), fmt."%,+g\{-12345.6}");
        test(String.format("%,+g", 0.0), fmt."%,+g\{0.0}");
        test(String.format("%,+g", 12345.6), fmt."%,+g\{12345.6}");
        test(String.format("%,+10g", -12345.6), fmt."%,+10g\{-12345.6}");
        test(String.format("%,+10g", 0.0), fmt."%,+10g\{0.0}");
        test(String.format("%,+10g", 12345.6), fmt."%,+10g\{12345.6}");
        test(String.format("%,+-10g", -12345.6), fmt."%,+-10g\{-12345.6}");
        test(String.format("%,+-10g", 0.0), fmt."%,+-10g\{0.0}");
        test(String.format("%,+-10g", 12345.6), fmt."%,+-10g\{12345.6}");
        test(String.format("%,+G", -12345.6), fmt."%,+G\{-12345.6}");
        test(String.format("%,+G", 0.0), fmt."%,+G\{0.0}");
        test(String.format("%,+G", 12345.6), fmt."%,+G\{12345.6}");
        test(String.format("%,+10G", -12345.6), fmt."%,+10G\{-12345.6}");
        test(String.format("%,+10G", 0.0), fmt."%,+10G\{0.0}");
        test(String.format("%,+10G", 12345.6), fmt."%,+10G\{12345.6}");
        test(String.format("%,+-10G", -12345.6), fmt."%,+-10G\{-12345.6}");
        test(String.format("%,+-10G", 0.0), fmt."%,+-10G\{0.0}");
        test(String.format("%,+-10G", 12345.6), fmt."%,+-10G\{12345.6}");
        test(String.format("%(+g", -12345.6), fmt."%(+g\{-12345.6}");
        test(String.format("%(+g", 0.0), fmt."%(+g\{0.0}");
        test(String.format("%(+g", 12345.6), fmt."%(+g\{12345.6}");
        test(String.format("%(+10g", -12345.6), fmt."%(+10g\{-12345.6}");
        test(String.format("%(+10g", 0.0), fmt."%(+10g\{0.0}");
        test(String.format("%(+10g", 12345.6), fmt."%(+10g\{12345.6}");
        test(String.format("%(+-10g", -12345.6), fmt."%(+-10g\{-12345.6}");
        test(String.format("%(+-10g", 0.0), fmt."%(+-10g\{0.0}");
        test(String.format("%(+-10g", 12345.6), fmt."%(+-10g\{12345.6}");
        test(String.format("%(+G", -12345.6), fmt."%(+G\{-12345.6}");
        test(String.format("%(+G", 0.0), fmt."%(+G\{0.0}");
        test(String.format("%(+G", 12345.6), fmt."%(+G\{12345.6}");
        test(String.format("%(+10G", -12345.6), fmt."%(+10G\{-12345.6}");
        test(String.format("%(+10G", 0.0), fmt."%(+10G\{0.0}");
        test(String.format("%(+10G", 12345.6), fmt."%(+10G\{12345.6}");
        test(String.format("%(+-10G", -12345.6), fmt."%(+-10G\{-12345.6}");
        test(String.format("%(+-10G", 0.0), fmt."%(+-10G\{0.0}");
        test(String.format("%(+-10G", 12345.6), fmt."%(+-10G\{12345.6}");
        test(String.format("%,(+g", -12345.6), fmt."%,(+g\{-12345.6}");
        test(String.format("%,(+g", 0.0), fmt."%,(+g\{0.0}");
        test(String.format("%,(+g", 12345.6), fmt."%,(+g\{12345.6}");
        test(String.format("%,(+10g", -12345.6), fmt."%,(+10g\{-12345.6}");
        test(String.format("%,(+10g", 0.0), fmt."%,(+10g\{0.0}");
        test(String.format("%,(+10g", 12345.6), fmt."%,(+10g\{12345.6}");
        test(String.format("%,(+-10g", -12345.6), fmt."%,(+-10g\{-12345.6}");
        test(String.format("%,(+-10g", 0.0), fmt."%,(+-10g\{0.0}");
        test(String.format("%,(+-10g", 12345.6), fmt."%,(+-10g\{12345.6}");
        test(String.format("%,(+G", -12345.6), fmt."%,(+G\{-12345.6}");
        test(String.format("%,(+G", 0.0), fmt."%,(+G\{0.0}");
        test(String.format("%,(+G", 12345.6), fmt."%,(+G\{12345.6}");
        test(String.format("%,(+10G", -12345.6), fmt."%,(+10G\{-12345.6}");
        test(String.format("%,(+10G", 0.0), fmt."%,(+10G\{0.0}");
        test(String.format("%,(+10G", 12345.6), fmt."%,(+10G\{12345.6}");
        test(String.format("%,(+-10G", -12345.6), fmt."%,(+-10G\{-12345.6}");
        test(String.format("%,(+-10G", 0.0), fmt."%,(+-10G\{0.0}");
        test(String.format("%,(+-10G", 12345.6), fmt."%,(+-10G\{12345.6}");

        test(String.format("%a", -12345.6), fmt."%a\{-12345.6}");
        test(String.format("%a", 0.0), fmt."%a\{0.0}");
        test(String.format("%a", 12345.6), fmt."%a\{12345.6}");
        test(String.format("%10a", -12345.6), fmt."%10a\{-12345.6}");
        test(String.format("%10a", 0.0), fmt."%10a\{0.0}");
        test(String.format("%10a", 12345.6), fmt."%10a\{12345.6}");
        test(String.format("%-10a", -12345.6), fmt."%-10a\{-12345.6}");
        test(String.format("%-10a", 0.0), fmt."%-10a\{0.0}");
        test(String.format("%-10a", 12345.6), fmt."%-10a\{12345.6}");
        test(String.format("%A", -12345.6), fmt."%A\{-12345.6}");
        test(String.format("%A", 0.0), fmt."%A\{0.0}");
        test(String.format("%A", 12345.6), fmt."%A\{12345.6}");
        test(String.format("%10A", -12345.6), fmt."%10A\{-12345.6}");
        test(String.format("%10A", 0.0), fmt."%10A\{0.0}");
        test(String.format("%10A", 12345.6), fmt."%10A\{12345.6}");
        test(String.format("%-10A", -12345.6), fmt."%-10A\{-12345.6}");
        test(String.format("%-10A", 0.0), fmt."%-10A\{0.0}");
        test(String.format("%-10A", 12345.6), fmt."%-10A\{12345.6}");

        test("aaa%false", fmt."aaa%%%b\{false}");
        test("aaa" + System.lineSeparator() + "false", fmt."aaa%n%b\{false}");

        assertThrows(
                MissingFormatArgumentException.class,
                () -> fmt. "%10ba\{ false }",
                "Format specifier '%10b is not immediately followed by an embedded expression'");

        assertThrows(
                MissingFormatArgumentException.class,
                () ->fmt. "%ba\{ false }",
                "Format specifier '%b is not immediately followed by an embedded expression'");

        assertThrows(
                MissingFormatArgumentException.class,
                () ->fmt. "%b",
                "Format specifier '%b is not immediately followed by an embedded expression'");
        assertThrows(
                UnknownFormatConversionException.class,
                () ->fmt. "%0",
                "Conversion = '0'");
    }
}
