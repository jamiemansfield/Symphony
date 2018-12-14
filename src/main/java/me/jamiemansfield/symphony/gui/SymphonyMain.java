//******************************************************************************
// Copyright (c) Jamie Mansfield <https://jamiemansfield.me/>
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//******************************************************************************

package me.jamiemansfield.symphony.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TabPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import me.jamiemansfield.symphony.SharedConstants;
import me.jamiemansfield.symphony.gui.menu.MainMenuBar;
import me.jamiemansfield.symphony.gui.tab.code.CodeTab;
import me.jamiemansfield.symphony.gui.tab.welcome.WelcomeTab;
import me.jamiemansfield.symphony.gui.tree.ClassElement;
import me.jamiemansfield.symphony.gui.tree.PackageElement;
import me.jamiemansfield.symphony.gui.tree.RootElement;
import me.jamiemansfield.symphony.gui.tree.SymphonyTreeCell;
import me.jamiemansfield.symphony.gui.tree.TreeElement;
import me.jamiemansfield.symphony.jar.Jar;
import me.jamiemansfield.symphony.util.StateHelper;
import org.cadixdev.bombe.jar.JarClassEntry;
import org.cadixdev.lorenz.model.TopLevelClassMapping;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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
    private TreeItem<TreeElement> treeRoot;

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
        // TODO: Search
        {
            final TreeView<TreeElement> treeView = new TreeView<>();
            treeView.setShowRoot(false);
            treeView.setCellFactory(view -> new SymphonyTreeCell());
            treeView.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2) {
                    final TreeItem<TreeElement> item = treeView.getSelectionModel().getSelectedItems().get(0);
                    if (item == null) return;
                    item.getValue().activate();
                }
            });
            this.treeRoot = new TreeItem<>(new RootElement());
            this.treeRoot.setExpanded(true);
            treeView.setRoot(this.treeRoot);

            final ScrollPane scrollPane = new ScrollPane(treeView);
            scrollPane.setFitToWidth(true);
            scrollPane.setFitToHeight(true);

            classesView.setCenter(scrollPane);
        }
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
            // this.mainMenu.file.saveMappings.setDisable(!opening); // TODO: implement
            this.mainMenu.file.saveMappingsAs.setDisable(!opening);
            this.mainMenu.file.exportRemappedJar.setDisable(!opening);
            this.mainMenu.navigate.klass.setDisable(!opening);
        }

        // Correct the title, if needed
        if (opening) {
            this.stage.setTitle(DEFAULT_TITLE + " - " + jar.getName());
        }
        else {
            this.stage.setTitle(DEFAULT_TITLE);
        }

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
        final List<String> expanded = this.getExpandedPackages(new ArrayList<>(), this.treeRoot);
        this.treeRoot.getChildren().clear();
        if (this.jar == null) return;

        final Map<String, TreeItem<TreeElement>> packageCache = new HashMap<>();

        this.jar.entries()
                .filter(JarClassEntry.class::isInstance).map(JarClassEntry.class::cast)
                .filter(entry -> !entry.getSimpleName().contains("$"))
                .forEach(entry -> {
            final String klassName = entry.getName().substring(0, entry.getName().length() - ".class".length());
            final TopLevelClassMapping klass = this.jar.getMappings().getOrCreateTopLevelClassMapping(klassName);

            this.getPackageItem(packageCache, klass.getDeobfuscatedPackage()).getChildren()
                    .add(new TreeItem<>(new ClassElement(this, klass)));
        });

        // sort
        packageCache.values().forEach(item -> {
            item.getChildren().setAll(item.getChildren().sorted(Comparator.comparing(TreeItem::getValue)));
        });
        this.treeRoot.getChildren().setAll(this.treeRoot.getChildren().sorted(Comparator.comparing(TreeItem::getValue)));

        // reopen packages
        expanded.forEach(pkg -> {
            final TreeItem<TreeElement> packageItem = packageCache.get(pkg);
            if (packageItem == null) return;
            packageItem.setExpanded(true);
        });
    }

    private TreeItem<TreeElement> getPackageItem(final Map<String, TreeItem<TreeElement>> cache, final String packageName) {
        if (packageName.isEmpty()) return this.treeRoot;
        if (cache.containsKey(packageName)) return cache.get(packageName);

        final TreeItem<TreeElement> parent;
        if (packageName.lastIndexOf('/') != -1) {
            parent = this.getPackageItem(cache, packageName.substring(0, packageName.lastIndexOf('/')));
        }
        else {
            parent = this.treeRoot;
        }
        final TreeItem<TreeElement> packageItem = new TreeItem<>(new PackageElement(this, packageName));
        parent.getChildren().add(packageItem);
        cache.put(packageName, packageItem);
        return packageItem;
    }

    private List<String> getExpandedPackages(final List<String> packages, final TreeItem<TreeElement> item) {
        item.getChildren().filtered(TreeItem::isExpanded).forEach(pkg -> {
            this.getExpandedPackages(packages, pkg);
            if (pkg.getValue() instanceof PackageElement) {
                packages.add(((PackageElement) pkg.getValue()).getName());
            }
        });
        return packages;
    }

    public void update() {
        this.refreshClasses();
        this.tabs.getTabs().stream().filter(CodeTab.class::isInstance).map(CodeTab.class::cast)
                .forEach(CodeTab::update);
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

    public static void main(final String[] args) {
        Runtime.getRuntime().addShutdownHook(new Thread(StateHelper::save));

        launch(args);
    }

}
