//******************************************************************************
// Copyright (c) Jamie Mansfield <https://jamiemansfield.me/>
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//******************************************************************************

package me.jamiemansfield.symphony.jar.index;

import static org.objectweb.asm.Opcodes.V1_8;

import org.objectweb.asm.ClassVisitor;

public class ClassIndexer extends ClassVisitor {

    private IndexedClass.Builder builder;

    public ClassIndexer() {
        super(V1_8);
    }

    @Override
    public void visit(final int version, final int access, final String name, final String signature,
                      final String superName, final String[] interfaces) {
        this.builder = new IndexedClass.Builder(name);
        super.visit(version, access, name, signature, superName, interfaces);
    }

    @Override
    public void visitEnd() {
        super.visitEnd();
    }

}
