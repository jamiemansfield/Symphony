//******************************************************************************
// Copyright (c) Jamie Mansfield <https://jamiemansfield.me/>
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//******************************************************************************

package me.jamiemansfield.symphony.jar.io;

import org.cadixdev.bombe.asm.jar.ClassProvider;
import org.cadixdev.bombe.jar.JarClassEntry;

/**
 * An implementation of {@link ClassProvider} backed by a
 * {@link JarFile}.
 *
 * @author Jamie Mansfield
 * @since 0.1.0
 */
public class JarFileClassProvider implements ClassProvider {

    private final JarFile jar;

    public JarFileClassProvider(final JarFile jar) {
        this.jar = jar;
    }

    @Override
    public byte[] get(final String klass) {
        final JarClassEntry entry = this.jar.getClass(klass);
        return entry == null ? null : entry.getContents();
    }

}
