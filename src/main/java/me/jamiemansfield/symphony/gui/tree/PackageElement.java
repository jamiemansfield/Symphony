//******************************************************************************
// Copyright (c) Jamie Mansfield <https://jamiemansfield.me/>
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//******************************************************************************

package me.jamiemansfield.symphony.gui.tree;

import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import me.jamiemansfield.symphony.gui.Symphony;
import me.jamiemansfield.symphony.gui.menu.PackageContextMenu;
import me.jamiemansfield.symphony.gui.util.DisplaySettings;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.Optional;

/**
 * A tree element for packages.
 *
 * @author Jamie Mansfield
 * @since 0.1.0
 */
public class PackageElement implements TreeElement {

    private final String name;
    private final ContextMenu contextMenu;

    public PackageElement(final Symphony symphony, final String name) {
        this.name = name;
        this.contextMenu = new PackageContextMenu(symphony, name);
    }

    public String getName() {
        return this.name;
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
        return DisplaySettings.flattenPackages() ?
                this.name.replace('/', '.') :
                this.name.substring(this.name.lastIndexOf('/') + 1);
    }

}
