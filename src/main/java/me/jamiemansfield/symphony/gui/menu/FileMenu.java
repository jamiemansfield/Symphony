//******************************************************************************
// Copyright (c) Jamie Mansfield <https://jamiemansfield.me/>
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//******************************************************************************

package me.jamiemansfield.symphony.gui.menu;

import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.ToggleGroup;
import javafx.stage.FileChooser;
import me.jamiemansfield.symphony.decompiler.DecompilerManager;
import me.jamiemansfield.symphony.decompiler.IDecompiler;
import me.jamiemansfield.symphony.gui.SymphonyMain;
import me.jamiemansfield.symphony.gui.concurrent.TaskManager;
import me.jamiemansfield.symphony.gui.util.MappingsHelper;
import me.jamiemansfield.symphony.jar.Jar;
import me.jamiemansfield.symphony.util.LocaleHelper;

import java.io.File;
import java.io.IOException;
import java.util.jar.JarFile;

/**
 * The Symphony 'File' menu.
 *
 * @author Jamie Mansfield
 * @since 0.1.0
 */
public class FileMenu extends Menu {

    private static IDecompiler DECOMPILER = DecompilerManager.getDefault();

    public static IDecompiler decompiler() {
        return DECOMPILER;
    }

    private final MainMenuBar mainMenu;
    private final SymphonyMain symphony;

    // Menu items
    public final MenuItem openJar;
    public final MenuItem closeJar;
    public final MenuItem loadMappings;
    public final MenuItem saveMappings;
    public final MenuItem saveMappingsAs;
    public final MenuItem exportRemappedJar;
    public final MenuItem close;

    // File choosers
    private FileChooser openJarFileChooser;
    private FileChooser exportJarFileChooser;

    public FileMenu(final MainMenuBar mainMenuBar) {
        // Settings
        super(LocaleHelper.get("menu.file"));
        this.setMnemonicParsing(true);

        // Fields
        this.mainMenu = mainMenuBar;
        this.symphony = mainMenuBar.getSymphony();

        // Jar related
        {
            this.openJar = new MenuItem(LocaleHelper.get("menu.file.open_jar"));
            this.openJar.addEventHandler(ActionEvent.ACTION, this::openJar);
            this.getItems().add(this.openJar);

            this.closeJar = new MenuItem(LocaleHelper.get("menu.file.close_jar"));
            this.closeJar.setDisable(true);
            this.getItems().add(this.closeJar);
        }
        this.getItems().add(new SeparatorMenuItem());

        // Mapping related
        {
            this.loadMappings = new MenuItem(LocaleHelper.get("menu.file.load_mappings"));
            this.loadMappings.setDisable(true);
            this.loadMappings.addEventHandler(ActionEvent.ACTION, this::loadMappings);
            this.getItems().add(this.loadMappings);

            this.saveMappings = new MenuItem(LocaleHelper.get("menu.file.save_mappings"));
            this.saveMappings.setDisable(true);
            this.getItems().add(this.saveMappings);

            this.saveMappingsAs = new MenuItem(LocaleHelper.get("menu.file.save_mappings_as"));
            this.saveMappingsAs.setDisable(true);
            this.saveMappingsAs.addEventHandler(ActionEvent.ACTION, this::saveMappingsAs);
            this.getItems().add(this.saveMappingsAs);
        }
        this.getItems().add(new SeparatorMenuItem());

        // Binary related
        {
            this.exportRemappedJar = new MenuItem(LocaleHelper.get("menu.file.export_remapped_jar"));
            this.exportRemappedJar.setDisable(true);
            this.exportRemappedJar.addEventHandler(ActionEvent.ACTION, this::exportRemappedJar);
            this.getItems().add(this.exportRemappedJar);
        }
        this.getItems().add(new SeparatorMenuItem());

        // Settings
        {
            final Menu settings = new Menu(LocaleHelper.get("menu.file.settings"));
            // Decompiler
            {
                final ToggleGroup decompilerGroup = new ToggleGroup();
                final Menu decompilerMenu = new Menu(LocaleHelper.get("menu.file.settings.decompiler"));

                for (final IDecompiler decompiler : DecompilerManager.getDecompilers()) {
                    final RadioMenuItem menuItem = new RadioMenuItem(decompiler.getName());
                    menuItem.addEventHandler(ActionEvent.ACTION, event -> {
                        // Set the decompiler
                        DECOMPILER = decompiler;

                        // Update the views
                        symphony.update();
                    });
                    decompilerGroup.getToggles().add(menuItem);
                    decompilerMenu.getItems().add(menuItem);
                    if (decompiler == DecompilerManager.getDefault()) {
                        decompilerGroup.selectToggle(menuItem);
                    }
                }

                settings.getItems().add(decompilerMenu);
            }
            this.getItems().add(settings);
        }
        this.getItems().add(new SeparatorMenuItem());

        // Program related
        {
            this.close = new MenuItem(LocaleHelper.get("menu.file.quit"));
            this.close.addEventHandler(ActionEvent.ACTION, event -> Platform.exit());
            this.getItems().add(this.close);
        }
    }

    private void openJar(final ActionEvent event) {
        if (this.openJarFileChooser == null) {
            this.openJarFileChooser = new FileChooser();
            this.openJarFileChooser.setTitle("Select JAR File");
            this.openJarFileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("JAR file", "*.jar")
            );
        }

        final File jarPath = this.openJarFileChooser.showOpenDialog(this.symphony.getStage());
        if (jarPath == null) return;

        try {
            this.symphony.jar = new Jar(new JarFile(jarPath));
        }
        catch (final IOException ex) {
            ex.printStackTrace();
            return;
        }

        // Enable menu items
        this.closeJar.setDisable(false);
        this.loadMappings.setDisable(false);
        this.saveMappings.setDisable(false);
        this.saveMappingsAs.setDisable(false);
        this.exportRemappedJar.setDisable(false);
        this.mainMenu.navigate.klass.setDisable(false);

        // Refresh classes view
        this.symphony.refreshClasses();
    }

    private void loadMappings(final ActionEvent event) {
        if (MappingsHelper.loadMappings(this.symphony.getStage(), this.symphony.jar.getMappings())) {
            // Update views
            this.symphony.update();
        }
    }

    private void saveMappingsAs(final ActionEvent event) {
        MappingsHelper.saveMappingsAs(this.symphony.getStage(), this.symphony.jar.getMappings());
    }

    private void exportRemappedJar(final ActionEvent event) {
        if (this.exportJarFileChooser == null) {
            this.exportJarFileChooser = new FileChooser();
            this.exportJarFileChooser.setTitle("Export Remapped Jar");
            this.exportJarFileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("JAR file", "*.jar")
            );
        }

        final File jarPath = this.exportJarFileChooser.showSaveDialog(this.symphony.getStage());
        if (jarPath == null) return;

        final RemapperService remapperService = new RemapperService(this.symphony.jar, jarPath);
        remapperService.start();
    }

    private class RemapperService extends Service<Void> {

        private final Jar jar;
        private final File to;

        public RemapperService(final Jar jar, final File to) {
            this.jar = jar;
            this.to = to;
        }

        @Override
        protected Task<Void> createTask() {
            return TaskManager.INSTANCE.new TrackedTask<Void>() {
                {
                    this.updateTitle("remap: " + RemapperService.this.to.getName());
                }

                @Override
                protected Void call() {
                    RemapperService.this.jar.exportRemapped(RemapperService.this.to);
                    return null;
                }
            };
        }

    }

}
