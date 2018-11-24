//******************************************************************************
// Copyright (c) Jamie Mansfield <https://jamiemansfield.me/>
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//******************************************************************************

package me.jamiemansfield.symphony.gui.util;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * A builder for creating {@link TextFlow}s.
 *
 * @param <T> The type of the current node
 */
public class TextFlowBuilder<T extends Node> {

    /**
     * Creates a new text flow builder.
     *
     * @return A builder
     */
    public static TextFlowBuilder<Node> create() {
        return new TextFlowBuilder<>(new TextFlow(), null);
    }

    private final TextFlow textFlow;
    private final T current;

    private TextFlowBuilder(final TextFlow flow, final T current) {
        this.textFlow = flow;
        this.current = current;
    }

    /**
     * Appends the given {@link Node} to the text flow.
     *
     * @param node The node
     * @param <N> The type of the node
     * @return A builder
     */
    public <N extends Node> TextFlowBuilder<N> append(final N node) {
        this.textFlow.getChildren().add(node);
        return new TextFlowBuilder<>(this.textFlow, node);
    }

    /**
     * Appends a {@link Text} node, of the given value.
     *
     * @param text The text
     * @return A builder
     */
    public TextFlowBuilder<Text> text(final String text) {
        return this.append(new Text(text));
    }

    /**
     * Appends a {@link Text} node, of {@code "\n"}.
     *
     * @return A builder
     */
    public TextFlowBuilder<Text> newline() {
        return this.text("\n");
    }

    /**
     * Appends a {@link Label} node, of the given value.
     *
     * @param text The text
     * @return A builder
     */
    public TextFlowBuilder<Label> label(final String text) {
        return this.append(new Label(text));
    }

    /**
     * Performs the given action of the current node.
     *
     * @param consumer The action
     * @return The builder
     */
    public TextFlowBuilder<T> perform(final Consumer<T> consumer) {
        consumer.accept(this.current);
        return this;
    }

    /**
     * Sets the style of the current node.
     *
     * @param style The style to apply
     * @return The builder
     */
    public TextFlowBuilder<T> style(final String style) {
        this.current.setStyle(style);
        return this;
    }

    /**
     * Set the context menu requested event handler for the text flow.
     *
     * @param value The event handler
     * @return The builder
     */
    public TextFlowBuilder<T> contextMenuRequested(final Function<TextFlow, EventHandler<? super ContextMenuEvent>> value) {
        this.textFlow.setOnContextMenuRequested(value.apply(this.textFlow));
        return this;
    }

    /**
     * Gets the underlying text flow.
     *
     * @return The text flow
     */
    public TextFlow build() {
        return this.textFlow;
    }

}
