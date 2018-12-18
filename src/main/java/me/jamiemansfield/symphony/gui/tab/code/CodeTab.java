//******************************************************************************
// Copyright (c) Jamie Mansfield <https://jamiemansfield.me/>
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//******************************************************************************

package me.jamiemansfield.symphony.gui.tab.code;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.Separator;
import javafx.scene.control.Tab;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import me.jamiemansfield.symphony.decompiler.Decompiler;
import me.jamiemansfield.symphony.decompiler.Decompilers;
import me.jamiemansfield.symphony.gui.SymphonyMain;
import me.jamiemansfield.symphony.gui.concurrent.TaskManager;
import me.jamiemansfield.symphony.gui.menu.ClassContextMenu;
import me.jamiemansfield.symphony.gui.util.RadioMenuHelper;
import me.jamiemansfield.symphony.gui.util.TextFlowBuilder;
import me.jamiemansfield.symphony.jar.Jar;
import me.jamiemansfield.symphony.util.LocaleHelper;
import org.cadixdev.lorenz.model.TopLevelClassMapping;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;

import java.util.Optional;

/**
 * A tab used to display the code of a file.
 *
 * @author Jamie Mansfield
 * @since 0.1.0
 */
public class CodeTab extends Tab {

    private final SymphonyMain symphony;
    private final TopLevelClassMapping klass;
    private Decompiler decompiler = null;

    private final ContextMenu classMenu;

    public CodeTab(final SymphonyMain symphony, final TopLevelClassMapping klass) {
        this.symphony = symphony;
        this.klass = klass;

        this.classMenu = new ClassContextMenu(symphony, klass);
        {
            final Menu decompilerMenu = RadioMenuHelper.create(
                    new Menu(LocaleHelper.get("tab.code.context_menu.decompiler")),
                    Decompilers.values(),
                    Decompilers.getDefault(),
                    Decompiler::getName,
                    decompiler -> {
                        // Set the decompiler
                        this.decompiler = decompiler;

                        // Update the views
                        this.update();
                    }
            );
            this.classMenu.getItems().add(decompilerMenu);
        }

        this.update();
    }

    public void update() {
        this.setText(this.klass.getSimpleDeobfuscatedName());
        this.setTooltip(new Tooltip(this.klass.getFullDeobfuscatedName()));
        this.setContextMenu(this.classMenu);

        final BorderPane root = new BorderPane();

        // Code display
        final Text notice = new Text(LocaleHelper.get("tab.code.decompiling"));
        notice.setFont(new Font(24));
        root.setCenter(notice);

        final DecompileService decompileService = new DecompileService(
                this.symphony.getJar(),
                this.getDecompiler().orElse(Decompilers.getDefault()),
                this.klass
        );
        decompileService.setOnSucceeded(event -> {
            final CodeArea code = new CodeArea(event.getSource().getValue().toString());
            code.setParagraphGraphicFactory(LineNumberFactory.get(code));
            code.setEditable(false);
            JavaSyntaxHighlighting.highlight(code);
            root.setCenter(code);
        });
        decompileService.start();

        // Bottom tool bar
        final ToolBar bar = new ToolBar();
        bar.getItems().add(TextFlowBuilder.create()
                .label(this.klass.getSimpleDeobfuscatedName())
                    .perform(lbl -> lbl.setTooltip(new Tooltip(this.klass.getFullDeobfuscatedName())))
                    .style("-fx-font-weight: bold")
                .text(" (")
                .label(this.klass.getFullObfuscatedName())
                    .style("-fx-font-style: italic")
                .text(")")
                .contextMenuRequested(flow -> event -> {
                    this.classMenu.show(flow, event.getScreenX(), event.getScreenY());
                })
                .build());
        bar.getItems().add(new Separator());
        root.setBottom(bar);

        this.setContent(root);
    }

    public TopLevelClassMapping getKlass() {
        return this.klass;
    }

    public Optional<Decompiler> getDecompiler() {
        return Optional.ofNullable(this.decompiler);
    }

    private static class DecompileService extends Service<String> {

        private final Jar jar;
        private final Decompiler decompiler;
        private final TopLevelClassMapping klass;

        DecompileService(final Jar jar, final Decompiler decompiler, final TopLevelClassMapping klass) {
            this.jar = jar;
            this.decompiler = decompiler;
            this.klass = klass;
        }

        @Override
        protected Task<String> createTask() {
            return TaskManager.INSTANCE.new TrackedTask<String>() {
                {
                    this.updateTitle("decompile: " + DecompileService.this.klass.getSimpleDeobfuscatedName());
                }
                @Override
                protected String call() {
                    return DecompileService.this.jar.decompile(DecompileService.this.decompiler, DecompileService.this.klass);
                }
            };
        }

    }

}
