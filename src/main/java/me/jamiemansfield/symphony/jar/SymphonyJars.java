//******************************************************************************
// Copyright (c) Jamie Mansfield <https://jamiemansfield.me/>
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//******************************************************************************

package me.jamiemansfield.symphony.jar;

import org.cadixdev.bombe.jar.AbstractJarEntry;
import org.cadixdev.bombe.jar.JarClassEntry;
import org.cadixdev.bombe.jar.JarManifestEntry;
import org.cadixdev.bombe.jar.JarResourceEntry;
import org.cadixdev.bombe.jar.JarServiceProviderConfigurationEntry;
import org.cadixdev.bombe.jar.ServiceProviderConfiguration;
import org.cadixdev.bombe.util.ByteStreams;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.EnumSet;
import java.util.Objects;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import java.util.stream.Stream;

public final class SymphonyJars {

    public static Stream<AbstractJarEntry> walk(final JarFile jarFile, final EnumSet<Options> options) {
        return jarFile.stream().filter(entry -> !entry.isDirectory()).map(entry -> {
            final String name = entry.getName();
            try (final InputStream stream = jarFile.getInputStream(entry)) {
                final long time = entry.getTime();

                if (Objects.equals("META-INF/MANIFEST.MF", entry.getName())) {
                    return new JarManifestEntry(time, new Manifest(stream));
                }
                else if (entry.getName().startsWith("META-INF/services/")) {
                    final String serviceName = entry.getName().substring("META-INF/services/".length());

                    final ServiceProviderConfiguration config = new ServiceProviderConfiguration(serviceName);
                    config.read(stream);
                    return new JarServiceProviderConfigurationEntry(time, config);
                }

                final ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ByteStreams.copy(stream, baos);

                if (entry.getName().endsWith(".class")) {
                    return new JarClassEntry(name, time, baos.toByteArray());
                }
                else if (options.contains(Options.IGNORE_RESOURCES)) {
                    return new JarResourceEntry(name, time, baos.toByteArray());
                }
                else {
                    return null;
                }
            }
            catch (final IOException ignored) {
                // TODO: handle?
                return null;
            }
        });
    }

    enum Options {

        IGNORE_RESOURCES,
        ;

    }

}
