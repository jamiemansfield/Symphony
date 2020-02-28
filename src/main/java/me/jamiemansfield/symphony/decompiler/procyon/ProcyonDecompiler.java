//******************************************************************************
// Copyright (c) Jamie Mansfield <https://jamiemansfield.me/>
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//******************************************************************************

package me.jamiemansfield.symphony.decompiler.procyon;

import com.strobel.assembler.metadata.ClasspathTypeLoader;
import com.strobel.assembler.metadata.CompositeTypeLoader;
import com.strobel.decompiler.DecompilerSettings;
import com.strobel.decompiler.PlainTextOutput;
import me.jamiemansfield.symphony.source.SourceFileType;
import me.jamiemansfield.symphony.decompiler.AbstractDecompiler;
import me.jamiemansfield.symphony.decompiler.Decompiler;
import me.jamiemansfield.symphony.decompiler.WrappedBytecode;
import org.cadixdev.bombe.asm.jar.ClassProvider;

/**
 * An implementation of {@link Decompiler} for Procyon.
 *
 * @author Jamie Mansfield
 * @since 0.1.0
 */
public class ProcyonDecompiler extends AbstractDecompiler {

    private static final String ID = "procyon";
    private static final String NAME = "Procyon";

    @Override
    public String getId() {
        return ID;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public SourceFileType getOutputType() {
        return SourceFileType.JAVA;
    }

    @Override
    public String decompile(final ClassProvider classProvider, final WrappedBytecode klass, final WrappedBytecode... innerKlasses) {
        final String name = klass.getName().substring(0, klass.getName().length() - ".class".length());

        final DecompilerSettings settings = DecompilerSettings.javaDefaults();
        settings.setForceExplicitImports(true);
        settings.setTypeLoader(new CompositeTypeLoader(
                new ClassProviderTypeLoader(classProvider),
                new ClasspathTypeLoader()
        ));

        final PlainTextOutput output = new PlainTextOutput();
        com.strobel.decompiler.Decompiler.decompile(
                name,
                output,
                settings
        );

        return output.toString();
    }

}
