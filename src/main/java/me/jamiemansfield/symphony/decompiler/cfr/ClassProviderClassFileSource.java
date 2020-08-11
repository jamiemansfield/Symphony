//******************************************************************************
// Copyright (c) Jamie Mansfield <https://jamiemansfield.me/>
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//******************************************************************************

package me.jamiemansfield.symphony.decompiler.cfr;

import org.benf.cfr.reader.api.ClassFileSource;
import org.benf.cfr.reader.bytecode.analysis.parse.utils.Pair;
import org.cadixdev.bombe.jar.ClassProvider;

import java.io.IOException;
import java.util.Collection;

/**
 * An implementation of {@link ClassFileSource}, backed by
 * a {@link ClassProvider}.
 *
 * @author Jamie Mansfield
 * @since 0.1.0
 */
class ClassProviderClassFileSource implements ClassFileSource {

    private final ClassProvider provider;

    ClassProviderClassFileSource(final ClassProvider provider) {
        this.provider = provider;
    }

    @Override
    public void informAnalysisRelativePathDetail(final String usePath, final String classFilePath) {
    }

    @Override
    public Collection<String> addJar(final String jarPath) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getPossiblyRenamedPath(final String path) {
        return path;
    }

    @Override
    public Pair<byte[], String> getClassFileContent(final String path) throws IOException {
        final String name = path.substring(0, path.length() - ".class".length());
        return new Pair<>(this.provider.get(name), path);
    }

}
