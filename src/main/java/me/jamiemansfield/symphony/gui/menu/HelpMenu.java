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
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import me.jamiemansfield.symphony.gui.Symphony;
import me.jamiemansfield.symphony.gui.concurrent.TaskManager;
import me.jamiemansfield.symphony.gui.tab.welcome.WelcomeTab;
import me.jamiemansfield.symphony.gui.util.AboutHelper;
import me.jamiemansfield.symphony.util.LocaleHelper;

/**
 * The Symphony 'Help' menu.
 *
 * @author Jamie Mansfield
 * @since 0.1.0
 */
public class HelpMenu extends Menu {

    private final Symphony symphony;

    public HelpMenu(final MainMenuBar mainMenuBar) {
        // Settings
        super(LocaleHelper.get("menu.help"));
        this.setMnemonicParsing(true);

        // Fields
        this.symphony = mainMenuBar.getSymphony();

        // Items
        {
            final MenuItem welcomeTab = new MenuItem(LocaleHelper.get("menu.help.open_welcome_tab"));
            welcomeTab.addEventHandler(ActionEvent.ACTION, this::displayWelcomeTab);
            this.getItems().add(welcomeTab);
        }

        {
            final MenuItem tasksWindow = new MenuItem(LocaleHelper.get("menu.help.open_tasks_window"));
            tasksWindow.addEventHandler(ActionEvent.ACTION, event -> {
                TaskManager.INSTANCE.display();
            });
            this.getItems().add(tasksWindow);
        }
        this.getItems().add(new SeparatorMenuItem());

        {
            final MenuItem about = new MenuItem(LocaleHelper.get("menu.help.about"));
            about.addEventHandler(ActionEvent.ACTION, event -> AboutHelper.display());
            about.setAccelerator(new KeyCodeCombination(KeyCode.F1));
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
