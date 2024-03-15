/*
 * Copyright (c) 2024, Oracle and/or its affiliates. All rights reserved.
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
 * @bug 8051959
 * @summary Option to print extra information in java.security.debug output
 * @library /test/lib
 * @run junit DebugOptions
 */

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.security.KeyStore;
import java.security.Security;
import java.util.stream.Stream;

import jdk.test.lib.process.OutputAnalyzer;
import jdk.test.lib.process.ProcessTools;

public class DebugOptions {

    private static Stream<Arguments> patternMatches() {
        return Stream.of(
                // no extra info present
                Arguments.of("properties",
                        "properties: Initial",
                        "properties\\["),
                // thread info only
                Arguments.of("properties+thread",
                        "properties\\[.*\\|main|\\.*java.*]:",
                        "UTC]"),
                // timestamp info only
                Arguments.of("properties+timestamp",
                        "properties\\[.*UTC\\]",
                        "\\|main\\]:"),
                // both thread and timestamp
                Arguments.of("properties+timestamp+thread",
                        "properties\\[.*\\|main\\|.*UTC\\]:",
                        "properties:"),
                // flip the arguments of previous test
                Arguments.of("properties+thread+timestamp",
                        "properties\\[.*\\|main\\|.*UTC\\]:",
                        "properties:"),
                // comma not valid separator, ignore extra info printing request
                Arguments.of("properties,thread,timestamp",
                        "properties:",
                        "properties\\[.*\\|main\\|.*UTC\\]:"),
                // no extra info for keystore debug prints
                Arguments.of("properties+thread+timestamp,keystore",
                        "properties\\[.*\\|main\\|.*UTC\\]:",
                        "keystore\\["),
                // flip arguments around in last test - same outcome expected
                Arguments.of("keystore,properties+thread+timestamp",
                        "properties\\[.*\\|main\\|.*UTC\\]:",
                        "keystore\\["),
                // turn on thread info for both keystore and properties components
                Arguments.of("keystore+thread,properties+thread",
                        "properties\\[.*\\|main.*\\Rkeystore\\[.*\\|main|.*\\]:",
                        "UTC]"),
                // same as above with erroneous comma at end of string. same output expected
                Arguments.of("keystore+thread,properties+thread,",
                        "properties\\[.*\\|main.*\\Rkeystore\\[.*\\|main|.*\\]:",
                        "UTC]"),
                // turn on thread info for properties and timestamp for keystore
                Arguments.of("keystore+timestamp,properties+thread",
                        "properties\\[.*\\|main|.*\\Rkeystore\\[.*UTC\\]:",
                        "properties\\[.*UTC\\]:"),
                // turn on thread info for all components
                Arguments.of("all+thread",
                        "properties\\[.*\\|main.*((.*\\R)*)keystore\\[.*\\|main.*java.*\\]:",
                        "properties\\[.*UTC\\]:"),
                // turn on thread info and timestamp for all components
                Arguments.of("all+thread+timestamp",
                        "properties\\[.*\\|main.*UTC\\]((.*\\R)*)keystore\\[.*\\|main.*UTC\\]:",
                        "properties:"),
                // all decorator option should override other component options
                Arguments.of("all+thread+timestamp,properties",
                        "properties\\[.*\\|main.*UTC\\]((.*\\R)*)keystore\\[.*\\|main.*UTC\\]:",
                        "properties:")
        );
    }

    @ParameterizedTest
    @MethodSource("patternMatches")
    public void shouldContain(String params, String expected, String notExpected) throws Exception {
        OutputAnalyzer outputAnalyzer = ProcessTools.executeTestJava(
                "-Duser.timezone=UTC",
                "-Djava.security.debug=" + params,
                "DebugOptions"
        );
        outputAnalyzer.shouldHaveExitValue(0)
                .shouldMatch(expected)
                .shouldNotMatch(notExpected);
    }

    public static void main(String[] args) throws Exception {
        // something to trigger "properties" debug output
        Security.getProperty("test");
        // trigger "keystore" debug output
        KeyStore ks = KeyStore.getInstance("PKCS12");
        ks.load(null, null);
    }
}
