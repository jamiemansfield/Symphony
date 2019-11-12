//******************************************************************************
// Copyright (c) Jamie Mansfield <https://jamiemansfield.me/>
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//******************************************************************************

package me.jamiemansfield.symphony.util;

/**
 * Helper for working with class names.
 *
 * @author Jamie Mansfield
 * @since 0.1.0
 */
public final class ClassHelper {

    /**
     * Gets the package from a qualified class name.
     *
     * @param klass The qualified class name
     * @return The package name, or {@code ""} in the lack thereof
     */
    public static String getPackageName(final String klass) {
        return klass.lastIndexOf('/') != -1 ?
                klass.substring(0, klass.lastIndexOf('/')) :
                "";
    }

    /**
     * Gets the class name from a qualified class name.
     *
     * @param klass The qualified class name
     * @return The class name
     */
    public static String getClassName(final String klass) {
        return klass.substring(klass.lastIndexOf('/') + 1);
    }

    private ClassHelper() {
    }

}
