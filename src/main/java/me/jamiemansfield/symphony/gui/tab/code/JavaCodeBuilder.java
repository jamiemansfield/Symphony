//******************************************************************************
// Copyright (c) Jamie Mansfield <https://jamiemansfield.me/>
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//******************************************************************************

package me.jamiemansfield.symphony.gui.tab.code;

import javafx.scene.Node;
import javafx.scene.text.TextFlow;
import me.jamiemansfield.symphony.gui.util.TextFlowBuilder;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.SimpleName;

/**
 * A code builder that creates a {@link TextFlow} for Java
 * source code.
 *
 * @author Jamie Mansfield
 * @since 0.1.0
 */
public class JavaCodeBuilder extends ASTVisitor {

    private final TextFlowBuilder<Node> builder = TextFlowBuilder.create();

    private final String source;
    private int index = 0;

    public JavaCodeBuilder(final String source) {
        this.source = source;
    }

    @Override
    public boolean visit(final PackageDeclaration node) {
        // Add everything up to this binding
        this.builder.text(this.source.substring(this.index, node.getName().getStartPosition()));
        this.builder.label(node.getName().toString()).perform(lbl -> {
            lbl.setOnMouseClicked(event -> {
                System.out.println("woo");
            });
        });
        this.index = node.getName().getStartPosition() + node.getName().getLength();

        return super.visit(node);
    }

    @Override
    public boolean visit(final SimpleName node) {
        final IBinding binding = node.resolveBinding();
        if (binding != null) {
            // Add everything up to this binding
            this.builder.text(this.source.substring(this.index, node.getStartPosition()));
            this.builder.label(node.getIdentifier()).perform(lbl -> {
                lbl.setOnMouseClicked(event -> {
                    System.out.println("woo");
                });
            });
            this.index = node.getStartPosition() + node.getLength();
        }
        return super.visit(node);
    }

    /**
     * Gets the underlying text flow.
     *
     * @return The text flow
     */
    public TextFlow create() {
        if (this.index != this.source.length()) {
            this.builder.text(this.source.substring(this.index));
        }

        return this.builder.build();
    }

}
