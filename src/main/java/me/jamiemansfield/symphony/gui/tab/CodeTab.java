//******************************************************************************
// Copyright (c) Jamie Mansfield <https://jamiemansfield.me/>
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//******************************************************************************

package me.jamiemansfield.symphony.gui.tab;

import javafx.scene.control.Tab;
import javafx.scene.control.Tooltip;
import me.jamiemansfield.symphony.Jar;
import org.cadixdev.lorenz.model.ClassMapping;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;

/**
 * A tab used to display the code of a file.
 *
 * @author Jamie Mansfield
 * @since 0.1.0
 */
public class CodeTab extends Tab {

    private final Jar jar;
    private final ClassMapping<?, ?> klass;

    public CodeTab(final Jar jar, final ClassMapping<?, ?> klass) {
        this.jar = jar;
        this.klass = klass;

        this.update();
    }

    public void update() {
        this.setText(this.klass.getSimpleDeobfuscatedName());
        this.setTooltip(new Tooltip(this.klass.getFullDeobfuscatedName()));

        final CodeArea code = new CodeArea(this.jar.decompile(this.klass));
        code.setParagraphGraphicFactory(LineNumberFactory.get(code));
        code.setEditable(false);
        this.setContent(code);
    }

}
