//******************************************************************************
// Copyright (c) Jamie Mansfield <https://jamiemansfield.me/>
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//******************************************************************************

package me.jamiemansfield.symphony.gui.menu.mapper;

import javafx.event.ActionEvent;
import javafx.scene.control.MenuItem;
import me.jamiemansfield.symphony.gui.Symphony;
import me.jamiemansfield.symphony.util.LocaleHelper;
import org.cadixdev.survey.context.SurveyContext;
import org.cadixdev.survey.mapper.AbstractMapper;

import java.util.function.BiFunction;
import java.util.function.Supplier;

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
public abstract class MapperMenuItem<M extends AbstractMapper<C>, C> extends MenuItem {

    /**
     * Creates a {@link MenuItem menu item} for a {@link AbstractMapper mapper}, given
     * a {@link Symphony Symphony} instance, a locale key, a configuration supplier,
     * and a mapper constructor.
     *
     * @param symphony The symphony instance
     * @param localeKey The locale key to get the name
     * @param configSupplier The configuration supplier
     * @param mapperFunction The construction function for the mapper
     * @param <M> The type of the mapper
     * @param <C> The type of the mapper's configuration
     * @return The menu item.
     */
    public static <M extends AbstractMapper<C>, C> MapperMenuItem<M, C> of(
            final Symphony symphony, final String localeKey,
            final Supplier<C> configSupplier, final BiFunction<SurveyContext, C, M> mapperFunction) {
        return new MapperMenuItem<M, C>(symphony, localeKey) {
            @Override
            public C fetchConfig() {
                return configSupplier.get();
            }

            @Override
            public M createMapper(final SurveyContext ctx, final C config) {
                return mapperFunction.apply(ctx, config);
            }
        };
    }

    private final Symphony symphony;

    public MapperMenuItem(final Symphony symphony, final String localeKey) {
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
