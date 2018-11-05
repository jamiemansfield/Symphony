//******************************************************************************
// Copyright (c) Jamie Mansfield <https://jamiemansfield.me/>
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//******************************************************************************

package me.jamiemansfield.symphony.decompiler.forgeflower;

import org.jetbrains.java.decompiler.main.extern.IFernflowerLogger;

/**
 * An implementation of {@link IFernflowerLogger} that prints to
 * {@link System#out}.
 *
 * @author Jamie Mansfield
 * @since 0.1.0
 */
class SimpleFernflowerLogger extends IFernflowerLogger {

    public static final IFernflowerLogger INSTANCE = new SimpleFernflowerLogger();

    private SimpleFernflowerLogger() {
    }

    @Override
    public void writeMessage(final String message, final Severity severity) {
        System.out.println(message);
    }

    @Override
    public void writeMessage(final String message, final Severity severity, final Throwable t) {
        System.out.println(message);
        t.printStackTrace();
    }

}
