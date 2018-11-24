//******************************************************************************
// Copyright (c) Jamie Mansfield <https://jamiemansfield.me/>
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//******************************************************************************

package me.jamiemansfield.symphony.source;

import me.jamiemansfield.symphony.decompiler.IDecompiler;

/**
 * A post-decompiler processor, for a given {@link IDecompiler.OutputType}.
 *
 * @author Jamie Mansfield
 * @since 0.1.0
 */
public interface ISourceProcessor {

    /**
     * Processes the given source code.
     *
     * @param source The source code
     * @return The processed output
     */
    String process(final String source);

}
