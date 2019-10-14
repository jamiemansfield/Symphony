//******************************************************************************
// Copyright (c) Jamie Mansfield <https://jamiemansfield.me/>
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//******************************************************************************

package me.jamiemansfield.symphony.gui.menu.mapper;

import javafx.event.ActionEvent;
import javafx.scene.control.MenuItem;
import me.jamiemansfield.symphony.gui.SymphonyMain;
import me.jamiemansfield.symphony.util.LocaleHelper;
import org.cadixdev.survey.context.SurveyContext;
import org.cadixdev.survey.mapper.AbstractMapper;

/**
 * The framework for exposing Survey's {@link AbstractMapper mappers} in
 * Symphony.
 *
 * @param <M> The type of the mapper
 * @param <C> The type of the mapper's configuration
 *
 * @author Jamie Mansfield
 * @since 0.1.0
 */
public abstract class AbstractMapperMenuItem<M extends AbstractMapper<C>, C> extends MenuItem {

    private final SymphonyMain symphony;

    public AbstractMapperMenuItem(final SymphonyMain symphony, final String localeKey) {
        super(LocaleHelper.get(localeKey));
        this.symphony = symphony;

        this.addEventHandler(ActionEvent.ACTION, this::onAction);
    }

    /**
     * Called when the menu item is pressed, to configure the mapper.
     *
     * @return The mapper's configuration
     */
    public abstract C fetchConfig();

    /**
     * Creates a mapper with the given {@link SurveyContext context}, and
     * configuration.
     *
     * @param ctx The context
     * @param config The config
     * @return The mapper
     */
    public abstract M createMapper(final SurveyContext ctx, final C config);

    private void onAction(final ActionEvent event) {
        this.symphony.getJar().runMapper(this::createMapper, this.fetchConfig());
        this.symphony.update();
    }

}
