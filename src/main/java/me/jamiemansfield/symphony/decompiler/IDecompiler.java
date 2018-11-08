//******************************************************************************
// Copyright (c) Jamie Mansfield <https://jamiemansfield.me/>
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//******************************************************************************

package me.jamiemansfield.symphony.decompiler;

import org.cadixdev.bombe.asm.jar.ClassProvider;

/**
 * A representation of a decompiler, that can be used by Symphony.
 *
 * @author Jamie Mansfield
 * @since 0.1.0
 */
public interface IDecompiler {

    /**
     * Decompiles the given class, with any necessary inner
     * classes provided.
     *
     * @param classProvider The class provider to use
     * @param klass The top-level class to decompile
     * @param innerKlasses The inner (and anonymous) classes
     * @return The re-constructed source code
     */
    String decompile(final ClassProvider classProvider, final WrappedBytecode klass, final WrappedBytecode... innerKlasses);

    /**
     * Gets the name of the decompiler.
     *
     * @return The name
     */
    String getName();

}
