//******************************************************************************
// Copyright (c) Jamie Mansfield <https://jamiemansfield.me/>
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//******************************************************************************

package me.jamiemansfield.symphony.gui.tree;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputDialog;
import me.jamiemansfield.symphony.gui.SymphonyMain;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.Objects;
import java.util.Optional;

/**
 * A tree element for packages.
 *
 * @author Jamie Mansfield
 * @since 0.1.0
 */
public class PackageElement implements TreeElement {

    private final SymphonyMain symphony;
    private final String name;
    private final String simpleName;

    private final ContextMenu contextMenu = new ContextMenu() {
        {
            final MenuItem remap = new MenuItem("Set de-obfuscated name");
            remap.addEventHandler(ActionEvent.ACTION, event -> {
                final TextInputDialog dialog = new TextInputDialog();
                dialog.setTitle("Set de-obfuscated name");
                dialog.setHeaderText("Set de-obfuscated name");
                dialog.setContentText("Please enter name:");

                dialog.showAndWait()
                        .map(name -> name.endsWith("/") ? name : name + "/")
                        .ifPresent(deobfName -> {
                    final String packageName = PackageElement.this.name;

                    // Set the deobf name
                    PackageElement.this.symphony.getJar().getMappings().getTopLevelClassMappings().stream()
                            .filter(klass -> Objects.equals(packageName, klass.getDeobfuscatedPackage()) ||
                                    klass.getDeobfuscatedPackage().startsWith(packageName + '/'))
                            .forEach(klass -> {
                                final String className =
                                        klass.getDeobfuscatedName().substring(packageName.length() + 1);
                                klass.setDeobfuscatedName(deobfName + className);
                            });

                    // Update the view
                    PackageElement.this.symphony.update();
                });
            });
            this.getItems().add(remap);
        }
    };

    public PackageElement(final SymphonyMain symphony, final String name) {
        this.symphony = symphony;
        this.name = name;
        this.simpleName = name.substring(name.lastIndexOf('/') + 1);
    }

    public String getName() {
        return this.name;
    }

    @Override
    public void activate() {
    }

    @Override
    public Optional<ContextMenu> getContextMenu() {
        return Optional.of(this.contextMenu);
    }

    @Override
    public Optional<Node> getGraphic() {
        return Optional.of(new FontIcon("fth-folder"));
    }

    @Override
    public int compareTo(final TreeElement o) {
        if (o instanceof ClassElement) return -1;
        return this.toString().compareTo(o.toString());
    }

    @Override
    public String toString() {
        return this.simpleName;
    }

}
