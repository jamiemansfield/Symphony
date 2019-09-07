//******************************************************************************
// Copyright (c) Jamie Mansfield <https://jamiemansfield.me/>
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//******************************************************************************

package me.jamiemansfield.symphony.gui.tree;

import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import me.jamiemansfield.symphony.gui.SymphonyMain;
import me.jamiemansfield.symphony.gui.menu.ClassContextMenu;
import org.cadixdev.lorenz.model.TopLevelClassMapping;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.Optional;

/**
 * A tree element for classes.
 *
 * @author Jamie Mansfield
 * @since 0.1.0
 */
public class ClassElement implements TreeElement {

    private final SymphonyMain symphony;
    private final TopLevelClassMapping klass;

    private final ContextMenu contextMenu;

    public ClassElement(final SymphonyMain symphony, final TopLevelClassMapping klass) {
        this.symphony = symphony;
        this.klass = klass;
        this.contextMenu = new ClassContextMenu(symphony, klass);
    }

    @Override
    public void activate() {
        this.symphony.displayCodeTab(this.klass);
    }

    @Override
    public Optional<ContextMenu> getContextMenu() {
        return Optional.of(this.contextMenu);
    }

    @Override
    public Optional<Node> getGraphic() {
        return Optional.of(new FontIcon("fth-file"));
    }

    @Override
    public int compareTo(final TreeElement o) {
        if (o instanceof PackageElement) return 1;

        final String key0 = this.toString();
        final String key1 = o.toString();

        if (o instanceof ClassElement) {
            final ClassElement that = (ClassElement) o;

            final boolean root0 = this.klass.getDeobfuscatedPackage().isEmpty();
            final boolean root1 = that.klass.getDeobfuscatedPackage().isEmpty();

            if (root0 && root1) {
                if (key0.length() != key1.length()) {
                    return key0.length() - key1.length();
                }
                else {
                    return key0.compareTo(key1);
                }
            }
        }

        return key0.compareTo(key1);
    }

    @Override
    public String toString() {
        return this.klass.getSimpleDeobfuscatedName();
    }

}
