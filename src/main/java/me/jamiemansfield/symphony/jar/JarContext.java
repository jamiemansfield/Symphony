//******************************************************************************
// Copyright (c) Jamie Mansfield <https://jamiemansfield.me/>
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//******************************************************************************

package me.jamiemansfield.symphony.jar;

import org.cadixdev.lorenz.MappingSet;
import org.cadixdev.survey.context.SurveyContext;

/**
 * Symphony's implementation of {@link SurveyContext}, backed by a
 * {@link Jar jar}.
 *
 * @author Jamie Mansfield
 * @since 0.1.0
 */
public class JarContext implements SurveyContext {

    private final Jar jar;

    public JarContext(final Jar jar) {
        this.jar = jar;
    }

    @Override
    public MappingSet mappings() {
        return this.jar.getMappings();
    }

    @Override
    public boolean blacklisted(final String s) {
        return false;
    }

}
