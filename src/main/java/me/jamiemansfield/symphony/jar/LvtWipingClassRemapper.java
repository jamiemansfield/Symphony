//******************************************************************************
// Copyright (c) Jamie Mansfield <https://jamiemansfield.me/>
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//******************************************************************************

package me.jamiemansfield.symphony.jar;

import org.objectweb.asm.Attribute;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.commons.ClassRemapper;
import org.objectweb.asm.commons.MethodRemapper;
import org.objectweb.asm.commons.Remapper;

import java.util.Objects;

/**
 * A {@link ClassRemapper} for stripping methods of their local
 * variable tables.
 *
 * @author Jamie Mansfield
 * @since 0.1.0
 */
class LvtWipingClassRemapper extends ClassRemapper {

    LvtWipingClassRemapper(final ClassVisitor classVisitor, final Remapper remapper) {
        super(classVisitor, remapper);
    }

    @Override
    protected MethodVisitor createMethodRemapper(final MethodVisitor methodVisitor) {
        return new MethodRemapper(methodVisitor, this.remapper) {
            @Override
            public void visitAttribute(final Attribute attribute) {
                if (Objects.equals("LocalVariableTable", attribute.type)) return;
                super.visitAttribute(attribute);
            }

            @Override
            public void visitLocalVariable(final String name, final String descriptor, final String signature, final Label start, final Label end, final int index) {
            }
        };
    }

}
