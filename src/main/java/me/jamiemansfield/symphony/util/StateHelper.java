//******************************************************************************
// Copyright (c) Jamie Mansfield <https://jamiemansfield.me/>
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//******************************************************************************

package me.jamiemansfield.symphony.util;

import me.jamiemansfield.symphony.SharedConstants;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.Properties;
import java.util.function.Supplier;

/**
 * A helper class for interacting with the persisted state of
 * Symphony.
 *
 * @author Jamie Mansfield
 * @since 0.1.0
 */
public enum StateHelper {

    GENERAL,
    DISPLAY,
    ;

    /**
     * Saves all the properties to file.
     */
    public static void saveAll() {
        for (final StateHelper helper : values()) {
            helper.save();
        }
    }

    private final Path path;
    private final Properties properties;

    StateHelper() {
        this.path = SharedConstants.CONFIG_PATH.resolve(this.name().toLowerCase() + ".properties");
        this.properties = new Properties();

        // Read
        if (Files.exists(this.path)) {
            try (final InputStream is = Files.newInputStream(this.path)) {
                this.properties.load(is);
            }
            catch (final IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * Gets the value, if available, of the given key.
     *
     * @param key The key
     * @param <T> The type of the key
     * @return The value, wrapped in an {@link Optional}
     */
    public <T> Optional<T> get(final PropertiesKey<T> key) {
        return key.get(this.properties);
    }

    /**
     * Gets the value, if available, of the given key - if not
     * available, will attempt to compute the value, using the given
     * supplier. The value will be set, provided it is not null.
     *
     * @param key The key
     * @param supplier The compute supplier
     * @param <T> The type of the key
     * @return The value
     */
    public <T> T computeIfAbsent(final PropertiesKey<T> key, final Supplier<T> supplier) {
        return this.get(key).orElseGet(() -> {
            final T value = supplier.get();
            if (value == null) return null;
            this.set(key, value);
            return value;
        });
    }

    /**
     * Sets the value of the given key.
     *
     * @param key The key
     * @param value The value
     * @param <T> The type of the key
     */
    public <T> void set(final PropertiesKey<T> key, final T value) {
        key.set(this.properties, value);
    }

    /**
     * Saves the properties to file.
     */
    public void save() {
        try (final OutputStream os = Files.newOutputStream(this.path)) {
            this.properties.store(os, "Symphony State");
        }
        catch (final IOException ex) {
            ex.printStackTrace();
        }
    }

}
