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
import javafx.scene.control.Alert;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import me.jamiemansfield.symphony.Jar;
import me.jamiemansfield.symphony.SharedConstants;
import me.jamiemansfield.symphony.gui.tab.CodeTab;
import me.jamiemansfield.symphony.gui.tab.WelcomeTab;
import org.cadixdev.lorenz.io.MappingFormats;

import java.io.File;
import java.io.IOException;
import java.util.jar.JarFile;
import java.util.stream.Stream;

/**
 * The Main-Class behind Symphony.
 *
 * @author Jamie Mansfield
 * @since 0.1.0
 */
public final class SymphonyMain extends Application {

    private Stage stage;
    private TabPane tabs;

    // File menu
    private MenuItem openJar;
    private MenuItem closeJar;
    private MenuItem loadMappings;
    private MenuItem saveMappings;
    private MenuItem saveMappingsAs;
    private MenuItem exportRemappedJar;
    private MenuItem close;

    // Navigate menu
    private MenuItem navigateKlass;

    // Active jar
    private Jar jar;

    // File choosers
    private FileChooser openJarFileChooser;
    private FileChooser loadMappingsFileChooser;
    private FileChooser exportJarFileChooser;

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
        this.stage.setTitle("Symphony v" + SharedConstants.VERSION);
        this.stage.setWidth(1280);
        this.stage.setHeight(720);

        // Root GUI container
        final BorderPane root = new BorderPane();

        // Main Menu
        final MenuBar mainMenu = new MenuBar();
        {
            final Menu file = new Menu("_File");
            file.setMnemonicParsing(true);
            {
                // Jar related
                {
                    this.openJar = new MenuItem("Open Jar...");
                    this.openJar.addEventHandler(ActionEvent.ACTION, this::openJar);
                    file.getItems().add(this.openJar);

                    this.closeJar = new MenuItem("Close Jar...");
                    this.closeJar.setDisable(true);
                    file.getItems().add(this.closeJar);
                }
                file.getItems().add(new SeparatorMenuItem());
                // Mapping related
                {
                    this.loadMappings = new MenuItem("Load Mappings...");
                    this.loadMappings.setDisable(true);
                    this.loadMappings.addEventHandler(ActionEvent.ACTION, this::loadMappings);
                    file.getItems().add(this.loadMappings);

                    this.saveMappings = new MenuItem("Save Mappings");
                    this.saveMappings.setDisable(true);
                    file.getItems().add(this.saveMappings);

                    this.saveMappingsAs = new MenuItem("Save Mappings As...");
                    this.saveMappingsAs.setDisable(true);
                    file.getItems().add(this.saveMappingsAs);
                }
                file.getItems().add(new SeparatorMenuItem());
                // Binary related
                {
                    this.exportRemappedJar = new MenuItem("Export Remapped Jar...");
                    this.exportRemappedJar.setDisable(true);
                    this.exportRemappedJar.addEventHandler(ActionEvent.ACTION, this::exportRemappedJar);
                    file.getItems().add(this.exportRemappedJar);
                }
                file.getItems().add(new SeparatorMenuItem());
                // Program related
                {
                    this.close = new MenuItem("Quit");
                    this.close.addEventHandler(ActionEvent.ACTION, event -> Platform.exit());
                    file.getItems().add(this.close);
                }
            }
            mainMenu.getMenus().add(file);

            final Menu navigate = new Menu("_Navigate");
            navigate.setMnemonicParsing(true);
            {
                {
                    this.navigateKlass = new MenuItem("Class");
                    this.navigateKlass.setDisable(true);
                    this.navigateKlass.addEventHandler(ActionEvent.ACTION, this::navigateToClass);
                    navigate.getItems().add(navigateKlass);
                }
            }
            mainMenu.getMenus().add(navigate);

            final Menu help = new Menu("_Help");
            help.setMnemonicParsing(true);
            {
                {
                    final MenuItem welcomeTab = new MenuItem("Open Welcome Tab");
                    welcomeTab.addEventHandler(ActionEvent.ACTION, this::displayWelcomeTab);
                    help.getItems().add(welcomeTab);
                }
                help.getItems().add(new SeparatorMenuItem());
                {
                    final MenuItem about = new MenuItem("About Symphony");
                    about.addEventHandler(ActionEvent.ACTION, this::displayAbout);
                    help.getItems().add(about);
                }
            }
            mainMenu.getMenus().add(help);
        }
        root.setTop(mainMenu);

        // Classes view
        final BorderPane classesView = new BorderPane();
        {
            final TextField search = new TextField();
            search.setPromptText("Search");
            classesView.setTop(search);
        }
        // TODO: implement
        //root.setLeft(classesView);

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

    private void openJar(final ActionEvent event) {
        if (this.openJarFileChooser == null) {
            this.openJarFileChooser = new FileChooser();
            this.openJarFileChooser.setTitle("Select JAR File");
            this.openJarFileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("JAR file", "*.jar")
            );
        }

        final File jarPath = this.openJarFileChooser.showOpenDialog(this.stage);
        if (jarPath == null) return;

        try {
            this.jar = new Jar(new JarFile(jarPath));
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
        this.navigateKlass.setDisable(false);
    }

    private void loadMappings(final ActionEvent event) {
        if (this.loadMappingsFileChooser == null) {
            this.loadMappingsFileChooser = new FileChooser();
            this.loadMappingsFileChooser.setTitle("Load Mappings");
            this.loadMappingsFileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("SRG", "*.srg")
            );
        }

        final File mappingsPath = this.loadMappingsFileChooser.showOpenDialog(this.stage);
        if (mappingsPath == null) return;

        try {
            MappingFormats.SRG.read(this.jar.getMappings(), mappingsPath.toPath());
        }
        catch (final IOException ex) {
            ex.printStackTrace();
        }
    }

    private void exportRemappedJar(final ActionEvent event) {
        if (this.exportJarFileChooser == null) {
            this.exportJarFileChooser = new FileChooser();
            this.exportJarFileChooser.setTitle("Export Remapped Jar");
            this.exportJarFileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("JAR file", "*.jar")
            );
        }

        final File jarPath = this.exportJarFileChooser.showSaveDialog(this.stage);
        if (jarPath == null) return;

        this.jar.exportRemapped(jarPath);
    }

    private void navigateToClass(final ActionEvent event) {
        final TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Navigate to Class");
        dialog.setHeaderText("Navigate to Class");
        dialog.setContentText("Please enter class:");

        dialog.showAndWait().ifPresent(klass -> {
            if (!this.jar.hasClass(klass)) return;
            final CodeTab tab = new CodeTab(this.jar,
                    this.jar.getMappings().getOrCreateTopLevelClassMapping(klass));
            this.tabs.getTabs().add(tab);
            this.tabs.getSelectionModel().select(tab);
        });
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

    private void displayAbout(final ActionEvent event) {
        final Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About Symphony");
        alert.setHeaderText("Symphony v" + SharedConstants.VERSION);

        final TextFlow textFlow = new TextFlow();
        {
            Stream.of(
                    "Copyright (c) 2018 Jamie Mansfield <https://www.jamiemansfield.me/>\n\n",
                    // The following is adapted from a similar statement Mozilla make for Firefox
                    // See about:rights
                    "Symphony is made available under the terms of the Mozilla Public License, giving\n",
                    "you the freedom to use, copy, and distribute Symphony to others, in addition to\n",
                    "the right to distribute modified versions."
            ).map(Text::new).forEach(textFlow.getChildren()::add);
        }
        alert.getDialogPane().setContent(textFlow);

        alert.showAndWait();
    }

    public static void main(final String[] args) {
        launch(args);
    }

}
