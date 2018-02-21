//******************************************************************************
// Copyright (c) Jamie Mansfield <https://jamiemansfield.me/>
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//******************************************************************************

package me.jamiemansfield.symphony.analysis;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.tree.ClassNode;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a container for a set of {@link ClassNode}s.
 */
public final class SourceSet {

    private final Map<String, ClassNode> classes = new HashMap<>();

    public SourceSet() {
    }

    /**
     * Adds the given {@link ClassNode} to the source set.
     *
     * @param node The class node
     */
    public void add(final ClassNode node) {
        this.classes.put(node.name, node);
    }

    /**
     * Gets all of the {@link ClassNode}s loaded in the source set.
     *
     * @return The classes
     */
    public Collection<ClassNode> getClasses() {
        return this.classes.values();
    }

    /**
     * Gets the {@link ClassNode} of the given name.
     *
     * @param className The class name
     * @return The class node, or null should one not exists of
     *         the given class name
     */
    public ClassNode get(final String className) {
        return this.classes.get(className);
    }

    /**
     * Accepts the given {@link ClassVisitor} on all {@link ClassNode}s
     * loaded by the source set.
     *
     * @param visitor The class visitor
     */
    public void accept(final ClassVisitor visitor) {
        this.classes.values()
                .forEach(node -> node.accept(visitor));
    }

}
