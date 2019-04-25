//******************************************************************************
// Copyright (c) Jamie Mansfield <https://jamiemansfield.me/>
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//******************************************************************************

package me.jamiemansfield.symphony.gui.menu;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.stage.FileChooser;
import me.jamiemansfield.symphony.gui.SymphonyMain;
import me.jamiemansfield.symphony.gui.concurrent.TaskManager;
import me.jamiemansfield.symphony.gui.util.MappingsHelper;
import me.jamiemansfield.symphony.jar.Jar;
import me.jamiemansfield.symphony.util.LocaleHelper;
import me.jamiemansfield.symphony.util.PropertiesKey;
import me.jamiemansfield.symphony.util.StateHelper;

import java.io.File;
import java.io.IOException;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.jar.JarFile;

/**
 * The Symphony 'File -> X Jar' menu.
 *
 * @author Jamie Mansfield
 * @since 0.1.0
 */
public class FileJarMenu extends Menu {

    private static final PropertiesKey<File> LAST_OPEN_DIRECTORY = PropertiesKey.file(
            "last_jar_directory"
    );
    private static final PropertiesKey<File> LAST_EXPORT_DIRECTORY = PropertiesKey.file(
            "last_export_jar_directory"
    );

    private final FileMenu fileMenu;
    private final SymphonyMain symphony;
    private final Type type;

    // Menu items
    public final MenuItem openJar;
    public final MenuItem closeJar;
    public final MenuItem loadMappings;
    public final MenuItem saveMappings;
    public final MenuItem saveMappingsAs;
    public final MenuItem exportRemappedJar;

    // File choosers
    private FileChooser openJarFileChooser;
    private FileChooser exportJarFileChooser;

    public FileJarMenu(final FileMenu fileMenu, final Type type) {
        // Settings
        super(type.getLocalised());
        this.setMnemonicParsing(true);

        // Fields
        this.fileMenu = fileMenu;
        this.symphony = fileMenu.getSymphony();
        this.type = type;

        // Jar related
        {
            this.openJar = new MenuItem(LocaleHelper.get("menu.file.open_jar"));
            this.openJar.setOnAction(this::openJar);
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
            this.getItems().add(this.loadMappings);

            this.saveMappings = new MenuItem(LocaleHelper.get("menu.file.save_mappings"));
            this.saveMappings.setDisable(true);
            this.getItems().add(this.saveMappings);

            this.saveMappingsAs = new MenuItem(LocaleHelper.get("menu.file.save_mappings_as"));
            this.saveMappingsAs.setDisable(true);
            this.saveMappingsAs.setOnAction(this::saveMappingsAs);
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
    }

    public void toggle(final boolean opening) {
        this.closeJar.setDisable(!opening);
        this.loadMappings.setDisable(!opening);
        // this.saveMappings.setDisable(!opening); // TODO: implement
        this.saveMappingsAs.setDisable(!opening);
        this.exportRemappedJar.setDisable(!opening);
        this.fileMenu.mainMenuBar.navigate.klass.setDisable(!opening);
    }

    private void openJar(final ActionEvent event) {
        if (this.openJarFileChooser == null) {
            this.openJarFileChooser = new FileChooser();
            this.openJarFileChooser.setTitle(LocaleHelper.get("menu.file.open_jar.file_chooser"));
            this.openJarFileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("JAR file", "*.jar")
            );

            // Use the last used directory
            StateHelper.get(LAST_OPEN_DIRECTORY).ifPresent(this.openJarFileChooser::setInitialDirectory);
        }

        final File jarPath = this.openJarFileChooser.showOpenDialog(this.symphony.getStage());
        if (jarPath == null) return;

        try {
            this.type.set(this.symphony, new Jar(new JarFile(jarPath)));
        }
        catch (final IOException ex) {
            ex.printStackTrace();
            return;
        }

        // Update state
        StateHelper.set(LAST_OPEN_DIRECTORY, jarPath.getParentFile());
    }

    private void closeJar(final ActionEvent actionEvent) {
        this.type.set(this.symphony, null);
    }

    private void loadMappings(final ActionEvent event) {
        if (MappingsHelper.loadMappings(this.symphony.getStage(), this.type.get(this.symphony).getMappings())) {
            // Update views
            this.symphony.update();
        }
    }

    private void saveMappingsAs(final ActionEvent event) {
        MappingsHelper.saveMappingsAs(this.symphony.getStage(), this.type.get(this.symphony).getMappings());
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

        final RemapperService remapperService = new RemapperService(this.type.get(this.symphony), jarPath);
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

    public enum Type {

        FIRST(
                "menu.file.first_jar",
                SymphonyMain::getFirstJar,
                SymphonyMain::setFirstJar
        ),
        SECOND(
                "menu.file.second_jar",
                SymphonyMain::getSecondJar,
                SymphonyMain::setSecondJar
        ),
        ;

        private final String key;
        private final Function<SymphonyMain, Jar> getter;
        private final BiConsumer<SymphonyMain, Jar> setter;

        Type(final String key,
             final Function<SymphonyMain, Jar> getter,
             final BiConsumer<SymphonyMain, Jar> setter) {
            this.key = key;
            this.getter = getter;
            this.setter = setter;
        }

        public String getLocalised() {
            return LocaleHelper.get(this.key);
        }

        public Jar get(final SymphonyMain symphony) {
            return this.getter.apply(symphony);
        }

        public void set(final SymphonyMain symphony, final Jar jar) {
            this.setter.accept(symphony, jar);
        }

    }

}
