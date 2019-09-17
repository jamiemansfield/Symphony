//******************************************************************************
// Copyright (c) Jamie Mansfield <https://jamiemansfield.me/>
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//******************************************************************************

package me.jamiemansfield.symphony.jar.hierarchy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Represents an element of a hierarchy that parents other
 * {@link HierarchyNode nodes}.
 *
 * @see Hierarchy
 * @see PackageHierarchyNode
 *
 * @author Jamie Mansfield
 * @since 0.1.0
 */
public abstract class ParentHierarchyElement implements Iterable<HierarchyNode> {

    protected final Map<NodeKey, HierarchyNode> children = new HashMap<>();

    public List<HierarchyNode> getChildren() {
        return Collections.unmodifiableList(new ArrayList<>(this.children.values()));
    }

    /**
     * Gets a child {@link HierarchyNode node}, with the given
     * {@link NodeKey key}.
     *
     * @param key The key
     * @param <T> The type of the node
     * @return The node, wrapped in an {@link Optional}
     */
    public <T extends HierarchyNode> Optional<T> get(final NodeKey<T> key) {
        // IMPORTANT: getOrDefault will not write to the map!
        return Optional.ofNullable(key.cast(this.children.getOrDefault(key, null)));
    }

    /**
     * Adds a {@link HierarchyNode node} to this parent, with the given
     * {@link NodeKey key}.
     *
     * @param key The key
     * @param node The node
     * @param <T> The type of the node
     */
    public <T extends HierarchyNode> void add(final NodeKey<T> key, final T node) {
        // TODO: we should probably do some verification here
        this.children.put(key, node);
    }

    @Override
    public Iterator<HierarchyNode> iterator() {
        // We use this.children directly, so that the iterator can be used
        // as an iterator.
        return this.children.values().iterator();
    }

}
