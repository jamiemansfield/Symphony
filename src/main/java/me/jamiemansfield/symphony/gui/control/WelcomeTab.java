//******************************************************************************
// Copyright (c) Jamie Mansfield <https://jamiemansfield.me/>
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//******************************************************************************

package me.jamiemansfield.symphony.gui.control;

import javafx.scene.control.Tab;
import javafx.scene.text.Text;

/**
 * A simple welcome tab to display when a user starts Symphony.
 */
public class WelcomeTab extends Tab {

    public WelcomeTab() {
        super("Welcome");

        final Text welcome = new Text("Welcome to Symphony...");
        this.setContent(welcome);
    }

}
