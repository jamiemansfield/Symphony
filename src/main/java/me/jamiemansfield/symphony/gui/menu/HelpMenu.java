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
import javafx.scene.control.SeparatorMenuItem;
import me.jamiemansfield.symphony.gui.SymphonyMain;
import me.jamiemansfield.symphony.gui.concurrent.TaskManager;
import me.jamiemansfield.symphony.gui.tab.welcome.WelcomeTab;
import me.jamiemansfield.symphony.gui.util.AboutHelper;

/**
 * The Symphony 'Help' menu.
 *
 * @author Jamie Mansfield
 * @since 0.1.0
 */
public class HelpMenu extends Menu {

    private final SymphonyMain symphony;

    public HelpMenu(final MainMenuBar mainMenuBar) {
        // Settings
        super("_Help");
        this.setMnemonicParsing(true);

        // Fields
        this.symphony = mainMenuBar.getSymphony();

        // Items
        {
            final MenuItem welcomeTab = new MenuItem("Open Welcome Tab");
            welcomeTab.addEventHandler(ActionEvent.ACTION, this::displayWelcomeTab);
            this.getItems().add(welcomeTab);
        }

        {
            final MenuItem tasksWindow = new MenuItem("Open Tasks Window");
            tasksWindow.addEventHandler(ActionEvent.ACTION, event -> {
                TaskManager.INSTANCE.display();
            });
            this.getItems().add(tasksWindow);
        }
        this.getItems().add(new SeparatorMenuItem());

        {
            final MenuItem about = new MenuItem("About Symphony");
            about.addEventHandler(ActionEvent.ACTION, event -> AboutHelper.display());
            this.getItems().add(about);
        }
    }

    private void displayWelcomeTab(final ActionEvent event) {
        this.symphony.getTabs().getSelectionModel().select(this.symphony.getTabs().getTabs().stream()
                .filter(WelcomeTab.class::isInstance)
                .findFirst()
                .orElseGet(() -> {
                    final WelcomeTab tab = new WelcomeTab();
                    this.symphony.getTabs().getTabs().add(tab);
                    return tab;
                }));
    }

}
