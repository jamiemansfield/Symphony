//******************************************************************************
// Copyright (c) Jamie Mansfield <https://jamiemansfield.me/>
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//******************************************************************************

package me.jamiemansfield.symphony.gui.menu.mapper;

import me.jamiemansfield.symphony.util.LocaleHelper;
import org.cadixdev.survey.mapper.config.EnumConstantsMapperConfig;

public class EnumConstantsMapperConfirmationDialog extends MapperConfirmationDialog<EnumConstantsMapperConfig> {

    public EnumConstantsMapperConfirmationDialog() {
        super(new EnumConstantsMapperConfig(), LocaleHelper.get("dialog.mapper.enum_constants.title"));
    }

}
