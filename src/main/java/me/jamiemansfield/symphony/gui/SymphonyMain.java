//******************************************************************************
// Copyright (c) Jamie Mansfield <https://jamiemansfield.me/>
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//******************************************************************************

package me.jamiemansfield.symphony.gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import me.jamiemansfield.symphony.SharedConstants;
import me.jamiemansfield.symphony.gui.control.WelcomeTab;

/**
 * The Main-Class behind Symphony.
 *
 * @author Jamie Mansfield
 * @since 0.1.0
 */
public final class SymphonyMain extends Application {

    private Stage stage;
    private TabPane tabs;

    @Override
    public void start(final Stage primaryStage) {
        // Set the primary stage
        this.stage = primaryStage;
        this.stage.setTitle("Symphony v" + SharedConstants.VERSION);
        this.stage.setWidth(1280);
        this.stage.setHeight(720);

        // Root GUI container
        final BorderPane root = new BorderPane();

        // Main Menu
        final MenuBar mainMenu = new MenuBar();
        {
            final Menu file = new Menu("File");
            {
                // Jar related
                {
                    final MenuItem openJar = new MenuItem("Open Jar...");
                    file.getItems().add(openJar);

                    final MenuItem closeJar = new MenuItem("Close Jar...");
                    file.getItems().add(closeJar);
                }
                file.getItems().add(new SeparatorMenuItem());
                // Mapping related
                {
                    final MenuItem loadMappings = new MenuItem("Load Mappings...");
                    file.getItems().add(loadMappings);

                    final MenuItem saveMappings = new MenuItem("Save Mappings");
                    file.getItems().add(saveMappings);

                    final MenuItem saveMappingsAs = new MenuItem("Save Mappings As...");
                    file.getItems().add(saveMappingsAs);
                }
                file.getItems().add(new SeparatorMenuItem());
                // Binary related
                {
                    final MenuItem exportRemappedJar = new MenuItem("Export Remapped Jar...");
                    file.getItems().add(exportRemappedJar);
                }
                file.getItems().add(new SeparatorMenuItem());
                // Program related
                {
                    final MenuItem close = new MenuItem("Quit");
                    close.addEventHandler(ActionEvent.ACTION, event -> Platform.exit());
                    file.getItems().add(close);
                }
            }
            mainMenu.getMenus().add(file);

            final Menu help = new Menu("Help");
            {
                {
                    final MenuItem welcomeTab = new MenuItem("Open Welcome Tab");
                    welcomeTab.addEventHandler(ActionEvent.ACTION, this::displayWelcomeTab);
                    help.getItems().add(welcomeTab);
                }
                help.getItems().add(new SeparatorMenuItem());
                {
                    final MenuItem about = new MenuItem("About Symphony");
                    help.getItems().add(about);
                }
            }
            mainMenu.getMenus().add(help);
        }
        root.setTop(mainMenu);

        // Tabs
        this.tabs = new TabPane();
        {
            this.tabs.getTabs().add(new WelcomeTab());
        }
        root.setCenter(this.tabs);

        // Set the scene
        final Scene scene = new Scene(root);
        this.stage.setScene(scene);
        this.stage.show();
    }

    private void displayWelcomeTab(final ActionEvent event) {
        this.tabs.getSelectionModel().select(this.tabs.getTabs().stream()
                .filter(WelcomeTab.class::isInstance)
                .findFirst()
                .orElseGet(() -> {
                    final WelcomeTab tab = new WelcomeTab();
                    this.tabs.getTabs().add(tab);
                    return tab;
                }));
    }

    public static void main(final String[] args) {
        launch(args);
    }

}
