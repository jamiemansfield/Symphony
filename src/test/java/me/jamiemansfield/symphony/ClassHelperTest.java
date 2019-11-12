//******************************************************************************
// Copyright (c) Jamie Mansfield <https://jamiemansfield.me/>
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//******************************************************************************

package me.jamiemansfield.symphony;

import static org.junit.jupiter.api.Assertions.assertEquals;

import me.jamiemansfield.symphony.util.ClassHelper;
import org.junit.jupiter.api.Test;

class ClassHelperTest {

    @Test
    void getPackageName() {
        assertEquals("org/cadixdev", ClassHelper.getPackageName("org/cadixdev/Demo"));
        assertEquals("", ClassHelper.getPackageName("Demo"));
    }

    @Test
    void getClassName() {
        assertEquals("Demo", ClassHelper.getClassName("org/cadixdev/Demo"));
        assertEquals("Demo", ClassHelper.getClassName("Demo"));
    }

}
