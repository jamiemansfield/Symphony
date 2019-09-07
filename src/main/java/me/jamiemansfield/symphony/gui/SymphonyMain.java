//******************************************************************************
// Copyright (c) Jamie Mansfield <https://jamiemansfield.me/>
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//******************************************************************************

package me.jamiemansfield.symphony.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import me.jamiemansfield.symphony.SharedConstants;
import me.jamiemansfield.symphony.gui.menu.MainMenuBar;
import me.jamiemansfield.symphony.gui.tab.code.CodeTab;
import me.jamiemansfield.symphony.gui.tab.welcome.WelcomeTab;
import me.jamiemansfield.symphony.gui.tree.ClassesView;
import me.jamiemansfield.symphony.gui.util.MappingsHelper;
import me.jamiemansfield.symphony.jar.Jar;
import me.jamiemansfield.symphony.util.StateHelper;
import org.cadixdev.lorenz.model.TopLevelClassMapping;

import java.io.IOException;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * The Main-Class behind Symphony.
 *
 * @author Jamie Mansfield
 * @since 0.1.0
 */
public final class SymphonyMain extends Application {

    private static final String DEFAULT_TITLE = "Symphony v" + SharedConstants.VERSION;
    private static final int DEFAULT_WIDTH = 1280;
    private static final int DEFAULT_HEIGHT = 720;

    private Stage stage;
    private TabPane tabs;
    private MainMenuBar mainMenu;

    // Active jar
    private Jar jar;

    // Classes View
    private ClassesView classesView;

    @Override
    public void start(final Stage primaryStage) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (this.jar == null) return;
            try {
                this.jar.close();
            }
            catch (final IOException ex) {
                ex.printStackTrace();
            }
        }));

        // Set the primary stage
        this.stage = primaryStage;
        this.stage.setTitle(DEFAULT_TITLE);
        this.stage.setWidth(DEFAULT_WIDTH);
        this.stage.setHeight(DEFAULT_HEIGHT);

        // Root GUI container
        final BorderPane root = new BorderPane();

        // Main Menu
        this.mainMenu = new MainMenuBar(this);
        root.setTop(this.mainMenu);

        // Main Section
        final SplitPane main = new SplitPane();

        // Classes view
        final BorderPane classesView = new BorderPane();
        classesView.setMaxWidth(400);
        classesView.setMinWidth(250);
        classesView.setCenter(this.classesView = new ClassesView(this));
        main.getItems().add(classesView);

        // Tabs
        this.tabs = new TabPane();
        {
            this.tabs.getTabs().add(new WelcomeTab());
        }
        main.getItems().add(this.tabs);

        main.setDividerPositions(0.1);
        root.setCenter(main);

        // Set the scene
        final Scene scene = new Scene(root);
        scene.getStylesheets().add("css/highlighting.css");
        this.stage.setScene(scene);
        this.stage.show();
    }

    public Jar getJar() {
        return this.jar;
    }

    public void setJar(final Jar jar) {
        // Close all current tabs
        if (this.jar != null) {
            this.tabs.getTabs().removeAll(
                    this.tabs.getTabs().stream().filter(CodeTab.class::isInstance).collect(Collectors.toList())
            );
        }

        // Set the jar
        final boolean opening = jar != null;
        this.jar = jar;

        // Enable/Disable menu buttons as required
        {
            this.mainMenu.file.closeJar.setDisable(!opening);
            this.mainMenu.file.loadMappings.setDisable(!opening);
            this.mainMenu.file.saveMappings.setDisable(true);
            this.mainMenu.file.saveMappingsAs.setDisable(!opening);
            this.mainMenu.file.exportRemappedJar.setDisable(!opening);
            this.mainMenu.navigate.klass.setDisable(!opening);
        }
        MappingsHelper.LAST_LOCATION = null;

        // Correct the title, if needed
        this.updateTitle();

        // Refresh classes view
        this.refreshClasses();
    }

    public TabPane getTabs() {
        return this.tabs;
    }

    public Stage getStage() {
        return this.stage;
    }

    public void refreshClasses() {
        final Set<String> expanded = this.classesView.clear();
        this.classesView.initialise(this.jar, expanded);
    }

    public void update() {
        this.updateTitle();
        this.refreshClasses();
        this.tabs.getTabs().stream().filter(CodeTab.class::isInstance).map(CodeTab.class::cast)
                .forEach(CodeTab::update);
    }

    public void updateTitle() {
        if (this.jar != null) {
            this.stage.setTitle(DEFAULT_TITLE + " - " + this.jar.getName());
        }
        else {
            this.stage.setTitle(DEFAULT_TITLE);
        }
    }

    public void displayCodeTab(final TopLevelClassMapping klass) {
        this.tabs.getSelectionModel().select(this.tabs.getTabs().stream()
                .filter(CodeTab.class::isInstance)
                .map(CodeTab.class::cast)
                .filter(tab -> Objects.equals(tab.getKlass(), klass))
                .findFirst()
                .orElseGet(() -> {
                    final CodeTab tab = new CodeTab(this, klass);
                    this.tabs.getTabs().add(tab);
                    return tab;
                }));
    }

    public void markDirty(final boolean dirty) {
        this.jar.markDirty(dirty);
        // These calls are always followed by a #update() call, no need to
        // update the title here.
        if (this.jar.isDirty() && MappingsHelper.LAST_LOCATION != null) {
            this.mainMenu.file.saveMappings.setDisable(false);
        }
    }

    public void resetDirty() {
        this.jar.resetDirty();
        this.updateTitle();
        if (this.jar.isDirty() && MappingsHelper.LAST_LOCATION != null) {
            this.mainMenu.file.saveMappings.setDisable(true);
        }
    }

    public static void main(final String[] args) {
        Runtime.getRuntime().addShutdownHook(new Thread(StateHelper::save));

        launch(args);
    }

}
