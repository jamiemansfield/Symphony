//******************************************************************************
// Copyright (c) Jamie Mansfield <https://jamiemansfield.me/>
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//******************************************************************************

package me.jamiemansfield.symphony.gui.tab;

import javafx.event.ActionEvent;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Separator;
import javafx.scene.control.Tab;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import me.jamiemansfield.symphony.Jar;
import org.cadixdev.lorenz.model.TopLevelClassMapping;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;

/**
 * A tab used to display the code of a file.
 *
 * @author Jamie Mansfield
 * @since 0.1.0
 */
public class CodeTab extends Tab {

    private final Jar jar;
    private final TopLevelClassMapping klass;

    private final ContextMenu classMenu = new ContextMenu() {
        {
            final MenuItem remap = new MenuItem("Set de-obfuscated name");
            remap.addEventHandler(ActionEvent.ACTION, event -> {
                final TextInputDialog dialog = new TextInputDialog();
                dialog.setTitle("Set de-obfuscated name");
                dialog.setHeaderText("Set de-obfuscated name");
                dialog.setContentText("Please enter name:");

                dialog.showAndWait().ifPresent(deobfName -> {
                    // Set the deobf name
                    CodeTab.this.klass.setDeobfuscatedName(deobfName);

                    // Update the view
                    CodeTab.this.update();
                });
            });
            this.getItems().add(remap);

            final MenuItem reset = new MenuItem("Reset de-obfuscated name");
            reset.addEventHandler(ActionEvent.ACTION, event -> {
                // Set the deobf name to the obf name (resetting it)
                CodeTab.this.klass.setDeobfuscatedName(CodeTab.this.klass.getObfuscatedName());

                // Update the view
                CodeTab.this.update();
            });
            this.getItems().add(reset);
        }
    };

    public CodeTab(final Jar jar, final TopLevelClassMapping klass) {
        this.jar = jar;
        this.klass = klass;

        this.update();
    }

    public void update() {
        this.setText(this.klass.getSimpleDeobfuscatedName());
        this.setTooltip(new Tooltip(this.klass.getFullDeobfuscatedName()));

        final BorderPane root = new BorderPane();

        // Code display
        final CodeArea code = new CodeArea(this.jar.decompile(this.klass));
        code.setParagraphGraphicFactory(LineNumberFactory.get(code));
        code.setEditable(false);
        root.setCenter(code);

        // Bottom tool bar
        final ToolBar bar = new ToolBar();
        bar.getItems().add(new TextFlow() {
            {
                final Text deobfName = new Text(CodeTab.this.klass.getSimpleDeobfuscatedName());
                deobfName.setStyle("-fx-font-weight: bold");
                final Text obfName = new Text(CodeTab.this.klass.getFullObfuscatedName());
                obfName.setStyle("-fx-font-style: italic");

                this.getChildren().addAll(
                        deobfName,
                        new Text(" ("),
                        obfName,
                        new Text(")")
                );

                this.setOnContextMenuRequested(event -> {
                    CodeTab.this.classMenu.show(this, event.getScreenX(), event.getScreenY());
                });
            }
        });
        bar.getItems().add(new Separator());
        root.setBottom(bar);

        this.setContent(root);
    }

}
