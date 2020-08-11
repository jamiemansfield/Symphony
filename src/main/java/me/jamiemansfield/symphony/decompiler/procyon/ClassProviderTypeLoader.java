//******************************************************************************
// Copyright (c) Jamie Mansfield <https://jamiemansfield.me/>
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//******************************************************************************

package me.jamiemansfield.symphony.decompiler.procyon;

import com.strobel.assembler.metadata.Buffer;
import com.strobel.assembler.metadata.ITypeLoader;
import org.cadixdev.bombe.jar.ClassProvider;

/**
 * An implementation of {@link ITypeLoader} backed by a
 * {@link ClassProvider}.
 *
 * @author Jamie Mansfield
 * @since 0.1.0
 */
class ClassProviderTypeLoader implements ITypeLoader {

    private final ClassProvider provider;

    ClassProviderTypeLoader(final ClassProvider provider) {
        this.provider = provider;
    }

    @Override
    public boolean tryLoadType(final String internalName, final Buffer buffer) {
        final byte[] klass = this.provider.get(internalName);
        if (klass == null) return false;

        buffer.reset(klass.length);
        System.arraycopy(klass, 0, buffer.array(), buffer.position(), klass.length);
        buffer.position(0);

        return true;
    }

}
