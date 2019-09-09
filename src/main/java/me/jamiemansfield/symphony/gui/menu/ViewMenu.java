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
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.SeparatorMenuItem;
import me.jamiemansfield.symphony.gui.util.DisplaySettings;
import me.jamiemansfield.symphony.util.LocaleHelper;

/**
 * The Symphony 'View' menu.
 *
 * @author Jamie Mansfield
 * @since 0.1.0
 */
public class ViewMenu extends Menu {

    public ViewMenu(final MainMenuBar mainMenuBar) {
        // Settings
        super(LocaleHelper.get("menu.view"));
        this.setMnemonicParsing(true);

        // Close all tabs
        {
            final MenuItem closeAllTabs = new MenuItem(LocaleHelper.get("menu.view.close_all_tabs"));
            closeAllTabs.addEventHandler(ActionEvent.ACTION, event -> mainMenuBar.getSymphony().getTabs().getTabs().clear());
            this.getItems().add(closeAllTabs);
        }
        this.getItems().add(new SeparatorMenuItem());

        // Display Settings
        {
            final RadioMenuItem flattenPackages = new RadioMenuItem(LocaleHelper.get("menu.view.flatten_packages"));
            flattenPackages.setSelected(DisplaySettings.flattenPackages());
            flattenPackages.addEventHandler(ActionEvent.ACTION, event -> {
                DisplaySettings.setFlattenPackages(flattenPackages.isSelected());
                mainMenuBar.getSymphony().refreshClasses();
            });
            this.getItems().add(flattenPackages);

            final RadioMenuItem compactMiddlePackages = new RadioMenuItem(LocaleHelper.get("menu.view.compact_middle_packages"));
            compactMiddlePackages.setSelected(DisplaySettings.compactMiddlePackages());
            compactMiddlePackages.addEventHandler(ActionEvent.ACTION, event -> {
                DisplaySettings.setCompactMiddlePackages(compactMiddlePackages.isSelected());
                mainMenuBar.getSymphony().refreshClasses();
            });
            this.getItems().add(compactMiddlePackages);

            final RadioMenuItem splitClasses = new RadioMenuItem(LocaleHelper.get("menu.view.split_classes"));
            splitClasses.setSelected(DisplaySettings.splitClasses());
            splitClasses.addEventHandler(ActionEvent.ACTION, event -> {
                DisplaySettings.setSplitClasses(splitClasses.isSelected());
                mainMenuBar.getSymphony().updateClassesPane();
            });
            this.getItems().add(splitClasses);
        }
    }

}
