//******************************************************************************
// Copyright (c) Jamie Mansfield <https://jamiemansfield.me/>
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//******************************************************************************

package me.jamiemansfield.symphony.gui.tree;

import javafx.scene.control.ContextMenu;

import java.util.Optional;

/**
 * The root tree element.
 *
 * @author Jamie Mansfield
 * @since 0.1.0
 */
public class RootElement implements TreeElement {

    private static final String NAME = "root";

    @Override
    public void activate() {
    }

    @Override
    public Optional<ContextMenu> getContextMenu() {
        return Optional.empty();
    }

    @Override
    public int compareTo(final TreeElement o) {
        return 0;
    }

    @Override
    public String toString() {
        return NAME;
    }

}
