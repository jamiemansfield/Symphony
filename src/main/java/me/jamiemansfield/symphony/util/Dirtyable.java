//******************************************************************************
// Copyright (c) Jamie Mansfield <https://jamiemansfield.me/>
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//******************************************************************************

package me.jamiemansfield.symphony.util;

/**
 * Marks an object as having a dirty (modified) state.
 *
 * @author Jamie Mansfield
 * @since 0.1.0
 */
// TODO: Move to Lorenz eventually
public interface Dirtyable {

    /**
     * Establishes whether the object has a dirty state.
     *
     * @return {@code true} if the object is dirty;
     *         {@code false} otherwise
     */
    boolean isDirty();

    /**
     * Updates the dirty state of the object, if {@code dirty}
     * is {@code true}.
     *
     * @param dirty {@code true} to update the dirty state;
     *              {@code false} to leave unaffected
     */
    void markDirty(final boolean dirty);

    /**
     * Resets the dirty state of the object, reverting to {@code false}.
     */
    void resetDirty();

}
