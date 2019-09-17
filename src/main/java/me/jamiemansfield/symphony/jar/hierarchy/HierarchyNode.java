//******************************************************************************
// Copyright (c) Jamie Mansfield <https://jamiemansfield.me/>
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//******************************************************************************

package me.jamiemansfield.symphony.jar.hierarchy;

/**
 * Represents a node within a hierarchy, essentially either a package
 * or a class.
 *
 * @see ClassHierarchyNode
 * @see PackageHierarchyNode
 *
 * @author Jamie Mansfield
 * @since 0.1.0
 */
public interface HierarchyNode {

    /**
     * Gets the fully-qualified name of the node.
     *
     * @return The fully-qualified name
     */
    String getName();

    /**
     * Gets the simple name of the node.
     *
     * @return The simple name
     */
    String getSimpleName();

}
