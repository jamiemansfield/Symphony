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
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.stage.FileChooser;
import me.jamiemansfield.symphony.decompiler.Decompiler;
import me.jamiemansfield.symphony.decompiler.Decompilers;
import me.jamiemansfield.symphony.gui.SymphonyMain;
import me.jamiemansfield.symphony.gui.concurrent.TaskManager;
import me.jamiemansfield.symphony.gui.util.MappingsHelper;
import me.jamiemansfield.symphony.gui.util.RadioMenuHelper;
import me.jamiemansfield.symphony.jar.Jar;
import me.jamiemansfield.symphony.util.LocaleHelper;
import me.jamiemansfield.symphony.util.PropertiesKey;
import me.jamiemansfield.symphony.util.StateHelper;

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

    private static final PropertiesKey<File> LAST_OPEN_DIRECTORY = PropertiesKey.file(
            "last_jar_directory"
    );
    private static final PropertiesKey<File> LAST_EXPORT_DIRECTORY = PropertiesKey.file(
            "last_export_jar_directory"
    );

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
        this.symphony = mainMenuBar.getSymphony();

        // Jar related
        {
            this.openJar = new MenuItem(LocaleHelper.get("menu.file.open_jar"));
            this.openJar.setOnAction(this::openJar);
            this.openJar.setAccelerator(new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN));
            this.getItems().add(this.openJar);

            this.closeJar = new MenuItem(LocaleHelper.get("menu.file.close_jar"));
            this.closeJar.setDisable(true);
            this.closeJar.setOnAction(this::closeJar);
            this.getItems().add(this.closeJar);
        }
        this.getItems().add(new SeparatorMenuItem());

        // Mapping related
        {
            this.loadMappings = new MenuItem(LocaleHelper.get("menu.file.load_mappings"));
            this.loadMappings.setDisable(true);
            this.loadMappings.setOnAction(this::loadMappings);
            this.loadMappings.setAccelerator(new KeyCodeCombination(KeyCode.L, KeyCombination.CONTROL_DOWN));
            this.getItems().add(this.loadMappings);

            this.saveMappings = new MenuItem(LocaleHelper.get("menu.file.save_mappings"));
            this.saveMappings.setDisable(true);
            this.saveMappings.setOnAction(this::saveMappings);
            this.saveMappings.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN));
            this.getItems().add(this.saveMappings);

            this.saveMappingsAs = new MenuItem(LocaleHelper.get("menu.file.save_mappings_as"));
            this.saveMappingsAs.setDisable(true);
            this.saveMappingsAs.setOnAction(this::saveMappingsAs);
            this.saveMappingsAs.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN,
                    KeyCombination.ALT_DOWN));
            this.getItems().add(this.saveMappingsAs);
        }
        this.getItems().add(new SeparatorMenuItem());

        // Binary related
        {
            this.exportRemappedJar = new MenuItem(LocaleHelper.get("menu.file.export_remapped_jar"));
            this.exportRemappedJar.setDisable(true);
            this.exportRemappedJar.setOnAction(this::exportRemappedJar);
            this.getItems().add(this.exportRemappedJar);
        }
        this.getItems().add(new SeparatorMenuItem());

        // Settings
        {
            final Menu settings = new Menu(LocaleHelper.get("menu.file.settings"));
            // Decompiler
            {
                final Menu decompilerMenu = RadioMenuHelper.create(
                        new Menu(LocaleHelper.get("menu.file.settings.default_decompiler")),
                        Decompilers.values(),
                        Decompilers.getDefault(),
                        Decompiler::getName,
                        decompiler -> {
                            // Set the decompiler
                            Decompilers.setDefault(decompiler);

                            // Update the views
                            this.symphony.update();
                        }
                );
                settings.getItems().add(decompilerMenu);
            }
            this.getItems().add(settings);
        }
        this.getItems().add(new SeparatorMenuItem());

        // Program related
        {
            this.close = new MenuItem(LocaleHelper.get("menu.file.quit"));
            this.close.setOnAction(event -> Platform.exit());
            this.getItems().add(this.close);
        }
    }

    private void openJar(final ActionEvent event) {
        if (this.openJarFileChooser == null) {
            this.openJarFileChooser = new FileChooser();
            this.openJarFileChooser.setTitle(LocaleHelper.get("menu.file.open_jar.file_chooser"));
            this.openJarFileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("JAR file", "*.jar")
            );

            // Use the last used directory
            StateHelper.get(LAST_OPEN_DIRECTORY)
                    .filter(File::exists)
                    .ifPresent(this.openJarFileChooser::setInitialDirectory);
        }

        final File jarPath = this.openJarFileChooser.showOpenDialog(this.symphony.getStage());
        if (jarPath == null) return;

        try {
            this.symphony.setJar(new Jar(new JarFile(jarPath)));
        }
        catch (final IOException ex) {
            ex.printStackTrace();
            return;
        }

        // Update state
        StateHelper.set(LAST_OPEN_DIRECTORY, jarPath.getParentFile());
    }

    private void closeJar(final ActionEvent actionEvent) {
        this.symphony.setJar(null);
    }

    private void loadMappings(final ActionEvent event) {
        if (MappingsHelper.loadMappings(this.symphony.getStage(), this.symphony.getJar().getMappings())) {
            // Update views
            this.symphony.update();

            // Enable File -> Save
            this.saveMappings.setDisable(false);
        }
    }

    private void saveMappings(final ActionEvent event) {
        MappingsHelper.saveMappings(this.symphony.getStage(), this.symphony.getJar().getMappings());
    }

    private void saveMappingsAs(final ActionEvent event) {
        if (MappingsHelper.saveMappingsAs(this.symphony.getStage(), this.symphony.getJar().getMappings())) {
            // Enable File -> Save
            this.saveMappings.setDisable(false);
        }
    }

    private void exportRemappedJar(final ActionEvent event) {
        if (this.exportJarFileChooser == null) {
            this.exportJarFileChooser = new FileChooser();
            this.exportJarFileChooser.setTitle(LocaleHelper.get("menu.file.export_remapped_jar"));
            this.exportJarFileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("JAR file", "*.jar")
            );

            // Use the last used directory
            StateHelper.get(LAST_EXPORT_DIRECTORY).ifPresent(this.exportJarFileChooser::setInitialDirectory);
        }

        final File jarPath = this.exportJarFileChooser.showSaveDialog(this.symphony.getStage());
        if (jarPath == null) return;

        final RemapperService remapperService = new RemapperService(this.symphony.getJar(), jarPath);
        remapperService.start();

        // Update state
        StateHelper.set(LAST_EXPORT_DIRECTORY, jarPath.getParentFile());
    }

    private class RemapperService extends Service<Void> {

        private final Jar jar;
        private final File to;

        RemapperService(final Jar jar, final File to) {
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
