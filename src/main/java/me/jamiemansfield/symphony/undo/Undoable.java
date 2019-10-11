//******************************************************************************
// Copyright (c) Jamie Mansfield <https://jamiemansfield.me/>
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//******************************************************************************

package me.jamiemansfield.symphony.undo;

/**
 * An action that can be undone, and redone.
 *
 * @author Jamie Mansfield
 * @since 0.1.0
 */
public interface Undoable {

    /**
     * Gets the title of the undoable action.
     *
     * @return The title
     */
    String getTitle();

    /**
     * Applies the action.
     */
    void apply();

    /**
     * Un-applies the action, reverting to the original state.
     */
    void unapply();

}
