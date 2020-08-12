//******************************************************************************
// Copyright (c) Jamie Mansfield <https://jamiemansfield.me/>
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//******************************************************************************

package me.jamiemansfield.symphony.gui.tab.code;

import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.paint.Color;
import me.jamiemansfield.symphony.gui.Symphony;
import me.jamiemansfield.symphony.gui.concurrent.TaskManager;
import me.jamiemansfield.symphony.gui.menu.PackageContextMenu;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.SelectionImpl;
import spoon.Launcher;

/**
 * A {@link Service service} to decorate a {@link CodeTab code tab}'s
 * view with selectable members.
 *
 * @author Jamie Mansfield
 * @since 0.1.0
 */
public class SourceDecoratorService extends Service<Void> {

    private final Symphony symphony;
    private final CodeArea codeArea;

    public SourceDecoratorService(final Symphony symphony, final CodeArea codeArea) {
        this.symphony = symphony;
        this.codeArea = codeArea;
    }

    @Override
    protected Task<Void> createTask() {
        return TaskManager.INSTANCE.new TrackedTask<>() {
            @Override
            protected Void call() throws Exception {
                SourceDecoratorService.this.decorate();
                return null;
            }
        };
    }

    /**
     * Decorates the code tab's view with selectable members.
     */
    protected void decorate() {
        final var klass = Launcher.parseClass(this.codeArea.getText());

        if (!klass.getPackage().isUnnamedPackage()) {
            Platform.runLater(() -> {
                final var packageDeclaration = klass.getPosition().getCompilationUnit().getPackageDeclaration();
                final var menu = new PackageContextMenu(this.symphony,
                        klass.getPackage().getQualifiedName().replace('.', '/')
                );

                final var selection = new SelectionImpl<>("demo", this.codeArea, path -> {
                    path.setHighlightFill(Color.YELLOW);
                });
                this.codeArea.addSelection(selection);

                final int start = packageDeclaration.getPosition().getSourceStart() + "package ".length();
                final int end = packageDeclaration.getPosition().getSourceEnd();
                selection.selectRange(start, end);

//                Nodes.addInputMap(this.codeArea, InputMap.consume(EventPattern.mouseClicked(MouseButton.SECONDARY), mouseEvent -> {
//                    menu.show(this.codeArea, mouseEvent.getScreenX(), mouseEvent.getScreenY());
//                }));
            });
        }
    }

}
