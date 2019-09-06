//******************************************************************************
// Copyright (c) Jamie Mansfield <https://jamiemansfield.me/>
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//******************************************************************************

package me.jamiemansfield.symphony.gui.util;

import javafx.scene.control.Alert;
import me.jamiemansfield.symphony.SharedConstants;

/**
 * A helper class for displaying the about window.
 *
 * @author Jamie Mansfield
 * @since 0.1.0
 */
public final class AboutHelper {

    private static final Alert ALERT = new Alert(Alert.AlertType.INFORMATION);

    static {
        ALERT.setTitle("About Symphony");
        ALERT.setHeaderText("Symphony v" + SharedConstants.VERSION);
        ALERT.getDialogPane().setContent(TextFlowBuilder.create()
                .text("Copyright (c) 2018-2019 Jamie Mansfield <https://www.jamiemansfield.me/>").newline().newline()
                // The following is adapted from a similar statement Mozilla make for Firefox
                // See about:rights
                .text("Symphony is made available under the terms of the Mozilla Public License, giving").newline()
                .text("you the freedom to use, copy, and distribute Symphony to others, in addition to").newline()
                .text("the right to distribute modified versions.")
                .build());
    }

    /**
     * Displays the about window.
     */
    public static void display() {
        ALERT.showAndWait();
    }

    private AboutHelper() {
    }

}
