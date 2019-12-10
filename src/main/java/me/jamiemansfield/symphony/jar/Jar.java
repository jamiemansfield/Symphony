//******************************************************************************
// Copyright (c) Jamie Mansfield <https://jamiemansfield.me/>
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//******************************************************************************

package me.jamiemansfield.symphony.jar;

import static me.jamiemansfield.symphony.SharedConstants.CLASSLOADER_PROVIDER;
import static org.cadixdev.atlas.jar.JarVisitOption.IGNORE_MANIFESTS;
import static org.cadixdev.atlas.jar.JarVisitOption.IGNORE_RESOURCES;
import static org.cadixdev.atlas.jar.JarVisitOption.IGNORE_SERVICE_PROVIDER_CONFIGURATIONS;

import me.jamiemansfield.symphony.decompiler.Decompiler;
import me.jamiemansfield.symphony.decompiler.WrappedBytecode;
import me.jamiemansfield.symphony.util.Dirtyable;
import org.cadixdev.atlas.jar.JarFile;
import org.cadixdev.atlas.jar.JarPath;
import org.cadixdev.bombe.analysis.CachingInheritanceProvider;
import org.cadixdev.bombe.analysis.InheritanceProvider;
import org.cadixdev.bombe.asm.analysis.ClassProviderInheritanceProvider;
import org.cadixdev.bombe.asm.jar.ClassProvider;
import org.cadixdev.bombe.asm.jar.JarEntryRemappingTransformer;
import org.cadixdev.bombe.jar.JarClassEntry;
import org.cadixdev.lorenz.MappingSet;
import org.cadixdev.lorenz.asm.LorenzRemapper;
import org.cadixdev.lorenz.model.ClassMapping;
import org.cadixdev.lorenz.model.TopLevelClassMapping;
import org.cadixdev.survey.context.SurveyContext;
import org.cadixdev.survey.mapper.AbstractMapper;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.commons.Remapper;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

/**
 * A wrapper around {@link JarFile}.
 *
 * @author Jamie Mansfield
 * @since 0.1.0
 */
public class Jar implements Dirtyable, Closeable {

    // Mappings Related
    private final MappingSet mappings = new MappingSet();
    private boolean dirty = false;

    // Jar related
    private final JarFile jar;
    private final SurveyContext ctx;
    private final Set<JarPath> classes;
    private final ClassProvider obfProvider;
    private final ClassProvider deobfProvider;
    private final InheritanceProvider inheritanceProvider;
    private final Remapper remapper;

    public Jar(final Path path) throws IOException {
        this.jar = new JarFile(path);
        this.ctx = new JarContext(this);
        this.classes = this.jar.walk(IGNORE_MANIFESTS, IGNORE_SERVICE_PROVIDER_CONFIGURATIONS, IGNORE_RESOURCES)
                .collect(Collectors.toSet());
        this.obfProvider = klass -> {
            if (klass.startsWith("java/") || klass.startsWith("javax/")) {
                final byte[] contents = CLASSLOADER_PROVIDER.get(klass);
                if (contents != null) return contents;
            }
            return this.jar.get(klass);
        };
        this.inheritanceProvider =
                new CachingInheritanceProvider(new ClassProviderInheritanceProvider(this.obfProvider));
        this.remapper = new LorenzRemapper(this.mappings, this.inheritanceProvider);
        this.deobfProvider = new DeobfuscatingClassProvider(this.obfProvider, this.mappings, this.remapper);
    }

    /**
     * Gets the name of the Jar file.
     *
     * @return The name
     */
    public String getName() {
        return (this.dirty ? "*" : "") + this.jar.getName();
    }

    /**
     * Gets the mappings associated with the jar.
     *
     * @return The mappings
     */
    public MappingSet getMappings() {
        return this.mappings;
    }

    @Override
    public boolean isDirty() {
        return this.dirty;
    }

    @Override
    public void markDirty(final boolean dirty) {
        this.dirty = this.dirty || dirty;
    }

    @Override
    public void resetDirty() {
        this.dirty = false;
    }

    public Set<JarPath> classes() {
        return Collections.unmodifiableSet(this.classes);
    }

    public boolean hasClass(final String klass) {
        return this.classes.contains(new JarPath(klass + ".class"));
    }

    public void exportRemapped(final File exportPath) {
        try {
            this.jar.transform(exportPath.toPath(), new JarEntryRemappingTransformer(this.remapper));
        }
        catch (final IOException ex) {
            ex.printStackTrace();
        }
    }

    public String decompile(final Decompiler decompiler, final TopLevelClassMapping klass) {
        // Get the top-level class
        final byte[] deobfBytes = this.deobfProvider.get(klass.getFullObfuscatedName());
        if (deobfBytes == null) return "Well... this is embarrassing.";
        final WrappedBytecode rootKlass = new WrappedBytecode(
                klass.getFullDeobfuscatedName() + ".class",
                deobfBytes
        );

        // Get the inner classes
        final WrappedBytecode[] innerKlasses = this.classes.stream()
                .map(path -> path.getName().substring(0, path.getName().length() - ".class".length()))
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

    public <C, M extends AbstractMapper<C>> void runMapper(final BiFunction<SurveyContext, C, M> mapperConstructor,
                                                           final C config) {
        final M mapper = mapperConstructor.apply(this.ctx, config);
        this.classes.stream().map(this.jar::getClass).map(JarClassEntry::getContents).map(ClassReader::new).forEach(reader -> {
            reader.accept(mapper, 0);
        });
    }

    @Override
    public void close() throws IOException {
        this.jar.close();
    }

}
