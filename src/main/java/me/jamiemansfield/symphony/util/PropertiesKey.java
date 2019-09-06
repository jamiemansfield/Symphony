//******************************************************************************
// Copyright (c) Jamie Mansfield <https://jamiemansfield.me/>
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//******************************************************************************

package me.jamiemansfield.symphony.util;

import java.io.File;
import java.util.Optional;
import java.util.Properties;
import java.util.function.Function;

/**
 * A representation of data stored in a properties file.
 *
 * @param <T> The type of the key
 * @author Jamie Mansfield
 * @since 0.1.0
 */
public abstract class PropertiesKey<T> {

    /**
     * Creates a key from the given key and de-serialiser.
     *
     * @param key The key
     * @param serialiser The serialiser
     * @param deserialiser The de-serialiser
     * @param <T> The type of the key
     * @return The key
     */
    public static <T> PropertiesKey<T> create(final String key,
                                              final Function<T, String> serialiser,
                                              final Function<String, T> deserialiser) {
        return new PropertiesKey<T>(key) {
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
    public static PropertiesKey<String> string(final String key) {
        return create(key, str -> str, str -> str);
    }

    /**
     * Creates an integer key from the given key.
     *
     * @param key The key
     * @return The key
     */
    public static PropertiesKey<Integer> integer(final String key) {
        return create(key, Object::toString, Integer::parseInt);
    }

    /**
     * Creates a file key from the given key.
     *
     * @param key The key
     * @return The key
     */
    public static PropertiesKey<File> file(final String key) {
        return create(key, File::toString, File::new);
    }

    private final String key;

    protected PropertiesKey(final String key) {
        this.key = key;
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

    /**
     * Gets the value, if available, of the given key.
     *
     * @param properties The properties
     * @return The value, wrapped in an {@link Optional}
     */
    public Optional<T> get(final Properties properties) {
        final String value = properties.getProperty(this.key);
        if (value == null) return Optional.empty();
        return Optional.ofNullable(this.deserialise(value));
    }

    /**
     * Sets the value of the given key.
     *
     * @param properties The properties
     * @param value The value
     */
    public void set(final Properties properties, final T value) {
        properties.setProperty(this.key, this.serialise(value));
    }

}
