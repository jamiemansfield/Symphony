//******************************************************************************
// Copyright (c) Jamie Mansfield <https://jamiemansfield.me/>
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//******************************************************************************

package me.jamiemansfield.symphony;

import me.jamiemansfield.symphony.decompiler.JarBytecodeProvider;
import me.jamiemansfield.symphony.decompiler.NoopResultSaver;
import me.jamiemansfield.symphony.decompiler.SimpleFernflowerLogger;
import org.cadixdev.bombe.analysis.CachingInheritanceProvider;
import org.cadixdev.bombe.analysis.InheritanceProvider;
import org.cadixdev.bombe.asm.analysis.ClassProviderInheritanceProvider;
import org.cadixdev.bombe.asm.jar.ClassProvider;
import org.cadixdev.bombe.asm.jar.JarEntryRemappingTransformer;
import org.cadixdev.bombe.jar.AbstractJarEntry;
import org.cadixdev.bombe.jar.JarClassEntry;
import org.cadixdev.bombe.jar.Jars;
import org.cadixdev.bombe.util.ByteStreams;
import org.cadixdev.lorenz.MappingSet;
import org.cadixdev.lorenz.model.TopLevelClassMapping;
import org.cadixdev.survey.remapper.SurveyRemapper;
import org.jetbrains.java.decompiler.main.Fernflower;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.commons.ClassRemapper;
import org.objectweb.asm.commons.MethodRemapper;
import org.objectweb.asm.commons.Remapper;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
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

    public boolean hasClass(final String klass) {
        return this.jar.getEntry(klass + ".class") != null;
    }

    public void exportRemapped(final File exportPath) {
        try (final JarOutputStream jos = new JarOutputStream(new FileOutputStream(exportPath))) {
            Jars.transform(this.jar, jos, new JarEntryRemappingTransformer(this.remapper));
        }
        catch (final IOException ex) {
            ex.printStackTrace();
        }
    }

    public String decompile(final TopLevelClassMapping klass) {
        final byte[] deobfBytes = this.deobfuscate(klass.getFullObfuscatedName());
        if (deobfBytes == null) return "Well... this is embarrassing.";

        try {
            final Fernflower fernflower = new Fernflower(
                    new JarBytecodeProvider(this),
                    NoopResultSaver.INSTANCE,
                    SharedConstants.DECOMPILER_OPTTIONS,
                    SimpleFernflowerLogger.INSTANCE
            );
            // Provide immediate class
            fernflower.getStructContext().addData(
                    klass.getDeobfuscatedPackage(),
                    klass.getSimpleDeobfuscatedName() + ".class",
                    deobfBytes,
                    true
            );
            // Provide inner classes
            this.entries().filter(JarClassEntry.class::isInstance).map(JarClassEntry.class::cast)
                    .map(JarClassEntry::getName)
                    .map(name -> name.substring(0, name.length() - ".class".length()))
                    .filter(name -> name.startsWith(klass.getFullObfuscatedName() + "$"))
                    .map(this.mappings::getOrCreateClassMapping)
                    .forEach(mapping -> {
                        final byte[] innerDeobfBytes = this.deobfuscate(mapping.getFullObfuscatedName());
                        if (innerDeobfBytes == null) return;

                        try {
                            fernflower.getStructContext().addData(
                                    mapping.getDeobfuscatedPackage(),
                                    mapping.getSimpleDeobfuscatedName() + ".class",
                                    innerDeobfBytes,
                                    true
                            );
                        }
                        catch (final IOException ex) {
                            ex.printStackTrace();
                        }
                    });
            fernflower.decompileContext();
            return fernflower.getClassContent(fernflower.getStructContext().getClass(klass.getFullDeobfuscatedName()));
        }
        catch (final IOException ex) {
            ex.printStackTrace();
            return "Well... this is embarrassing.";
        }
    }

    public byte[] deobfuscate(final String klass) {
        // Get obfuscated bytecode
        final byte[] obfuscated = this.get(klass);
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

    @Override
    public byte[] get(final String klass) {
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
    public void close() throws IOException {
        this.jar.close();
    }

    private static class LvtWipingClassRemapper extends ClassRemapper {

        public LvtWipingClassRemapper(final ClassVisitor classVisitor, final Remapper remapper) {
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

}