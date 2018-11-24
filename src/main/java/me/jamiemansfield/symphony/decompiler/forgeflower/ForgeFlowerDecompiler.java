//******************************************************************************
// Copyright (c) Jamie Mansfield <https://jamiemansfield.me/>
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//******************************************************************************

package me.jamiemansfield.symphony.decompiler.forgeflower;

import me.jamiemansfield.symphony.decompiler.AbstractDecompiler;
import me.jamiemansfield.symphony.decompiler.IDecompiler;
import me.jamiemansfield.symphony.decompiler.WrappedBytecode;
import org.cadixdev.bombe.asm.jar.ClassProvider;
import org.jetbrains.java.decompiler.main.Fernflower;
import org.jetbrains.java.decompiler.main.extern.IFernflowerPreferences;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * An implementation of {@link IDecompiler} for ForgeFlower.
 *
 * @author Jamie Mansfield
 * @since 0.1.0
 */
public class ForgeFlowerDecompiler extends AbstractDecompiler {

    private static final String NAME =  "ForgeFlower";
    private static final Map<String, Object> DECOMPILER_OPTIONS = new HashMap<String, Object>(){
        {
            this.put(IFernflowerPreferences.DECOMPILE_GENERIC_SIGNATURES, "1");
            this.put(IFernflowerPreferences.ASCII_STRING_CHARACTERS, "1");
            this.put(IFernflowerPreferences.REMOVE_SYNTHETIC, "1");
            this.put(IFernflowerPreferences.USE_JAD_VARNAMING, "1");
            this.put(IFernflowerPreferences.INDENT_STRING, "    ");
        }
    };

    @Override
    public String decompile(final ClassProvider classProvider, final WrappedBytecode klass, final WrappedBytecode... innerKlasses) {
        // Create the Fernflower instance
        final Fernflower fernflower = new Fernflower(
                new ClassProviderBytecodeProvider(classProvider),
                NoopResultSaver.INSTANCE,
                DECOMPILER_OPTIONS,
                SimpleFernflowerLogger.INSTANCE
        );
        // Provide the top-level class
        provideClass(fernflower, klass);
        // Provide the inner classes
        for (final WrappedBytecode inner : innerKlasses) {
            provideClass(fernflower, inner);
        }
        // Decompile the class
        final String name = klass.getName().substring(0, klass.getName().length() - ".class".length());
        fernflower.decompileContext();
        return fernflower.getClassContent(fernflower.getStructContext().getClass(name));
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public OutputType getOutputType() {
        return OutputType.JAVA;
    }

    private static void provideClass(final Fernflower fernflower, final WrappedBytecode klass) {
        final String packageName = getPackageName(klass.getName());
        final String simpleName = getSimpleName(klass.getName());

        try {
            fernflower.getStructContext().addData(
                    packageName,
                    simpleName,
                    klass.getBytecode(),
                    true
            );
        }
        catch (final IOException ex) {
            ex.printStackTrace();
        }
    }

}
