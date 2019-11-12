//******************************************************************************
// Copyright (c) Jamie Mansfield <https://jamiemansfield.me/>
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//******************************************************************************

package me.jamiemansfield.symphony.gui.menu;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputDialog;
import me.jamiemansfield.symphony.gui.Symphony;
import me.jamiemansfield.symphony.gui.dialog.RemappingInputDialog;
import me.jamiemansfield.symphony.util.LocaleHelper;

import java.util.Objects;

/**
 * The package context menu.
 *
 * @author Jamie Mansfield
 * @since 0.1.0
 */
public class PackageContextMenu extends ContextMenu {

    public PackageContextMenu(final Symphony symphony, final String packageName) {
        final MenuItem remap = new MenuItem(LocaleHelper.get("context_menu.remappable.set_deobfuscated_name"));
        remap.setOnAction(event -> {
            final TextInputDialog dialog = new RemappingInputDialog();
            dialog.showAndWait()
                    .map(name -> name.endsWith("/") ? name : name + "/")
                    .ifPresent(deobfName -> {
                        // Set the deobf name
                        symphony.getJar().getMappings().getTopLevelClassMappings().stream()
                                .filter(klass -> Objects.equals(packageName, klass.getDeobfuscatedPackage()) ||
                                        klass.getDeobfuscatedPackage().startsWith(packageName + '/'))
                                .forEach(klass -> {
                                    final String className =
                                            klass.getDeobfuscatedName().substring(packageName.length() + 1);
                                    klass.setDeobfuscatedName(deobfName + className);
                                    symphony.markDirty(klass.hasDeobfuscatedName());
                                });

                        // Update the view
                        symphony.update();
                    });
        });
        this.getItems().add(remap);
    }

}
