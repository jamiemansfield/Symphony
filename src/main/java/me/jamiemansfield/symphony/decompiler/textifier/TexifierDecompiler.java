//******************************************************************************
// Copyright (c) Jamie Mansfield <https://jamiemansfield.me/>
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//******************************************************************************

package me.jamiemansfield.symphony.decompiler.textifier;

import me.jamiemansfield.symphony.decompiler.AbstractDecompiler;
import me.jamiemansfield.symphony.decompiler.Decompiler;
import me.jamiemansfield.symphony.decompiler.WrappedBytecode;
import org.cadixdev.bombe.asm.jar.ClassProvider;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.util.TraceClassVisitor;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;

/**
 * An implementation of {@link Decompiler} for ASM's Texifier.
 *
 * @author Jamie Mansfield
 * @since 0.1.0
 */
public class TexifierDecompiler extends AbstractDecompiler {

    private static final String ID = "textifier";
    private static final String NAME = "Textifier";

    @Override
    public String getId() {
        return ID;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public OutputType getOutputType() {
        return OutputType.OTHER;
    }

    @Override
    public String decompile(final ClassProvider classProvider, final WrappedBytecode klass, final WrappedBytecode... innerKlasses) {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final TraceClassVisitor cv = new TraceClassVisitor(null, new PrintWriter(baos));
        new ClassReader(klass.getBytecode()).accept(cv, 0);
        for (final WrappedBytecode bc : innerKlasses) {
            new ClassReader(bc.getBytecode()).accept(cv, 0);
        }
        return baos.toString();
    }

}
