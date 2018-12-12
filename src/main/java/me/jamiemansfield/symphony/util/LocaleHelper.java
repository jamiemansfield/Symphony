//******************************************************************************
// Copyright (c) Jamie Mansfield <https://jamiemansfield.me/>
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//******************************************************************************

package me.jamiemansfield.symphony.util;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * A helper class for working with strings that can be localised.
 *
 * @author Jamie Mansfield
 * @since 0.1.0
 */
public final class LocaleHelper {

    // Default to British English
    private static ResourceBundle BUNDLE = ResourceBundle.getBundle("strings.messages");

    /**
     * Gets the localised message for the given key.
     *
     * @param key The key
     * @return The localised message
     */
    public static String get(final String key) {
        if (BUNDLE.containsKey(key)) return BUNDLE.getString(key);
        throw new RuntimeException("Message could not be found for '" + key + "'!");
    }

    /**
     * Sets the current locale.
     *
     * @param locale The locale
     */
    public static void setLocale(final Locale locale) {
        BUNDLE = ResourceBundle.getBundle("strings.messages", locale);
    }

    private LocaleHelper() {
    }

}
