//******************************************************************************
// Copyright (c) Jamie Mansfield <https://jamiemansfield.me/>
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//******************************************************************************

package me.jamiemansfield.symphony.analysis;

import com.google.common.io.ByteStreams;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.jar.JarFile;

/**
 * An implementation of {@link Walker} for walking a jar file.
 */
public class JarWalker implements Walker {

    private final File jarFile;

    /**
     * Creates a new jar walker, from the given {@link Path}.
     *
     * @param jarPath The path of the jar
     */
    public JarWalker(final Path jarPath) {
        this(jarPath.toFile());
    }

    /**
     * Creates a new jar walker, from the given {@link File}.
     *
     * @param jarFile The file of the jar
     */
    public JarWalker(final File jarFile) {
        this.jarFile = jarFile;
    }

    @Override
    public void walk(final SourceSet sourceSet) {
        try (final JarFile jarFile = new JarFile(this.jarFile)) {
            jarFile.stream()
                    // Filter out directories
                    .filter(entry -> !entry.isDirectory())
                    // I only want to get classes
                    .filter(entry -> entry.getName().endsWith(".class"))
                    // Now to read the class
                    .forEach(entry -> {
                        try (final InputStream in = jarFile.getInputStream(entry)) {
                            final ClassReader reader = new ClassReader(ByteStreams.toByteArray(in));
                            final ClassNode node = new ClassNode();
                            reader.accept(node, 0);
                            sourceSet.add(node);
                        } catch (final IOException ex) {
                            System.err.println("Failed to get an input stream for " + entry.getName() + "!");
                            ex.printStackTrace(System.err);
                        }
                    });
        } catch (final IOException ex) {
            System.err.println("Failed to read the jar file!");
            ex.printStackTrace(System.err);
        }
    }

}
