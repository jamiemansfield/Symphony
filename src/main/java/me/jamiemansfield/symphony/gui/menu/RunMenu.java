//******************************************************************************
// Copyright (c) Jamie Mansfield <https://jamiemansfield.me/>
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//******************************************************************************

package me.jamiemansfield.symphony.gui.menu;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import me.jamiemansfield.symphony.gui.menu.mapper.MapperMenuItemProvider;
import me.jamiemansfield.symphony.gui.menu.mapper.SymphonyMappers;
import me.jamiemansfield.symphony.util.LocaleHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * The Symphony 'Run' menu.
 *
 * @author Jamie Mansfield
 * @since 0.1.0
 */
public class RunMenu extends Menu {

    private final List<MenuItem> mappers = new ArrayList<>();

    public RunMenu(final MainMenuBar mainMenuBar) {
        // Settings
        super(LocaleHelper.get("menu.run"));
        this.setMnemonicParsing(true);

        // Mappers
        for (final MapperMenuItemProvider provider : SymphonyMappers.values()) {
            final MenuItem mapper = provider.provide(mainMenuBar.getSymphony());
            mapper.setDisable(true);
            this.mappers.add(mapper);
        }
        this.getItems().addAll(this.mappers);
    }

    public void setMappersDisable(final boolean disable) {
        for (final MenuItem mapper : this.mappers) {
            mapper.setDisable(disable);
        }
    }

}
