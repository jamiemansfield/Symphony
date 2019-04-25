//******************************************************************************
// Copyright (c) Jamie Mansfield <https://jamiemansfield.me/>
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//******************************************************************************

package me.jamiemansfield.symphony.gui.menu;

import javafx.application.Platform;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import me.jamiemansfield.symphony.decompiler.Decompiler;
import me.jamiemansfield.symphony.decompiler.Decompilers;
import me.jamiemansfield.symphony.gui.SymphonyMain;
import me.jamiemansfield.symphony.gui.util.RadioMenuHelper;
import me.jamiemansfield.symphony.util.LocaleHelper;

/**
 * The Symphony 'File' menu.
 *
 * @author Jamie Mansfield
 * @since 0.1.0
 */
public class FileMenu extends Menu {

    public final MainMenuBar mainMenuBar;
    private final SymphonyMain symphony;

    // Menu items
    public final FileJarMenu firstJar;
    public final FileJarMenu secondJar;
    public final MenuItem close;

    public FileMenu(final MainMenuBar mainMenuBar) {
        // Settings
        super(LocaleHelper.get("menu.file"));
        this.setMnemonicParsing(true);

        // Fields
        this.mainMenuBar = mainMenuBar;
        this.symphony = mainMenuBar.getSymphony();

        // Jars
        {
            this.firstJar = new FileJarMenu(this, FileJarMenu.Type.FIRST);
            this.getItems().add(this.firstJar);

            this.secondJar = new FileJarMenu(this, FileJarMenu.Type.SECOND);
            this.getItems().add(this.secondJar);
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

    public SymphonyMain getSymphony() {
        return this.symphony;
    }

}
