//******************************************************************************
// Copyright (c) Jamie Mansfield <https://jamiemansfield.me/>
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//******************************************************************************

package me.jamiemansfield.symphony.gui.menu.mapper;

import org.cadixdev.lorenz.util.Registry;

import java.util.Collection;
import java.util.ServiceLoader;

/**
 * A psuedo-enum of {@link MapperMenuItemProvider}s.
 *
 * @author Jamie Mansfield
 * @since 0.1.0
 */
public final class SymphonyMappers {

    /**
     * The registry of {@link MapperMenuItemProvider}s.
     */
    public static final Registry<MapperMenuItemProvider> REGISTRY = new Registry<>();

    static {
        // Populate the registry
        for (final MapperMenuItemProvider decompiler : ServiceLoader.load(MapperMenuItemProvider.class)) {
            REGISTRY.register(decompiler.toString(), decompiler);
        }
    }

    /**
     * @see Registry#byId(String)
     */
    public static MapperMenuItemProvider byId(final String id) {
        return REGISTRY.byId(id);
    }

    /**
     * @see Registry#values()
     */
    public static Collection<MapperMenuItemProvider> values() {
        return REGISTRY.values();
    }

    private SymphonyMappers() {
    }

}
