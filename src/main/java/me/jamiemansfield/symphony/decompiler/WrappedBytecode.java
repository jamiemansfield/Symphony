//******************************************************************************
// Copyright (c) Jamie Mansfield <https://jamiemansfield.me/>
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//******************************************************************************

package me.jamiemansfield.symphony.decompiler;

/**
 * A wrapper around a class' bytecode, including information already
 * known without having to perform any analysis - such as its name.
 *
 * @author Jamie Mansfield
 * @since 0.1.0
 */
public class WrappedBytecode {

    private final String name;
    private final byte[] bytecode;

    public WrappedBytecode(final String name, final byte[] bytecode) {
        this.name = name;
        this.bytecode = bytecode;
    }

    /**
     * Gets the fully-qualified name of the class.
     *
     * @return The name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Gets the bytecode of the class.
     *
     * @return The bytecode
     */
    public byte[] getBytecode() {
        return this.bytecode;
    }

}
