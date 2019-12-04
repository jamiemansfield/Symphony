//******************************************************************************
// Copyright (c) Jamie Mansfield <https://jamiemansfield.me/>
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//******************************************************************************

package me.jamiemansfield.symphony.jar.index;

import org.cadixdev.bombe.type.MethodDescriptor;

public class IndexedMethod {

    private final String name;
    private final int access;
    private final MethodDescriptor descriptor;

    public IndexedMethod(final String name, final int access, final MethodDescriptor descriptor) {
        this.name = name;
        this.access = access;
        this.descriptor = descriptor;
    }

    public String getName() {
        return this.name;
    }

    public int getAccess() {
        return this.access;
    }

    public MethodDescriptor getDescriptor() {
        return this.descriptor;
    }

}
