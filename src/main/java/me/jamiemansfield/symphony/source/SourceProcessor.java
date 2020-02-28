//******************************************************************************
// Copyright (c) Jamie Mansfield <https://jamiemansfield.me/>
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//******************************************************************************

package me.jamiemansfield.symphony.source;

/**
 * A processor to modify source code before it is displayed, specific to
 * a {@link SourceFileType source file type}.
 * <p>
 * Potential uses of a source processor include:
 * <ul>
 *     <li>Run the source code through a code formatter, to have consistently
 *     styled output</li>
 *     <li>Populate a source index of token throughout the source code, to
 *     display elements inline with the code</li>
 * </ul>
 *
 * @author Jamie Mansfield
 * @since 0.1.0
 */
@FunctionalInterface
public interface SourceProcessor {

    /**
     * Creates a single processor from multiple.
     * <p>
     * This will respect the order of the array, running each processor
     * sequentially.
     *
     * @param processors The processors to run
     * @return A single processor
     */
    static SourceProcessor runAll(final SourceProcessor... processors) {
        return source -> {
            for (final SourceProcessor processor : processors) {
                source = processor.process(source);
            }
            return source;
        };
    }

    /**
     * Processes the given <em>raw</em> source code.
     *
     * @param source The raw source code (<strong>input</strong>)
     * @return The processed source code (<strong>output</strong>)
     */
    String process(final String source);

}
