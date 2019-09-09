//******************************************************************************
// Copyright (c) Jamie Mansfield <https://jamiemansfield.me/>
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//******************************************************************************

package me.jamiemansfield.symphony.gui.tree.view;

import me.jamiemansfield.symphony.gui.SymphonyMain;
import me.jamiemansfield.symphony.jar.Jar;

import java.util.HashSet;
import java.util.Set;

public class SplitClassesPane extends ClassesPane {

    private final ClassesTreeView obfView;
    private final ClassesTreeView deobfView;

    public SplitClassesPane(final SymphonyMain symphony) {
        super(symphony);
        this.obfView = new ClassesTreeView(symphony, ClassesTreeView.ClassesView.OBFUSCATED);
        this.deobfView = new ClassesTreeView(symphony, ClassesTreeView.ClassesView.DEOBFUSCATED);
        this.setTop(this.obfView);
        this.setBottom(this.deobfView);
    }

    @Override
    public Set<String> clear() {
        final Set<String> expanded = new HashSet<>();
        expanded.addAll(this.obfView.clear());
        expanded.addAll(this.deobfView.clear());
        return expanded;
    }

    @Override
    public void initialise(final Jar jar, final Set<String> expanded) {
        this.obfView.initialise(jar, expanded);
        this.deobfView.initialise(jar, expanded);
    }

}
