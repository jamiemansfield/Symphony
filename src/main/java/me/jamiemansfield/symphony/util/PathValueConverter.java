//******************************************************************************
// Copyright (c) Jamie Mansfield <https://jamiemansfield.me/>
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//******************************************************************************

package me.jamiemansfield.symphony.util;

import joptsimple.ValueConverter;

import java.nio.file.Path;
import java.nio.file.Paths;

public final class PathValueConverter implements ValueConverter<Path> {

    public static final PathValueConverter INSTANCE = new PathValueConverter();

    @Override
    public Path convert(final String value) {
        return Paths.get(value);
    }

    @Override
    public Class<Path> valueType() {
        return Path.class;
    }

    @Override
    public String valuePattern() {
        return null;
    }

}