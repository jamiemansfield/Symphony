//******************************************************************************
// Copyright (c) Jamie Mansfield <https://jamiemansfield.me/>
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//******************************************************************************

package me.jamiemansfield.symphony.gui.menu.mapper;

import javafx.scene.control.MenuItem;
import me.jamiemansfield.symphony.gui.SymphonyMain;
import org.cadixdev.survey.mapper.AbstractMapper;

/**
 * A provider for {@link MenuItem menu items} specifically for adding
 * support for Survey's {@link AbstractMapper mappers} to Symphony.
 * <p>
 * Implementations are encouraged to use {@link AbstractMapperMenuItem}
 * as the base of their menu item.
 *
 * @author Jamie Mansfield
 * @since 0.1.0
 */
public interface MapperMenuItemProvider {

    /**
     * Creates a menu item for the mapper, for the given Symphony
     * instance.
     *
     * @param symphony The symphony instance
     * @return The menu item
     */
    MenuItem provide(final SymphonyMain symphony);

    /**
     * Gets the identifier of the menu item provider.
     *
     * @return The identifier
     */
    @Override
    String toString();

}
