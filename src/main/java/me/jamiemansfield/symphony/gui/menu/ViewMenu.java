//******************************************************************************
// Copyright (c) Jamie Mansfield <https://jamiemansfield.me/>
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//******************************************************************************

package me.jamiemansfield.symphony.gui.menu;

import javafx.event.ActionEvent;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;

/**
 * The Symphony 'View' menu.
 *
 * @author Jamie Mansfield
 * @since 0.1.0
 */
public class ViewMenu extends Menu {

    public ViewMenu(final MainMenuBar mainMenuBar) {
        // Settings
        super("_View");
        this.setMnemonicParsing(true);

        // Close all tabs
        {
            final MenuItem closeAllTabs = new MenuItem("Close all tabs");
            closeAllTabs.addEventHandler(ActionEvent.ACTION, event -> mainMenuBar.getSymphony().getTabs().getTabs().clear());
            this.getItems().add(closeAllTabs);
        }
    }

}
