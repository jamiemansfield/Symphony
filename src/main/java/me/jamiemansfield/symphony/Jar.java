//******************************************************************************
// Copyright (c) Jamie Mansfield <https://jamiemansfield.me/>
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//******************************************************************************

package me.jamiemansfield.symphony;

import org.cadixdev.bombe.analysis.CachingInheritanceProvider;
import org.cadixdev.bombe.analysis.InheritanceProvider;
import org.cadixdev.bombe.asm.analysis.ClassProviderInheritanceProvider;
import org.cadixdev.bombe.asm.jar.ClassProvider;
import org.cadixdev.bombe.jar.AbstractJarEntry;
import org.cadixdev.bombe.jar.Jars;
import org.cadixdev.bombe.util.ByteStreams;
import org.cadixdev.lorenz.MappingSet;
import org.cadixdev.lorenz.model.ClassMapping;
import org.cadixdev.survey.remapper.SurveyRemapper;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.commons.ClassRemapper;
import org.objectweb.asm.commons.Remapper;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Stream;

/**
 * A wrapper around {@link JarFile}, implementing {@link ClassProvider},
 * with various other utilities.
 *
 * @author Jamie Mansfield
 * @since 0.1.0
 */
public class Jar implements ClassProvider, Closeable {

    private final JarFile jar;
    private final MappingSet mappings = MappingSet.create();
    private final InheritanceProvider inheritanceProvider =
            new CachingInheritanceProvider(new ClassProviderInheritanceProvider(this));
    private final Remapper remapper = new SurveyRemapper(this.mappings, this.inheritanceProvider);

    public Jar(final JarFile jar) {
        this.jar = jar;
    }

    /**
     * Gets the mappings associated with the jar.
     *
     * @return The mappings
     */
    public MappingSet getMappings() {
        return this.mappings;
    }

    /**
     * @see Jars#walk(JarFile)
     */
    public Stream<AbstractJarEntry> entries() {
        return Jars.walk(this.jar);
    }

    private byte[] getObfuscated(final String klass) {
        final String internalName = klass + ".class";

        final JarEntry entry = this.jar.getJarEntry(internalName);
        if (entry == null) return null;

        try (final InputStream in = this.jar.getInputStream(entry)) {
            final ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ByteStreams.copy(in, baos);
            return baos.toByteArray();
        }
        catch (final IOException ignored) {
            return null;
        }
    }

    @Override
    public byte[] get(final String klass) {
        // deobf -> obf
        final ClassMapping<?, ?> mapping = this.mappings.reverse().getOrCreateClassMapping(klass);

        // Get obfuscated bytecode
        final byte[] obfuscated = this.getObfuscated(mapping.getFullDeobfuscatedName());
        if (obfuscated == null) return null;

        // Remap the class
        final ClassReader reader = new ClassReader(obfuscated);
        final ClassWriter writer = new ClassWriter(reader, 0);
        reader.accept(new ClassRemapper(
                writer,
                this.remapper
        ), 0);
        return writer.toByteArray();
    }

    @Override
    public void close() throws IOException {
        this.jar.close();
    }

}
