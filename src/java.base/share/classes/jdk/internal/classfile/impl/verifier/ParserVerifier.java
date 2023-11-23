/*
 * Copyright (c) 2023, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
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
package jdk.internal.classfile.impl.verifier;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.function.Consumer;
import jdk.internal.classfile.Attribute;
import jdk.internal.classfile.AttributedElement;
import jdk.internal.classfile.ClassModel;
import jdk.internal.classfile.ClassfileElement;
import jdk.internal.classfile.CodeModel;
import jdk.internal.classfile.CompoundElement;
import jdk.internal.classfile.FieldModel;
import jdk.internal.classfile.MethodModel;
import jdk.internal.classfile.attribute.*;
import jdk.internal.classfile.impl.BoundAttribute;

/**
 * @see <a href="https://raw.githubusercontent.com/openjdk/jdk/master/src/hotspot/share/classfile/classFileParser.cpp">hotspot/share/classfile/classFileParser.cpp</a>
 */
public class ParserVerifier {

    static List<VerifyError> verify(ClassModel classModel, Consumer<String> logger) {
        var errors = new ArrayList<VerifyError>();
        verifyAttributes(classModel, errors);
        return errors;
    }

    private static void verifyAttributes(ClassfileElement cfe, List<VerifyError> errors) {
        if (cfe instanceof AttributedElement ae) {
            var attrNames = new HashSet<String>();
            for (var a : ae.attributes()) {
                if (!a.attributeMapper().allowMultiple() && !attrNames.add(a.attributeName())) {
                    errors.add(new VerifyError("Duplicate %s attribute in %s".formatted(a.attributeName(), toString(ae))));
                }
                verifyAttribute(ae, a, errors);
            }
        }
        if (cfe instanceof CompoundElement<?> comp) {
            for (var e : comp) verifyAttributes(e, errors);
        }
    }

    private static void verifyAttribute(AttributedElement ae, Attribute<?> a, List<VerifyError> errors) {
        int payLoad = ((BoundAttribute)a).payloadLen();
        if (payLoad != switch (a) {
            case BootstrapMethodsAttribute bma -> {
                int l = 2;
                for (var bm : bma.bootstrapMethods()) {
                    l += 4 + 2 * bm.arguments().size();
                }
                yield l;
            }
            case ConstantValueAttribute cva -> 2;
            case DeprecatedAttribute da -> 0;
            case EnclosingMethodAttribute ema -> 4;
            case ExceptionsAttribute ea -> 2 + 2 * ea.exceptions().size();
            case InnerClassesAttribute ica -> 2 + 8 * ica.classes().size();
            case MethodParametersAttribute mpa -> 1 + 4 * mpa.parameters().size();
            case NestHostAttribute nha -> 2;
            case NestMembersAttribute nma -> 2 + 2 * nma.nestMembers().size();
            case PermittedSubclassesAttribute psa -> 2 + 2 * psa.permittedSubclasses().size();
            case RecordAttribute ra -> {
                int l = 2;
                for (var rc : ra.components()) {
                    l += 6;
                    for (var rca : rc.attributes()) {
                        l += 6 + ((BoundAttribute)rca).payloadLen();
                    }
                }
                yield l;
            }
            case SignatureAttribute sa -> 2;
            case SourceFileAttribute sfa -> 2;
            case SyntheticAttribute sa -> 0;
            default -> payLoad;
        }) {
            errors.add(new VerifyError("Wrong %s attribute length in %s".formatted(a.attributeName(), toString(ae))));
        }

    }

    private static String toString(AttributedElement ae) {
        return switch (ae) {
            case ClassModel m -> "class";
            case CodeModel m -> "CodeAttribute of " + toString(m.parent().get());
            case FieldModel m -> "field " + m.fieldName().stringValue();
            case MethodModel m -> "method " + m.methodName().stringValue() + m.methodTypeSymbol().displayDescriptor();
            case RecordComponentInfo i -> "record component " + i.name().stringValue();
            default -> ae.toString();
        };
    }
}
