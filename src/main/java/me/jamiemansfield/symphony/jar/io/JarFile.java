//******************************************************************************
// Copyright (c) Jamie Mansfield <https://jamiemansfield.me/>
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//******************************************************************************

package me.jamiemansfield.symphony.jar.io;

import me.jamiemansfield.symphony.jar.util.NIOHelper;
import org.cadixdev.bombe.jar.AbstractJarEntry;
import org.cadixdev.bombe.jar.JarClassEntry;
import org.cadixdev.bombe.jar.JarEntryTransformer;
import org.cadixdev.bombe.jar.JarManifestEntry;
import org.cadixdev.bombe.jar.JarResourceEntry;
import org.cadixdev.bombe.jar.JarServiceProviderConfigurationEntry;
import org.cadixdev.bombe.jar.ServiceProviderConfiguration;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.jar.Manifest;
import java.util.stream.Stream;

public class JarFile implements Closeable {

    private final Path path;
    private final FileSystem fs;
    private final Map<String, JarClassEntry> classes = new ConcurrentHashMap<>();

    public JarFile(final Path path) throws IOException {
        this.path = path;
        this.fs = NIOHelper.openZip(this.path, false);
        this.walk(JarVisitOption.IGNORE_MANIFESTS,
                JarVisitOption.IGNORE_SERVICE_PROVIDER_CONFIGURATIONS,
                JarVisitOption.IGNORE_RESOURCES)
                .map(JarClassEntry.class::cast)
                .forEach(klass -> {
                    final String name = klass.getName();
                    this.classes.put(name.substring(0, name.length() - ".class".length()), klass);
                });
    }

    public String getName() {
        return this.path.toString();
    }

    public JarClassEntry getClass(final String name) {
        return this.classes.get(name);
    }

    public Stream<JarClassEntry> classes() {
        return this.classes.values().stream();
    }

    public Stream<AbstractJarEntry> walk(final JarVisitOption... options) throws IOException {
        return Files.walk(this.fs.getPath("/")).filter(p -> !Files.isDirectory(p)).map(p -> {
            try {
                return _read(p, options);
            }
            catch (final IOException ex) {
                ex.printStackTrace();
                return null;
            }
        }).filter(Objects::nonNull);
    }

    public void transform(final Path export, final JarEntryTransformer... transformers) throws IOException {
        try (final FileSystem fs = NIOHelper.openZip(export, true)) {
            Stream.concat(this.walk(JarVisitOption.IGNORE_CLASSES), this.classes.values().stream())
                    .map(entry -> {
                        for (final JarEntryTransformer transformer : transformers) {
                            entry = entry.accept(transformer);
                        }
                        return entry;
                    })
                    .forEach(entry -> {
                        final Path outEntry = fs.getPath("/" + entry.getName());

                        try {
                            // Ensure parent directory exists
                            Files.createDirectories(outEntry.getParent());

                            // Write the result to the new jar
                            Files.write(outEntry, entry.getContents());
                            Files.setLastModifiedTime(outEntry, FileTime.fromMillis(entry.getTime()));
                        }
                        catch (final IOException ex) {
                            ex.printStackTrace();
                        }
                    });
        }
    }

    private static AbstractJarEntry _read(final Path entry, final JarVisitOption... options) throws IOException {
        final String name = entry.toString().substring(1); // Remove '/' prefix
        final long time = Files.getLastModifiedTime(entry).toMillis();

        if ("META-INF/MANIFEST.MF".equals(name)) {
            if (!_contains(JarVisitOption.IGNORE_MANIFESTS, options)) {
                try (final InputStream is = Files.newInputStream(entry)) {
                    return new JarManifestEntry(time, new Manifest(is));
                }
            }
        }
        else if (name.startsWith("META-INF/services/")) {
            if (!_contains(JarVisitOption.IGNORE_SERVICE_PROVIDER_CONFIGURATIONS, options)) {
                try (final InputStream is = Files.newInputStream(entry)) {
                    final String serviceName = name.substring("META-INF/services/".length());

                    final ServiceProviderConfiguration config = new ServiceProviderConfiguration(serviceName);
                    config.read(is);
                    return new JarServiceProviderConfigurationEntry(time, config);
                }
            }
        }
        else if (name.endsWith(".class")) {
            if (!_contains(JarVisitOption.IGNORE_CLASSES, options)) {
                return new JarClassEntry(name, time, Files.readAllBytes(entry));
            }
        }
        else {
            if (!_contains(JarVisitOption.IGNORE_RESOURCES, options)) {
                return new JarResourceEntry(name, time, Files.readAllBytes(entry));
            }
        }
        return null;
    }

    private static <T> boolean _contains(final T entry, final T[] in) {
        for (final T i : in) {
            if (i == entry) return true;
        }
        return false;
    }

    @Override
    public void close() throws IOException {
        this.fs.close();
    }

}
