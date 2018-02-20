//******************************************************************************
// Copyright (c) Jamie Mansfield <https://jamiemansfield.me/>
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//******************************************************************************

package me.jamiemansfield.symphony;

import java.util.Objects;

/**
 * A class populated with constants used throughout Symphony.
 */
public final class SharedConstants {

    /**
     * The version of Symphony, that is running (will be {@code "dev"} in dev env).
     */
    public static final String VERSION = Objects.toString(
            SymphonyMain.class.getPackage().getImplementationVersion(), "dev");

    private SharedConstants() {
    }

}
