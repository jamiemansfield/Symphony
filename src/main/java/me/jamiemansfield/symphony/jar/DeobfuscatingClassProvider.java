//******************************************************************************
// Copyright (c) Jamie Mansfield <https://jamiemansfield.me/>
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//******************************************************************************

package me.jamiemansfield.symphony.jar;

import org.cadixdev.bombe.asm.jar.ClassProvider;
import org.cadixdev.lorenz.MappingSet;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.commons.Remapper;

/**
 * A {@link ClassProvider} that fetches de-obfuscated bytecode.
 *
 * @author Jamie Mansfield
 * @since 0.1.0
 */
class DeobfuscatingClassProvider implements ClassProvider {

    private final ClassProvider obfProvider;
    private final MappingSet mappings;
    private final Remapper remapper;

    DeobfuscatingClassProvider(final ClassProvider obfProvider, final MappingSet mappings, final Remapper remapper) {
        this.obfProvider = obfProvider;
        this.mappings = mappings;
        this.remapper = remapper;
    }

    @Override
    public byte[] get(final String klass) {
        // Fetch the original (obfuscated) name and bytecode
        // note: #getFullDeobfuscatedName() is used, because the mapping set is reversed [deobf -> obf]
        final String obfName = this.mappings.reverse().getOrCreateClassMapping(klass)
                .getFullDeobfuscatedName();
        final byte[] obfuscated = this.obfProvider.get(obfName);
        if (obfuscated == null) return null;

        // Remap the class
        final ClassReader reader = new ClassReader(obfuscated);
        final ClassWriter writer = new ClassWriter(reader, 0);
        reader.accept(new LvtWipingClassRemapper(
                writer,
                this.remapper
        ), 0);
        return writer.toByteArray();
    }

}
