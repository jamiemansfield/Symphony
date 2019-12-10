//******************************************************************************
// Copyright (c) Jamie Mansfield <https://jamiemansfield.me/>
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//******************************************************************************

package me.jamiemansfield.symphony;

import javafx.application.Application;
import me.jamiemansfield.symphony.gui.Symphony;
import me.jamiemansfield.symphony.util.StateHelper;

/**
 * The Main-Class behind Symphony.
 *
 * @author Jamie Mansfield
 * @since 0.1.0
 */
public final class SymphonyMain {

    public static void main(final String[] args) {
        // Setup the environment
        Runtime.getRuntime().addShutdownHook(new Thread(StateHelper::saveAll));

        // Launch Symphony
        Application.launch(Symphony.class, args);
    }

    private SymphonyMain() {
    }

}
