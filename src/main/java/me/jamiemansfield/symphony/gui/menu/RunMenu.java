//******************************************************************************
// Copyright (c) Jamie Mansfield <https://jamiemansfield.me/>
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//******************************************************************************

package me.jamiemansfield.symphony.gui.menu;

import javafx.event.ActionEvent;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import me.jamiemansfield.symphony.util.LocaleHelper;
import org.cadixdev.survey.mapper.EnumConstantsMapper;
import org.cadixdev.survey.mapper.config.EnumConstantsMapperConfig;

/**
 * The Symphony 'Run' menu.
 *
 * @author Jamie Mansfield
 * @since 0.1.0
 */
public class RunMenu extends Menu {

    public final MenuItem mapEnumConstants;

    public RunMenu(final MainMenuBar mainMenuBar) {
        // Settings
        super(LocaleHelper.get("menu.run"));
        this.setMnemonicParsing(true);

        // Map Enum Constants
        {
            this.mapEnumConstants = new MenuItem(LocaleHelper.get("menu.run.map_enum_constants"));
            this.mapEnumConstants.setDisable(true);
            this.mapEnumConstants.addEventHandler(ActionEvent.ACTION, event -> {
                mainMenuBar.getSymphony().getJar().runMapper(EnumConstantsMapper::new, new EnumConstantsMapperConfig() {
                    {
                        this.mapSyntheticValues = true;
                    }
                });
                mainMenuBar.getSymphony().update();
            });
            this.getItems().add(this.mapEnumConstants);
        }
    }

}
