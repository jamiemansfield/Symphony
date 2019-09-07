//******************************************************************************
// Copyright (c) Jamie Mansfield <https://jamiemansfield.me/>
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//******************************************************************************

package me.jamiemansfield.symphony.gui.tree;

import javafx.scene.Node;
import javafx.scene.control.ContextMenu;

import java.util.Optional;

/**
 * An element within a tree.
 *
 * @author Jamie Mansfield
 * @since 0.1.0
 */
public interface TreeElement extends Comparable<TreeElement> {

    /**
     * The name of the tree element.
     *
     * @return The element name
     */
    @Override
    String toString();

    /**
     * Invoked when the element is double clicked, or other
     * equivalent action.
     */
    default void activate() {
    }

    /**
     * Gets the context menu of this tree element, if applicable.
     *
     * @return The context menu
     */
    default Optional<ContextMenu> getContextMenu() {
        return Optional.empty();
    }

    /**
     * Gets the graphic of this tree element, if applicable.
     *
     * @return The graphic
     */
    default Optional<Node> getGraphic() {
        return Optional.empty();
    }

}
