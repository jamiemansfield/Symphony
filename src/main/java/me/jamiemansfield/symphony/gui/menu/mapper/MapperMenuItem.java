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
import me.jamiemansfield.symphony.util.PairedBoolean;
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
     * a {@link SymphonyMain Symphony} instance, a locale key, a configuration supplier,
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
            final SymphonyMain symphony, final String localeKey,
            final Supplier<PairedBoolean<C>> configSupplier, final BiFunction<SurveyContext, C, M> mapperFunction) {
        return new MapperMenuItem<M, C>(symphony, localeKey) {
            @Override
            public PairedBoolean<C> configure() {
                return configSupplier.get();
            }

            @Override
            public M createMapper(final SurveyContext ctx, final C config) {
                return mapperFunction.apply(ctx, config);
            }
        };
    }

    private final SymphonyMain symphony;

    public MapperMenuItem(final SymphonyMain symphony, final String localeKey) {
        super(LocaleHelper.get(localeKey));
        this.symphony = symphony;

        this.addEventHandler(ActionEvent.ACTION, this::onAction);
    }

    /**
     * Gets the configuration of the mapper, when the {@link MenuItem menu item}
     * is pressed.
     * <p>
     * An {@link PairedBoolean paired boolean} is returned, allowing the mapper
     * operation to be cancelled. It is expected of Symphony mappers to require
     * a further 'confirmation' dialog.
     *
     * @return A paired boolean, where the value is the configuration; and the
     *         state is whether to continue or not.
     */
    public abstract PairedBoolean<C> configure();

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
        final PairedBoolean<C> config = this.configure();
        if (config.getState()) {
            this.symphony.getJar().runMapper(this::createMapper, config.getValue());
            this.symphony.update();
        }
    }

}
