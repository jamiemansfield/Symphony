//******************************************************************************
// Copyright (c) Jamie Mansfield <https://jamiemansfield.me/>
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//******************************************************************************

package me.jamiemansfield.symphony.decompiler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ServiceLoader;

/**
 * A manager for handling {@link Decompiler}s.
 *
 * @author Jamie Mansfield
 * @since 0.1.0
 */
public final class DecompilerManager {

    private static Decompiler DEFAULT;
    private static List<Decompiler> DECOMPILERS;

    /**
     * Gets a immutable-view of all the decompilers on the
     * classpath.
     *
     * @return The decompilers
     */
    public static List<Decompiler> getDecompilers() {
        if (DECOMPILERS == null) {
            DECOMPILERS = new ArrayList<>();

            for (final Decompiler decompiler : ServiceLoader.load(Decompiler.class)) {
                if (DEFAULT == null) DEFAULT = decompiler;
                DECOMPILERS.add(decompiler);
            }
        }
        return Collections.unmodifiableList(DECOMPILERS);
    }

    /**
     * Gets the default (first-loaded) decompiler.
     *
     * @return The default
     */
    public static Decompiler getDefault() {
        if (DEFAULT == null) getDecompilers();
        return DEFAULT;
    }

    private DecompilerManager() {
    }

}
