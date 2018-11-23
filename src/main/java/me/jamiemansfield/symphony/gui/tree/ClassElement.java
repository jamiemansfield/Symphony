//******************************************************************************
// Copyright (c) Jamie Mansfield <https://jamiemansfield.me/>
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//******************************************************************************

package me.jamiemansfield.symphony.gui.tree;

import me.jamiemansfield.symphony.gui.SymphonyMain;
import org.cadixdev.lorenz.model.TopLevelClassMapping;

/**
 * A tree element for classes.
 *
 * @author Jamie Mansfield
 * @since 0.1.0
 */
public class ClassElement implements TreeElement {

    private final TopLevelClassMapping klass;
    private final SymphonyMain symphony;

    public ClassElement(final TopLevelClassMapping klass, final SymphonyMain symphony) {
        this.klass = klass;
        this.symphony = symphony;
    }

    @Override
    public void activate() {
        this.symphony.displayCodeTab(this.klass);
    }

    @Override
    public int compareTo(final TreeElement o) {
        if (o instanceof PackageElement) return 1;

        final String key0 = this.toString();
        final String key1 = o.toString();

        if (o instanceof ClassElement) {
            final ClassElement that = (ClassElement) o;

            final boolean root0 = this.klass.getDeobfuscatedPackage().isEmpty();
            final boolean root1 = that.klass.getDeobfuscatedPackage().isEmpty();

            if (root0 && root1) {
                if (key0.length() != key1.length()) {
                    return key0.length() - key1.length();
                }
                else {
                    return key0.compareTo(key1);
                }
            }
        }

        return key0.compareTo(key1);
    }

    @Override
    public String toString() {
        return this.klass.getSimpleDeobfuscatedName();
    }

}
