//******************************************************************************
// Copyright (c) Jamie Mansfield <https://jamiemansfield.me/>
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//******************************************************************************

package me.jamiemansfield.symphony.jar.index;

import org.cadixdev.bombe.type.FieldType;

public class IndexedField {

    private final String name;
    private final int access;
    private final FieldType type;

    public IndexedField(final String name, final int access, final FieldType type) {
        this.name = name;
        this.access = access;
        this.type = type;
    }

    public String getName() {
        return this.name;
    }

    public int getAccess() {
        return this.access;
    }

    public FieldType getType() {
        return this.type;
    }

}
