//******************************************************************************
// Copyright (c) Jamie Mansfield <https://jamiemansfield.me/>
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//******************************************************************************

package me.jamiemansfield.symphony.gui.menu;

import javafx.event.ActionEvent;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputDialog;
import me.jamiemansfield.symphony.gui.SymphonyMain;
import me.jamiemansfield.symphony.util.LocaleHelper;
import org.cadixdev.lorenz.model.TopLevelClassMapping;

/**
 * The class context menu.
 *
 * @author Jamie Mansfield
 * @since 0.1.0
 */
public class ClassContextMenu extends ContextMenu {

    public ClassContextMenu(final SymphonyMain symphony, final TopLevelClassMapping klass) {
        final MenuItem remap = new MenuItem(LocaleHelper.get("context_menu.class.set_deobfuscated_name"));
        remap.addEventHandler(ActionEvent.ACTION, event -> {
            final String name = klass.getDeobfuscatedName();

            // Configure the dialog
            final TextInputDialog dialog = new TextInputDialog(name);
            dialog.setTitle(LocaleHelper.get("context_menu.class.set_deobfuscated_name"));
            dialog.setHeaderText(LocaleHelper.get("context_menu.class.set_deobfuscated_name"));
            dialog.setContentText(LocaleHelper.get("context_menu.class.set_deobfuscated_name.content"));

            dialog.showAndWait().ifPresent(deobfName -> {
                // Set the deobf name
                klass.setDeobfuscatedName(deobfName);

                // Update the view
                symphony.update();
            });
        });
        this.getItems().add(remap);

        final MenuItem reset = new MenuItem(LocaleHelper.get("context_menu.class.reset_deobfuscated_name"));
        reset.addEventHandler(ActionEvent.ACTION, event -> {
            // Set the deobf name to the obf name (resetting it)
            klass.setDeobfuscatedName(klass.getObfuscatedName());

            // Update the view
            symphony.update();
        });
        this.getItems().add(reset);
    }

}
