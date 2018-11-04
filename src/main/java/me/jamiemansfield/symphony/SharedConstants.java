//******************************************************************************
// Copyright (c) Jamie Mansfield <https://jamiemansfield.me/>
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//******************************************************************************

package me.jamiemansfield.symphony;

import org.jetbrains.java.decompiler.main.extern.IFernflowerPreferences;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * A class populated with constants used throughout Symphony.
 *
 * @author Jamie Mansfield
 * @since 0.1.0
 */
public final class SharedConstants {

    /**
     * The version of Symphony, that is running (will be {@code "dev"} in dev env).
     */
    public static final String VERSION = Objects.toString(
            SharedConstants.class.getPackage().getImplementationVersion(), "dev");

    public static final Map<String, Object> DECOMPILER_OPTTIONS = new HashMap<String, Object>(){
        {
            this.put(IFernflowerPreferences.DECOMPILE_GENERIC_SIGNATURES, "1");
            this.put(IFernflowerPreferences.ASCII_STRING_CHARACTERS, "1");
            this.put(IFernflowerPreferences.REMOVE_SYNTHETIC, "1");
            this.put(IFernflowerPreferences.USE_JAD_VARNAMING, "1");
            this.put(IFernflowerPreferences.INDENT_STRING, "    ");
        }
    };

    private SharedConstants() {
    }

}
