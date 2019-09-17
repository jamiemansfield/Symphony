//******************************************************************************
// Copyright (c) Jamie Mansfield <https://jamiemansfield.me/>
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//******************************************************************************

package me.jamiemansfield.symphony.jar.hierarchy;

import java.util.Objects;

/**
 * A key used to represent a {@link HierarchyNode node} within a
 * hierarchy.
 *
 * @param <T> The type of node the key represents
 *
 * @author Jamie Mansfield
 * @since 0.1.0
 */
public class NodeKey<T extends HierarchyNode> {

    public static NodeKey<PackageHierarchyNode> pkg(final String name) {
        return new NodeKey<>(name, PackageHierarchyNode.class, true);
    }

    public static NodeKey<ClassHierarchyNode> cls(final String name) {
        return new NodeKey<>(name, ClassHierarchyNode.class, false);
    }

    private final String name;
    private final Class<T> klass;
    private final boolean terminal;

    public NodeKey(final String name, final Class<T> klass, final boolean terminal) {
        this.name = name;
        this.klass = klass;
        this.terminal = terminal;
    }

    public String getName() {
        return this.name;
    }

    public boolean isTerminal() {
        return this.terminal;
    }

    public T cast(final Object object) {
        return this.klass.cast(object);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof NodeKey)) return false;
        final NodeKey that = (NodeKey) obj;
        return Objects.equals(this.name, that.name) &&
                Objects.equals(this.terminal, that.terminal);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.name, this.terminal);
    }

}
