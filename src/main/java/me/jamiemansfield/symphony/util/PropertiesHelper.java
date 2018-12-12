//******************************************************************************
// Copyright (c) Jamie Mansfield <https://jamiemansfield.me/>
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//******************************************************************************

package me.jamiemansfield.symphony.util;

import me.jamiemansfield.symphony.SharedConstants;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.Properties;
import java.util.function.Function;

/**
 * A helper class for interacting with the persisted settings of
 * Symphony.
 *
 * @author Jamie Mansfield
 * @since 0.1.0
 */
public final class PropertiesHelper {

    private static final Path PROPERTIES_PATH = SharedConstants.CONFIG_PATH.resolve("global.properties");
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
    public static <T> Optional<T> get(final Key<T> key) {
        final String value = PROPERTIES.getProperty(key.getKey());
        if (value == null) return Optional.empty();
        return Optional.ofNullable(key.deserialise(value));
    }

    /**
     * Sets the value of the given key.
     *
     * @param key The key
     * @param value The value
     * @param <T> The type of the key
     */
    public static <T> void set(final Key<T> key, final T value) {
        PROPERTIES.setProperty(key.getKey(), key.serialise(value));
    }

    /**
     * Saves the properties to file.
     */
    public static void save() {
        try (final OutputStream os = Files.newOutputStream(PROPERTIES_PATH)) {
            PROPERTIES.store(os, "Symphony Global Properties");
        }
        catch (final IOException ex) {
            ex.printStackTrace();
        }
    }

    private PropertiesHelper() {
    }

    /**
     * A representation of data stored in a properties file.
     *
     * @param <T> The type of the key
     */
    public static abstract class Key<T> {

        /**
         * Creates a key from the given key and de-serialiser.
         *
         * @param key The key
         * @param serialiser The serialiser
         * @param deserialiser The de-serialiser
         * @param <T> The type of the key
         * @return The key
         */
        public static <T> Key<T> create(final String key,
                                        final Function<T, String> serialiser,
                                        final Function<String, T> deserialiser) {
            return new Key<T>(key) {
                @Override
                public String serialise(final T value) {
                    return serialiser.apply(value);
                }

                @Override
                public T deserialise(final String value) {
                    return deserialiser.apply(value);
                }
            };
        }

        /**
         * Creates a string key from the given key.
         *
         * @param key The key
         * @return The key
         */
        public static Key<String> string(final String key) {
            return create(key, str -> str, str -> str);
        }

        /**
         * Creates an integer key from the given key.
         *
         * @param key The key
         * @return The key
         */
        public static Key<Integer> integer(final String key) {
            return create(key, Object::toString, Integer::parseInt);
        }

        /**
         * Creates a file key from the given key.
         *
         * @param key The key
         * @return The key
         */
        public static Key<File> file(final String key) {
            return create(key, File::toString, File::new);
        }

        private final String key;

        protected Key(final String key) {
            this.key = key;
        }

        /**
         * Gets the key.
         *
         * @return The key
         */
        public String getKey() {
            return this.key;
        }

        /**
         * Serialises the value from the de-serialised form.
         *
         * @param value The de-serialised form
         * @return The serialised form
         */
        public abstract String serialise(final T value);

        /**
         * De-serialises the value from the serialised form.
         *
         * @param value The serialised form
         * @return The de-serialised form
         */
        public abstract T deserialise(final String value);

    }

}
