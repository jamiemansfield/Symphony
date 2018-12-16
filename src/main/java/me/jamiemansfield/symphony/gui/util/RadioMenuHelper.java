//******************************************************************************
// Copyright (c) Jamie Mansfield <https://jamiemansfield.me/>
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//******************************************************************************

package me.jamiemansfield.symphony.gui.util;

import javafx.scene.control.Menu;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.ToggleGroup;

import java.util.Collection;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * A helper class for producing radio menus.
 *
 * @author Jamie Mansfield
 * @since 0.1.0
 */
public final class RadioMenuHelper {

    public static <M extends Menu, T> M create(final M menu,
                                               final Collection<T> options,
                                               final T active,
                                               final Function<T, String> nameExtractor,
                                               final Consumer<T> changedEvent) {
        final ToggleGroup group = new ToggleGroup();
        for (final T option : options) {
            // Create
            final RadioMenuItem item = new RadioMenuItem(nameExtractor.apply(option));
            item.setOnAction(event -> changedEvent.accept(option));

            // Add
            group.getToggles().add(item);
            menu.getItems().add(item);

            // Select the default
            if (Objects.equals(option, active)) {
                group.selectToggle(item);
            }
        }
        return menu;
    }

    private RadioMenuHelper() {
    }

}
