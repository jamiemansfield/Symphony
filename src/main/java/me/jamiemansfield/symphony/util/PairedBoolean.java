//******************************************************************************
// Copyright (c) Jamie Mansfield <https://jamiemansfield.me/>
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//******************************************************************************

package me.jamiemansfield.symphony.util;

public class PairedBoolean<T> {

    private final T value;
    private final boolean state;

    public PairedBoolean(final T value, final boolean state) {
        this.value = value;
        this.state = state;
    }

    public T getValue() {
        return this.value;
    }

    public boolean getState() {
        return this.state;
    }

}
