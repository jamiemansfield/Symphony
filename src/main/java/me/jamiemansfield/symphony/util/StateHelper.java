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

/**
 * A helper class for interacting with the persisted state of
 * Symphony.
 *
 * @author Jamie Mansfield
 * @since 0.1.0
 */
public class StateHelper {

    private static final Path PROPERTIES_PATH = SharedConstants.CONFIG_PATH.resolve("state.properties");
    private static final Properties PROPERTIES = new Properties();

    static {
        if (Files.exists(PROPERTIES_PATH)) {
            try (final InputStream is = Files.newInputStream(PROPERTIES_PATH)) {
                PROPERTIES.load(is);
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
    public static <T> Optional<T> get(final PropertiesKey<T> key) {
        return key.get(PROPERTIES);
    }

    /**
     * Sets the value of the given key.
     *
     * @param key The key
     * @param value The value
     * @param <T> The type of the key
     */
    public static <T> void set(final PropertiesKey<T> key, final T value) {
        key.set(PROPERTIES, value);
    }

    /**
     * Saves the properties to file.
     */
    public static void save() {
        try (final OutputStream os = Files.newOutputStream(PROPERTIES_PATH)) {
            PROPERTIES.store(os, "Symphony State");
        }
        catch (final IOException ex) {
            ex.printStackTrace();
        }
    }

    private StateHelper() {
    }

}
