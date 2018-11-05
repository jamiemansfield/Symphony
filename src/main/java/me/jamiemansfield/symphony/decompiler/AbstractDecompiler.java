//******************************************************************************
// Copyright (c) Jamie Mansfield <https://jamiemansfield.me/>
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//******************************************************************************

package me.jamiemansfield.symphony.decompiler;

/**
 * An base implementation of {@link IDecompiler} with some
 * convenience methods.
 *
 * @author Jamie Mansfield
 * @since 0.1.0
 */
public abstract class AbstractDecompiler implements IDecompiler {

    /**
     * Gets the package that the class is contained within.
     *
     * @param klassName The name of the class
     * @return The package name
     */
    protected static String getPackageName(final String klassName) {
        final int index = klassName.lastIndexOf('/');
        if (index == -1) return "";
        return klassName.substring(0, index);
    }

    /**
     * Gets the simple name (without the package) of the class.
     *
     * @param klassName The name of the class
     * @return The simple name
     */
    protected static String getSimpleName(final String klassName) {
        final int index = klassName.lastIndexOf('/');
        if (index == -1) return klassName;
        return klassName.substring(index + 1);
    }

}
