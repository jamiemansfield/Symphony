//******************************************************************************
// Copyright (c) Jamie Mansfield <https://jamiemansfield.me/>
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//******************************************************************************

package me.jamiemansfield.symphony.gui.tree.view;

import javafx.scene.control.TreeCell;
import me.jamiemansfield.symphony.gui.tree.TreeElement;

/**
 * An extension of {@link TreeCell} allowing for the use of context
 * menus.
 *
 * @author Jamie Mansfield
 * @since 0.1.0
 */
public class SymphonyTreeCell extends TreeCell<TreeElement> {

    @Override
    protected void updateItem(final TreeElement item, final boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
            this.setText(null);
            this.setGraphic(null);
            this.setContextMenu(null);
            return;
        }

        // Make the cell look proper
        this.setText(item.toString());
        item.getGraphic().ifPresent(this::setGraphic);
        item.getContextMenu().ifPresent(this::setContextMenu);
    }

}
