//******************************************************************************
// Copyright (c) Jamie Mansfield <https://jamiemansfield.me/>
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//******************************************************************************

package me.jamiemansfield.symphony.analysis;

import org.objectweb.asm.tree.ClassNode;

/**
 * Represents an object, that can walk through classes and
 * load them into a {@link SourceSet}.
 */
public interface Walker {

    /**
     * Walks through the previously given source, and loads
     * the {@link ClassNode}s into the given {@link SourceSet}.
     *
     * @param sourceSet The source set
     */
    void walk(final SourceSet sourceSet);

}
