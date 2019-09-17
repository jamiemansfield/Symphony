//******************************************************************************
// Copyright (c) Jamie Mansfield <https://jamiemansfield.me/>
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//******************************************************************************

package me.jamiemansfield.symphony.jar.hierarchy;

import org.cadixdev.lorenz.MappingSet;
import org.cadixdev.lorenz.model.TopLevelClassMapping;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * The root element of a hierarchy.
 *
 * @author Jamie Mansfield
 * @since 0.1.0
 */
public class Hierarchy extends ParentHierarchyElement {

    public static void main(final String[] args) {
        final MappingSet mappings = MappingSet.create();
        mappings.getOrCreateTopLevelClassMapping("Test");
        mappings.getOrCreateTopLevelClassMapping("Test2");
        mappings.getOrCreateTopLevelClassMapping("com/m/Test3");
        mappings.getOrCreateTopLevelClassMapping("com/m/Test4");

        final Hierarchy hierarchy = Hierarchy.create(new HashSet<>(mappings.getTopLevelClassMappings()));

        System.out.println(hierarchy);
    }

    /**
     * Creates a hierarchy from a set of {@link TopLevelClassMapping classes}.
     *
     * @param classes The set of classes
     * @return The hierarchy
     */
    public static Hierarchy create(final Set<TopLevelClassMapping> classes) {
        final Hierarchy hierarchy = new Hierarchy();
        for (final TopLevelClassMapping klass : classes) {
            final String packageName = klass.getDeobfuscatedPackage();
            final String[] subPackages = packageName.split("/");

            ParentHierarchyElement parent = hierarchy;
            for (final String partial : subPackages) {
                if (partial.isEmpty()) {
                    parent = hierarchy;
                    continue;
                }

                final NodeKey<PackageHierarchyNode> key = NodeKey.pkg(partial);
                final Optional<PackageHierarchyNode> node = parent.get(key);
                if (node.isPresent()) {
                    parent = node.get();
                }
                else {
                    final PackageHierarchyNode packageNode = new PackageHierarchyNode(parent, partial);
                    parent.add(key, packageNode);
                    parent = packageNode;
                }
            }

            final NodeKey<ClassHierarchyNode> key = NodeKey.cls(klass.getSimpleDeobfuscatedName());
            parent.add(key, new ClassHierarchyNode(klass));
        }
        return hierarchy;
    }

}
