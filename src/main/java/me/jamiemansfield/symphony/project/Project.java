//******************************************************************************
// Copyright (c) Jamie Mansfield <https://jamiemansfield.me/>
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//******************************************************************************

package me.jamiemansfield.symphony.project;

import me.jamiemansfield.symphony.SharedConstants;
import me.jamiemansfield.symphony.util.PropertiesKey;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.Properties;

/**
 * A Symphony project workspace.
 *
 * @author Jamie Mansfield
 * @since 0.1.0
 */
public class Project {

    private final String id;
    private final Path dir;
    private final Path configPath;

    private final Properties config = new Properties();

    public Project(final String id) {
        this.id = id;
        this.dir = SharedConstants.PROJECTS_PATH.resolve(id);
        this.configPath = this.dir.resolve("config.properties");

        // Load the configuration from file, if it exists
        if (Files.exists(this.configPath)) {
            try (final InputStream is = Files.newInputStream(this.configPath)) {
                this.config.load(is);
            }
            catch (final IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * Gets the <strong>unique</strong> identifier of this project.
     *
     * @return The identifier
     */
    public String getId() {
        return this.id;
    }

    /**
     * Gets the value, if available, of the given key.
     *
     * @param key The key
     * @param <T> The type of the key
     * @return The value, wrapped in an {@link Optional}
     */
    public <T> Optional<T> get(final PropertiesKey<T> key) {
        return key.get(this.config);
    }

    /**
     * Sets the value of the given key.
     *
     * @param key The key
     * @param value The value
     * @param <T> The type of the key
     */
    public <T> void set(final PropertiesKey<T> key, final T value) {
        key.set(this.config, value);
    }

    /**
     * Saves the project configuration to file.
     */
    public void save() {
        try (final OutputStream os = Files.newOutputStream(this.configPath)) {
            this.config.store(os, "Symphony State");
        }
        catch (final IOException ex) {
            ex.printStackTrace();
        }
    }

}
