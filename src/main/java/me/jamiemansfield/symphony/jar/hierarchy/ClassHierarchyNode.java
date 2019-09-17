//******************************************************************************
// Copyright (c) Jamie Mansfield <https://jamiemansfield.me/>
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//******************************************************************************

package me.jamiemansfield.symphony.jar.hierarchy;

import org.cadixdev.lorenz.model.TopLevelClassMapping;

/**
 * A {@link HierarchyNode hierarchy node} to represent a class.
 *
 * @author Jamie Mansfield
 * @since 0.1.0
 */
public class ClassHierarchyNode implements HierarchyNode {

    private final TopLevelClassMapping klass;

    public ClassHierarchyNode(final TopLevelClassMapping klass) {
        this.klass = klass;
    }

    @Override
    public String getName() {
        return this.klass.getFullDeobfuscatedName();
    }

    @Override
    public String getSimpleName() {
        return this.klass.getDeobfuscatedName();
    }

}
