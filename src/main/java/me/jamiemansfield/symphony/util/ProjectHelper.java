//******************************************************************************
// Copyright (c) Jamie Mansfield <https://jamiemansfield.me/>
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//******************************************************************************

package me.jamiemansfield.symphony.util;

import me.jamiemansfield.symphony.project.Project;

import java.util.Optional;

/**
 * A helper class for working with Symphony projects.
 *
 * @author Jamie Mansfield
 * @since 0.1.0
 */
public final class ProjectHelper {

    public static final PropertiesKey<String> NAME = PropertiesKey.string("name");

    /**
     * Gets the human-readable name of the project.
     *
     * @param project The project
     * @return The project's name
     */
    public static Optional<String> getName(final Project project) {
        return project.get(NAME);
    }

    /**
     * Sets the human-readable name of the project.
     *
     * @param project The project
     * @param name The name
     */
    public static void setName(final Project project, final String name) {
        project.set(NAME, name);
    }

    private ProjectHelper() {
    }

}
