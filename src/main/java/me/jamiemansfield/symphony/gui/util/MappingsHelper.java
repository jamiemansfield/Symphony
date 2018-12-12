//******************************************************************************
// Copyright (c) Jamie Mansfield <https://jamiemansfield.me/>
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//******************************************************************************

package me.jamiemansfield.symphony.gui.util;

import javafx.stage.FileChooser;
import javafx.stage.Window;
import me.jamiemansfield.symphony.util.PropertiesKey;
import me.jamiemansfield.symphony.util.StateHelper;
import org.cadixdev.lorenz.MappingSet;
import org.cadixdev.lorenz.io.MappingFormat;
import org.cadixdev.lorenz.io.MappingFormats;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * A helper class for reading and writing mappings to
 * file, graphically.
 *
 * @author Jamie Mansfield
 * @since 0.1.0
 */
public final class MappingsHelper {

    private static final PropertiesKey<MappingFormat> LAST_FORMAT = PropertiesKey.create(
            "last_mapping_format", MappingFormat::toString, MappingFormats::byId
    );
    private static final PropertiesKey<File> LAST_DIRECTORY = PropertiesKey.file(
            "last_mapping_directory"
    );

    private static final Map<FileChooser.ExtensionFilter, MappingFormat> FORWARD = new HashMap<>();
    private static final Map<MappingFormat, FileChooser.ExtensionFilter> BACKWARD = new HashMap<>();
    private static final FileChooser FILE_CHOOSER = new FileChooser();

    private static void register(final FileChooser.ExtensionFilter filter,
                                 final MappingFormat format) {
        FORWARD.put(filter, format);
        BACKWARD.put(format, filter);
        FILE_CHOOSER.getExtensionFilters().add(filter);
    }

    static {
        // Register the supported mapping formats
        register(Formats.SRG, MappingFormats.SRG);
        register(Formats.CSRG, MappingFormats.CSRG);
        register(Formats.TSRG, MappingFormats.TSRG);

        // Use the last used mapping format
        FILE_CHOOSER.setSelectedExtensionFilter(
                BACKWARD.get(StateHelper.get(LAST_FORMAT).orElse(MappingFormats.SRG))
        );

        // Use the last used directory
        StateHelper.get(LAST_DIRECTORY).ifPresent(FILE_CHOOSER::setInitialDirectory);
    }

    /**
     * Loads mappings to the given {@link MappingSet}, in the {@link Window}.
     *
     * @param window The parent window
     * @param mappings The mappings
     * @return {@code true} if the mappings were read successfully;
     *         {@code false} otherwise
     */
    public static boolean loadMappings(final Window window, final MappingSet mappings) {
        // Setup the file chooser appropriately for the operation being done
        FILE_CHOOSER.setTitle("Load Mappings");

        // Gets the mappings
        final File mappingsPath = FILE_CHOOSER.showOpenDialog(window);
        if (mappingsPath == null) return false;

        // Reads from file
        final MappingFormat format = FORWARD.get(FILE_CHOOSER.getSelectedExtensionFilter());
        try {
            format.read(mappings, mappingsPath.toPath());
        }
        catch (final IOException ex) {
            ex.printStackTrace();
            return false;
        }

        // Update state
        StateHelper.set(LAST_FORMAT, format);
        StateHelper.set(LAST_DIRECTORY, mappingsPath.getParentFile());

        return true;
    }

    /**
     * Saves mappings from the given {@link MappingSet}, in the {@link Window}.
     *
     * @param window The parent window
     * @param mappings The mappings
     * @return {@code true} if the mappings were written successfully;
     *         {@code false} otherwise
     */
    public static boolean saveMappingsAs(final Window window, final MappingSet mappings) {
        // Setup the file chooser appropriately for the operation being done
        FILE_CHOOSER.setTitle("Save Mappings");

        // Gets the mappings
        final File mappingsPath = FILE_CHOOSER.showSaveDialog(window);
        if (mappingsPath == null) return false;

        // Reads from file
        final MappingFormat format = FORWARD.get(FILE_CHOOSER.getSelectedExtensionFilter());
        try {
            format.write(mappings, mappingsPath.toPath());
        }
        catch (final IOException ex) {
            ex.printStackTrace();
            return false;
        }

        // Update state
        StateHelper.set(LAST_FORMAT, format);
        StateHelper.set(LAST_DIRECTORY, mappingsPath.getParentFile());

        return true;
    }

    private MappingsHelper() {
    }

    /**
     * A pseudo-enumeration of {@link FileChooser.ExtensionFilter}s.
     */
    public static final class Formats {

        public static final FileChooser.ExtensionFilter SRG = new FileChooser.ExtensionFilter(
                "SRG Files",
                "*.srg"
        );

        public static final FileChooser.ExtensionFilter CSRG = new FileChooser.ExtensionFilter(
                "CSRG Files",
                "*.csrg"
        );

        public static final FileChooser.ExtensionFilter TSRG = new FileChooser.ExtensionFilter(
                "TSRG Files",
                "*.tsrg"
        );

        private Formats() {
        }

    }

}
