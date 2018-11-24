//******************************************************************************
// Copyright (c) Jamie Mansfield <https://jamiemansfield.me/>
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//******************************************************************************

package me.jamiemansfield.symphony.decompiler.cfr;

import me.jamiemansfield.symphony.decompiler.AbstractDecompiler;
import me.jamiemansfield.symphony.decompiler.IDecompiler;
import me.jamiemansfield.symphony.decompiler.WrappedBytecode;
import org.benf.cfr.reader.api.CfrDriver;
import org.benf.cfr.reader.api.OutputSinkFactory;
import org.cadixdev.bombe.asm.jar.ClassProvider;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * An implementation of {@link IDecompiler} for CFR.
 *
 * @author Jamie Mansfield
 * @since 0.1.0
 */
public class CfrDecompiler extends AbstractDecompiler {

    private static final String NAME = "CFR";

    @Override
    public String decompile(final ClassProvider classProvider, final WrappedBytecode klass, final WrappedBytecode... innerKlasses) {
        final String name = klass.getName().substring(0, klass.getName().length() - ".class".length());

        final Sink sink = new Sink();
        final CfrDriver driver = new CfrDriver.Builder()
                .withClassFileSource(new ClassProviderClassFileSource(classProvider))
                .withOutputSink(sink)
                .build();
        driver.analyse(Collections.singletonList(name + ".class"));

        return sink.getValue();
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public OutputType getOutputType() {
        return OutputType.JAVA;
    }

    private static final class Sink implements OutputSinkFactory {

        private String value;

        @Override
        public List<SinkClass> getSupportedSinks(final SinkType sinkType, final Collection<SinkClass> collection) {
            return Collections.singletonList(SinkClass.STRING);
        }

        @Override
        public <T> Sink<T> getSink(final SinkType sinkType, final SinkClass sinkClass) {
            return sinkType == SinkType.JAVA ? value -> this.value = value.toString() : value -> {};
        }

        public final String getValue() {
            return this.value;
        }

    }

}
