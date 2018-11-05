//******************************************************************************
// Copyright (c) Jamie Mansfield <https://jamiemansfield.me/>
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//******************************************************************************

package me.jamiemansfield.symphony.decompiler.forgeflower;

import org.cadixdev.bombe.asm.jar.ClassProvider;
import org.jetbrains.java.decompiler.main.extern.IBytecodeProvider;

/**
 * An implementation of {@link IBytecodeProvider} backed by a
 * {@link ClassProvider}.
 *
 * @author Jamie Mansfield
 * @since 0.1.0
 */
class ClassProviderBytecodeProvider implements IBytecodeProvider {

    private final ClassProvider provider;

    ClassProviderBytecodeProvider(final ClassProvider provider) {
        this.provider = provider;
    }

    @Override
    public byte[] getBytecode(final String externalPath, final String internalPath) {
        return this.provider.get(internalPath);
    }

}
