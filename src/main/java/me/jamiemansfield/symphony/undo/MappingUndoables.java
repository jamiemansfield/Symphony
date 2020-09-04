//******************************************************************************
// Copyright (c) Jamie Mansfield <https://jamiemansfield.me/>
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//******************************************************************************

package me.jamiemansfield.symphony.undo;

import org.cadixdev.lorenz.model.Mapping;

public final class MappingUndoables {

    public static Undoable remapMapping(final Mapping<?, ?> mapping, final String deobf) {
        // Store original deobf value
        final String originalDeobf = mapping.getDeobfuscatedName();
        return new Undoable() {
            @Override
            public String getTitle() {
                return "Remapping " + originalDeobf;
            }

            @Override
            public void apply() {
                mapping.setDeobfuscatedName(deobf);
            }

            @Override
            public void unapply() {
                mapping.setDeobfuscatedName(originalDeobf);
            }
        };
    }

    private MappingUndoables() {
    }

}
