//******************************************************************************
// Copyright (c) Jamie Mansfield <https://jamiemansfield.me/>
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//******************************************************************************

package me.jamiemansfield.symphony.gui;

import javafx.event.ActionEvent;
import javafx.scene.control.TabPane;
import me.jamiemansfield.symphony.gui.control.WelcomeTab;

public class SymphonyController {

    public TabPane tabs;

    public void openWelcomeTab(final ActionEvent event) {
        this.tabs.getSelectionModel().select(this.tabs.getTabs().stream()
                .filter(tab -> tab instanceof WelcomeTab)
                .findFirst()
                .orElseGet(() -> {
                    final WelcomeTab tab = new WelcomeTab();
                    this.tabs.getTabs().add(tab);
                    return tab;
                }));
    }

    public void onQuit(final ActionEvent event) {
        System.exit(0);
    }

}
