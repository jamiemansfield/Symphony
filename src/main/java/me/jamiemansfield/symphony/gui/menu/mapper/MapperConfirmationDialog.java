//******************************************************************************
// Copyright (c) Jamie Mansfield <https://jamiemansfield.me/>
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//******************************************************************************

package me.jamiemansfield.symphony.gui.menu.mapper;

import javafx.geometry.Pos;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.layout.GridPane;
import me.jamiemansfield.symphony.util.PairedBoolean;

import java.util.Objects;

public class MapperConfirmationDialog<C> extends Dialog<PairedBoolean<C>> {

    private final C config;
    private final GridPane grid;

    public MapperConfirmationDialog(final C initial, final String title) {
        this.config = initial;

        // Setup dialog
        this.setTitle(title);
        this.getDialogPane().getButtonTypes().addAll(ButtonType.APPLY, ButtonType.CANCEL);

        this.grid = new GridPane();
        this.grid.setHgap(10);
        this.grid.setMaxWidth(Double.MAX_VALUE);
        this.grid.setAlignment(Pos.CENTER_LEFT);
        this.getDialogPane().setContent(this.grid);

        this.setResultConverter(param -> {
            if (Objects.equals(param, ButtonType.APPLY)) {
                return new PairedBoolean<>(this.config, true);
            }
            else {
                return new PairedBoolean<>(null, false);
            }
        });
    }

}
