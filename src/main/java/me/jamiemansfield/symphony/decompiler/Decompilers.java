//******************************************************************************
// Copyright (c) Jamie Mansfield <https://jamiemansfield.me/>
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//******************************************************************************

package me.jamiemansfield.symphony.decompiler;

import me.jamiemansfield.symphony.util.PropertiesKey;
import me.jamiemansfield.symphony.util.StateHelper;
import org.cadixdev.lorenz.util.Registry;

import java.util.Collection;
import java.util.ServiceLoader;

/**
 * The psuedo-enum of decompilers implemented within Symphony,
 * and the registry.
 *
 * @author Jamie Mansfield
 * @since 0.1.0
 */
public class Decompilers {

    private static final PropertiesKey<Decompiler> DEFAULT = PropertiesKey.create(
            "default_decompiler", Decompiler::getId, Decompilers::byId);

    /**
     * The registry of {@link Decompiler}s.
     */
    public static final Registry<Decompiler> REGISTRY = new Registry<>();

    static {
        // Populate the registry
        for (final Decompiler decompiler : ServiceLoader.load(Decompiler.class)) {
            REGISTRY.register(decompiler.getId(), decompiler);
        }
    }

    /**
     * The CFR decompiler.
     */
    public static final Decompiler CFR = byId("cfr");

    /**
     * The ForgeFlower decompiler (a fork of Fernflower), the default decompiler
     * used by Symphony.
     */
    public static final Decompiler FORGEFLOWER = byId("forgeflower");

    /**
     * The Procyon decompiler.
     */
    public static final Decompiler PROCYON = byId("procyon");

    /**
     * The Textifier disassembler.
     */
    public static final Decompiler TEXTIFIER = byId("textifier");

    /**
     * @see Registry#byId(String)
     */
    public static Decompiler byId(final String id) {
        return REGISTRY.byId(id);
    }

    /**
     * @see Registry#values()
     */
    public static Collection<Decompiler> values() {
        return REGISTRY.values();
    }

    /**
     * Gets the decompiler currently used by default.
     *
     * @return The decompiler
     */
    public static Decompiler getDefault() {
        return StateHelper.GENERAL.get(DEFAULT).orElse(FORGEFLOWER);
    }

    /**
     * Sets the decompiler used by default.
     *
     * @param decompiler The decompiler
     */
    public static void setDefault(final Decompiler decompiler) {
        StateHelper.GENERAL.set(DEFAULT, decompiler);
    }

}
