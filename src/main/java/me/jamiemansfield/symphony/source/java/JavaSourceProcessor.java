//******************************************************************************
// Copyright (c) Jamie Mansfield <https://jamiemansfield.me/>
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//******************************************************************************

package me.jamiemansfield.symphony.source.java;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.printer.PrettyPrinter;
import com.github.javaparser.printer.PrettyPrinterConfiguration;
import me.jamiemansfield.symphony.source.ISourceProcessor;

/**
 * The Java post-decompile processor.
 *
 * @author Jamie Mansfield
 * @since 0.1.0
 */
public class JavaSourceProcessor implements ISourceProcessor {

    @Override
    public String process(final String source) {
        final CompilationUnit unit = JavaParser.parse(source);
        return new PrettyPrinter(new PrettyPrinterConfiguration()
                .setIndentSize(4).setIndentType(PrettyPrinterConfiguration.IndentType.SPACES)
                .setMaxEnumConstantsToAlignHorizontally(0)
                .setOrderImports(true)
                .setVisitorFactory(JavaSourceVisitor::new)
        ).print(unit);
    }

}
