//******************************************************************************
// Copyright (c) Jamie Mansfield <https://jamiemansfield.me/>
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//******************************************************************************

package me.jamiemansfield.symphony.gui.util;

import me.jamiemansfield.symphony.util.PropertiesKey;
import me.jamiemansfield.symphony.util.StateHelper;

/**
 * A helper class for interacting with Symphony's display settings.
 *
 * @author Jamie Mansfield
 * @since 0.1.0
 */
public class DisplaySettings {

    private static final PropertiesKey<Boolean> FLATTEN_PACKAGES = PropertiesKey.bool("flatten_packages");
    private static final PropertiesKey<Boolean> COMPACT_MIDDLE_PACKAGES = PropertiesKey.bool("compact_middle_packages");
    private static final PropertiesKey<Boolean> SPLIT_CLASSES = PropertiesKey.bool("split_classes");

    public static boolean flattenPackages() {
        return StateHelper.DISPLAY.computeIfAbsent(FLATTEN_PACKAGES, () -> false);
    }

    public static void setFlattenPackages(final boolean value) {
        StateHelper.DISPLAY.set(FLATTEN_PACKAGES, value);
    }

    // TODO: Implement functionality
    public static boolean compactMiddlePackages() {
        return StateHelper.DISPLAY.computeIfAbsent(COMPACT_MIDDLE_PACKAGES, () -> true);
    }

    public static void setCompactMiddlePackages(final boolean value) {
        StateHelper.DISPLAY.set(COMPACT_MIDDLE_PACKAGES, value);
    }

    public static boolean splitClasses() {
        return StateHelper.DISPLAY.computeIfAbsent(SPLIT_CLASSES, () -> false);
    }

    public static void setSplitClasses(final boolean value) {
        StateHelper.DISPLAY.set(SPLIT_CLASSES, value);
    }

    private DisplaySettings() {
    }

}
