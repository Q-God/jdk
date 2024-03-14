/*
 * Copyright (c) 2024, Arm Limited. All rights reserved.
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

#ifndef _VECT_MATH_H_
#define _VECT_MATH_H_

#ifndef __has_attribute
#define __has_attribute(x) 0
#endif

#ifndef VMATH_EXPORT
  #if (defined(__GNUC__) && (__GNUC__ >= 4)) || __has_attribute(visibility)
    #define VMATH_EXPORT __attribute__((visibility("default")))
  #else
    #define VMATH_EXPORT
  #endif
#endif /* VMATH_EXPORT */

#endif /* _VECT_MATH_H_ */
