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
import javafx.scene.control.TextInputDialog;
import me.jamiemansfield.symphony.gui.Symphony;
import me.jamiemansfield.symphony.util.LocaleHelper;

/**
 * The Symphony 'Navigate' menu.
 *
 * @author Jamie Mansfield
 * @since 0.1.0
 */
public class NavigateMenu extends Menu {

    private final Symphony symphony;

    public final MenuItem klass;

    public NavigateMenu(final MainMenuBar mainMenuBar) {
        // Settings
        super(LocaleHelper.get("menu.navigate"));
        this.setMnemonicParsing(true);

        // Fields
        this.symphony = mainMenuBar.getSymphony();

        // Class
        {
            this.klass = new MenuItem(LocaleHelper.get("menu.navigate.class"));
            this.klass.setDisable(true);
            this.klass.addEventHandler(ActionEvent.ACTION, this::navigateToClass);
            this.getItems().add(klass);
        }
    }

    private void navigateToClass(final ActionEvent event) {
        final TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(LocaleHelper.get("menu.navigate.class.title"));
        dialog.setHeaderText(LocaleHelper.get("menu.navigate.class.title"));
        dialog.setContentText(LocaleHelper.get("menu.navigate.class.content"));

        dialog.showAndWait().ifPresent(klass -> {
            if (!this.symphony.getJar().hasClass(klass)) return;
            this.symphony.displayCodeTab(this.symphony.getJar().getMappings().getOrCreateTopLevelClassMapping(klass));
        });
    }

}
