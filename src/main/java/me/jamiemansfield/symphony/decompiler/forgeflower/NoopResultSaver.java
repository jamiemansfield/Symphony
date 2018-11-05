//******************************************************************************
// Copyright (c) Jamie Mansfield <https://jamiemansfield.me/>
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//******************************************************************************

package me.jamiemansfield.symphony.decompiler.forgeflower;

import org.jetbrains.java.decompiler.main.extern.IResultSaver;

import java.util.jar.Manifest;

/**
 * An implementation of {@link IResultSaver} that doesn't save anywhere.
 *
 * @author Jamie Mansfield
 * @since 0.1.0
 */
class NoopResultSaver implements IResultSaver {

    public static final IResultSaver INSTANCE = new NoopResultSaver();

    private NoopResultSaver() {
    }

    @Override
    public void saveFolder(final String path) {
    }

    @Override
    public void copyFile(final String source, final String path, final String entryName) {
    }

    @Override
    public void saveClassFile(final String path, final String qualifiedName, final String entryName, final String content, final int[] mapping) {
    }

    @Override
    public void createArchive(final String path, final String archiveName, final Manifest manifest) {
    }

    @Override
    public void saveDirEntry(final String path, final String archiveName, final String entryName) {
    }

    @Override
    public void copyEntry(final String source, final String path, final String archiveName, final String entry) {
    }

    @Override
    public void saveClassEntry(final String path, final String archiveName, final String qualifiedName, final String entryName, final String content) {
    }

    @Override
    public void closeArchive(final String path, final String archiveName) {
    }

}
