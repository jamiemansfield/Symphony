//******************************************************************************
// Copyright (c) Jamie Mansfield <https://jamiemansfield.me/>
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//******************************************************************************

package me.jamiemansfield.symphony.jar.hierarchy;

import org.cadixdev.lorenz.model.TopLevelClassMapping;

import java.util.Set;

/**
 * The root element of a hierarchy.
 *
 * @author Jamie Mansfield
 * @since 0.1.0
 */
public class Hierarchy extends ParentHierarchyElement {

    /**
     * Creates a hierarchy from a set of {@link TopLevelClassMapping classes}.
     *
     * @param classes The set of classes
     * @return The hierarchy
     */
    public static Hierarchy create(final Set<TopLevelClassMapping> classes) {
        final Hierarchy hierarchy = new Hierarchy();
        for (final TopLevelClassMapping klass : classes) {
            hierarchy.getPackage(klass.getDeobfuscatedPackage())
                    .add(NodeKey.cls(klass.getDeobfuscatedName()), new ClassHierarchyNode(klass));
        }
        return hierarchy;
    }

    private ParentHierarchyElement getPackage(final String name) {
        if (name.isEmpty()) return this;

        final NodeKey<PackageHierarchyNode> key = NodeKey.pkg(name);
        return this.get(key).orElseGet(() -> {
            final int index = name.lastIndexOf('/');
            final String parent = index == -1 ? "" : name.substring(0, index);
            final PackageHierarchyNode packageNode = new PackageHierarchyNode(name);
            this.getPackage(parent)
                    .add(NodeKey.pkg(name), packageNode);
            return packageNode;
        });
    }

}
