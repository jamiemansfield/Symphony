//******************************************************************************
// Copyright (c) Jamie Mansfield <https://jamiemansfield.me/>
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//******************************************************************************

package me.jamiemansfield.symphony.gui.tree.view;

import me.jamiemansfield.symphony.gui.SymphonyMain;
import me.jamiemansfield.symphony.jar.Jar;

import java.util.Set;

public class MergedClassesPane extends ClassesPane {

    private final ClassesTreeView view;

    public MergedClassesPane(final SymphonyMain symphony) {
        super(symphony);
        this.view = new ClassesTreeView(symphony, ClassesTreeView.ClassesView.ALL);
        this.setCenter(this.view);
    }

    @Override
    public Set<String> clear() {
        return this.view.clear();
    }

    @Override
    public void initialise(final Jar jar, final Set<String> expanded) {
        this.view.initialise(jar, expanded);
    }

}
