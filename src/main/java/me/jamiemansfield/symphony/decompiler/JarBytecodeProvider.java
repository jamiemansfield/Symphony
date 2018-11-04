//******************************************************************************
// Copyright (c) Jamie Mansfield <https://jamiemansfield.me/>
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//******************************************************************************

package me.jamiemansfield.symphony.decompiler;

import me.jamiemansfield.symphony.Jar;
import org.jetbrains.java.decompiler.main.extern.IBytecodeProvider;

/**
 * An implementation of {@link IBytecodeProvider} fetching from
 * a {@link Jar}.
 *
 * @author Jamie Mansfield
 * @since 0.1.0
 */
public class JarBytecodeProvider implements IBytecodeProvider {

    private final Jar jar;

    public JarBytecodeProvider(final Jar jar) {
        this.jar = jar;
    }

    @Override
    public byte[] getBytecode(final String externalPath, final String internalPath) {
        return this.jar.getDeobfuscated(this.jar.getMappings().getOrCreateClassMapping(internalPath));
    }

}
