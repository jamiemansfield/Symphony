//******************************************************************************
// Copyright (c) Jamie Mansfield <https://jamiemansfield.me/>
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//******************************************************************************

package me.jamiemansfield.symphony.gui.menu;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import me.jamiemansfield.symphony.gui.SymphonyMain;

/**
 * The Symphony main menu.
 *
 * @author Jamie Mansfield
 * @since 0.1.0
 */
public class MainMenuBar extends MenuBar {

    private final SymphonyMain symphony;

    // Menus
    public final FileMenu file;
    public final ViewMenu view;
    public final NavigateMenu navigate;
    public final HelpMenu help;

    public MainMenuBar(final SymphonyMain symphony) {
        this.symphony = symphony;

        this.file = this.install(new FileMenu(this));
        this.view = this.install(new ViewMenu(this));
        this.navigate = this.install(new NavigateMenu(this));
        this.help = this.install(new HelpMenu(this));
    }

    public SymphonyMain getSymphony() {
        return this.symphony;
    }

    private <T extends Menu> T install(final T menu) {
        this.getMenus().add(menu);
        return menu;
    }

}
