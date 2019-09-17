//******************************************************************************
// Copyright (c) Jamie Mansfield <https://jamiemansfield.me/>
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//******************************************************************************

package me.jamiemansfield.symphony.jar.hierarchy;

/**
 * Represents a package in a hierarchy.
 *
 * @author Jamie Mansfield
 * @since 0.1.0
 */
public class PackageHierarchyNode extends ParentHierarchyElement implements HierarchyNode {

    private final String name;

    public PackageHierarchyNode(final String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getSimpleName() {
        final int index = this.name.lastIndexOf('/');
        return index == -1 ? this.name : this.name.substring(index);
    }

}
