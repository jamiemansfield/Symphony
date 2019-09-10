//******************************************************************************
// Copyright (c) Jamie Mansfield <https://jamiemansfield.me/>
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//******************************************************************************

package me.jamiemansfield.symphony.gui.dialog;

import javafx.scene.control.TextInputDialog;
import me.jamiemansfield.symphony.util.LocaleHelper;

/**
 * A {@link TextInputDialog input dialog} used for remapping packages
 * and classes within Symphony.
 *
 * @author Jamie Mansfield
 * @since 0.1.0
 */
public class RemappingInputDialog extends TextInputDialog {

    public RemappingInputDialog() {
        this("");
    }

    public RemappingInputDialog(final String defaultValue) {
        super(defaultValue);
        this.setTitle(LocaleHelper.get("dialog.remap.title"));
        this.setHeaderText(LocaleHelper.get("dialog.remap.title"));
        this.setContentText(LocaleHelper.get("dialog.remap.message"));

        this.setResizable(true);
        this.getDialogPane().setMinWidth(400);
        this.getDialogPane().setPrefWidth(500);

        // TODO: Select appropriate part of text
    }

}
