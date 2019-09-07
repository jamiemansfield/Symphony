//******************************************************************************
// Copyright (c) Jamie Mansfield <https://jamiemansfield.me/>
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//******************************************************************************

package me.jamiemansfield.symphony.jar;

import me.jamiemansfield.symphony.decompiler.Decompiler;
import me.jamiemansfield.symphony.decompiler.WrappedBytecode;
import org.cadixdev.bombe.analysis.CachingInheritanceProvider;
import org.cadixdev.bombe.analysis.InheritanceProvider;
import org.cadixdev.bombe.asm.analysis.ClassProviderInheritanceProvider;
import org.cadixdev.bombe.asm.jar.ClassProvider;
import org.cadixdev.bombe.asm.jar.JarEntryRemappingTransformer;
import org.cadixdev.bombe.asm.jar.JarFileClassProvider;
import org.cadixdev.bombe.jar.AbstractJarEntry;
import org.cadixdev.bombe.jar.JarClassEntry;
import org.cadixdev.bombe.jar.Jars;
import org.cadixdev.lorenz.MappingSet;
import org.cadixdev.lorenz.asm.LorenzRemapper;
import org.cadixdev.lorenz.model.ClassMapping;
import org.cadixdev.lorenz.model.TopLevelClassMapping;
import org.objectweb.asm.commons.Remapper;

import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A wrapper around {@link JarFile}.
 *
 * @author Jamie Mansfield
 * @since 0.1.0
 */
public class Jar implements Closeable {

    // Mappings Related
    private final MappingSet mappings = MappingSet.create();
    private boolean dirty = false;

    // Jar related
    private final JarFile jar;
    private final Set<String> classes;
    private final ClassProvider obfProvider;
    private final ClassProvider deobfProvider;
    private final InheritanceProvider inheritanceProvider;
    private final Remapper remapper;

    public Jar(final JarFile jar) {
        this.jar = jar;
        this.classes = this.entries()
                .filter(JarClassEntry.class::isInstance)
                .map(JarClassEntry.class::cast)
                .map(JarClassEntry::getName)
                .map(name -> name.substring(0, name.length() - ".class".length()))
                .collect(Collectors.toSet());
        this.obfProvider = new JarFileClassProvider(this.jar);
        this.inheritanceProvider =
                new CachingInheritanceProvider(new ClassProviderInheritanceProvider(this.obfProvider));
        this.remapper = new LorenzRemapper(this.mappings, this.inheritanceProvider);
        this.deobfProvider = new DeobfuscatingClassProvider(this.obfProvider, this.mappings, this.remapper);
    }

    /**
     * Gets the mappings associated with the jar.
     *
     * @return The mappings
     */
    public MappingSet getMappings() {
        return this.mappings;
    }

    public boolean isDirty() {
        return this.dirty;
    }

    public void markDirty(final boolean dirty) {
        this.dirty = this.dirty || dirty;
    }

    public void resetDirty() {
        this.dirty = false;
    }

    public Set<String> classes() {
        return Collections.unmodifiableSet(this.classes);
    }

    private Stream<AbstractJarEntry> entries() {
        return SymphonyJars.walk(this.jar, EnumSet.of(SymphonyJars.Options.IGNORE_RESOURCES));
    }

    /**
     * @see JarFile#getName()
     */
    public String getName() {
        return (this.dirty ? "*" : "") + this.jar.getName();
    }

    /**
     * Gets the {@link ClassProvider} for querying obfuscated bytecode.
     *
     * @return The obfuscated class provider
     */
    public ClassProvider obfProvider() {
        return this.obfProvider;
    }

    /**
     * Gets the {@link ClassProvider} for querying de-obfuscated bytecode.
     *
     * @return The de-obfuscated class provider
     */
    public ClassProvider deobfProvider() {
        return this.deobfProvider;
    }

    public InheritanceProvider getInheritanceProvider() {
        return this.inheritanceProvider;
    }

    public boolean hasClass(final String klass) {
        return this.classes.contains(klass);
    }

    public void exportRemapped(final File exportPath) {
        synchronized (this.jar) {
            try (final JarOutputStream jos = new JarOutputStream(new FileOutputStream(exportPath))) {
                Jars.transform(this.jar, jos, new JarEntryRemappingTransformer(this.remapper));
            }
            catch (final IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public synchronized String decompile(final Decompiler decompiler, final TopLevelClassMapping klass) {
        // Get the top-level class
        final byte[] deobfBytes = this.deobfProvider.get(klass.getFullObfuscatedName());
        if (deobfBytes == null) return "Well... this is embarrassing.";
        final WrappedBytecode rootKlass = new WrappedBytecode(
                klass.getFullDeobfuscatedName() + ".class",
                deobfBytes
        );

        // Get the inner classes
        final WrappedBytecode[] innerKlasses = this.classes.stream()
                .filter(name -> name.startsWith(klass.getFullObfuscatedName() + '$'))
                .map(name -> {
                    final byte[] innerDeobfBytes = this.deobfProvider.get(name);
                    if (innerDeobfBytes == null) return null;

                    final ClassMapping<?, ?> innerKlass = this.mappings.getOrCreateClassMapping(name);
                    return new WrappedBytecode(
                            innerKlass.getFullDeobfuscatedName() + ".class",
                            innerDeobfBytes
                    );
                })
                .toArray(WrappedBytecode[]::new);

        // Decompile
        return decompiler.decompile(this.deobfProvider, rootKlass, innerKlasses);
    }

    @Override
    public void close() throws IOException {
        this.jar.close();
    }

}
