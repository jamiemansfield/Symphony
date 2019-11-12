//******************************************************************************
// Copyright (c) Jamie Mansfield <https://jamiemansfield.me/>
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//******************************************************************************

package me.jamiemansfield.symphony.gui.menu.mapper;

import javafx.scene.control.MenuItem;
import me.jamiemansfield.symphony.gui.Symphony;
import org.cadixdev.survey.mapper.EnumConstantsMapper;
import org.cadixdev.survey.mapper.config.EnumConstantsMapperConfig;

/**
 * A {@link MapperMenuItemProvider mapper menu item provider} to expose
 * Survey's {@link EnumConstantsMapper enum constants mapper}.
 *
 * @author Jamie Mansfield
 * @since 0.1.0
 */
public class EnumConstantsMapperMenuItemProvider implements MapperMenuItemProvider {

    @Override
    public MenuItem provide(final Symphony symphony) {
        return MapperMenuItem.of(symphony, "menu.run.map_enum_constants",
                EnumConstantsMapperConfig::new,
                EnumConstantsMapper::new
        );
    }

    @Override
    public String toString() {
        return "enum_constants";
    }

}
