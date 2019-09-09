//******************************************************************************
// Copyright (c) Jamie Mansfield <https://jamiemansfield.me/>
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//******************************************************************************

package me.jamiemansfield.symphony.gui.tree.view;

import javafx.scene.layout.BorderPane;
import me.jamiemansfield.symphony.gui.SymphonyMain;
import me.jamiemansfield.symphony.jar.Jar;

import java.util.Collections;
import java.util.Set;

public abstract class ClassesPane extends BorderPane {

    protected final SymphonyMain symphony;

    public ClassesPane(final SymphonyMain symphony) {
        this.symphony = symphony;
    }

    /**
     * Clears the entire classes view.
     *
     * @return The set of expanded packages, prior to clearing
     */
    public abstract Set<String> clear();

    /**
     * Populates the class view with package and class entries.
     *
     * @param jar The jar
     * @param expanded The packages to expand, after initialisation
     */
    public abstract void initialise(final Jar jar, final Set<String> expanded);

    /**
     * Populates the class view with package and class entries.
     *
     * @param jar The jar
     */
    public void initialise(final Jar jar) {
        this.initialise(jar, Collections.emptySet());
    }

}
