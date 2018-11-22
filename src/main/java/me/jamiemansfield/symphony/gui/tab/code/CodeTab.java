//******************************************************************************
// Copyright (c) Jamie Mansfield <https://jamiemansfield.me/>
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//******************************************************************************

package me.jamiemansfield.symphony.gui.tab.code;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.printer.PrettyPrintVisitor;
import com.github.javaparser.printer.PrettyPrinter;
import com.github.javaparser.printer.PrettyPrinterConfiguration;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Separator;
import javafx.scene.control.Tab;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import me.jamiemansfield.symphony.SharedConstants;
import me.jamiemansfield.symphony.gui.JavaSyntaxHighlighting;
import me.jamiemansfield.symphony.gui.SymphonyMain;
import me.jamiemansfield.symphony.gui.concurrent.TaskManager;
import me.jamiemansfield.symphony.jar.Jar;
import org.cadixdev.lorenz.model.TopLevelClassMapping;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        final Text notice = new Text("Decompiling...");
        notice.setFont(new Font(24));
        root.setCenter(notice);

        final DecompileService decompileService = new DecompileService(this.jar, this.klass);
        decompileService.setOnSucceeded(event -> {
            final CompilationUnit compilationUnit = JavaParser.parse(event.getSource().getValue().toString());
            final String source = new PrettyPrinter(new PrettyPrinterConfiguration()
                    .setIndentSize(4).setIndentType(PrettyPrinterConfiguration.IndentType.SPACES)
                    .setMaxEnumConstantsToAlignHorizontally(0)
                    .setOrderImports(true)
                    .setVisitorFactory(Visitor::new)
            ).print(compilationUnit);

            final CodeArea code = new CodeArea();

            final List<Node> nodes = new ArrayList<>();
            nodes.add(new Text(source));

            this.parseItems(nodes, SharedConstants.Processing.MEMBER_REGEX, 2);

            final TextFlow flow = new TextFlow(nodes.toArray(new Node[0]));

            code.setParagraphGraphicFactory(LineNumberFactory.get(code));
            code.setEditable(false);
            JavaSyntaxHighlighting.highlight(code);
            root.setCenter(flow);
        });
        decompileService.start();

        // Bottom tool bar
        final ToolBar bar = new ToolBar();
        bar.getItems().add(new TextFlow() {
            {
                final Label deobfName = new Label(CodeTab.this.klass.getSimpleDeobfuscatedName());
                deobfName.setTooltip(new Tooltip(CodeTab.this.klass.getFullDeobfuscatedName()));
                deobfName.setStyle("-fx-font-weight: bold");
                final Label obfName = new Label(CodeTab.this.klass.getFullObfuscatedName());
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

    public void parseItems(List<Node> nodes, Pattern pattern, int defaultGroup) {
        final List<Node> newNodes = new ArrayList<>();

        for (final Node node : nodes) {
            if (node.getClass() != Text.class) {
                newNodes.add(node);
                continue;
            }

            String str = ((Text) node).getText();
            Matcher matcher = pattern.matcher(str);
            int lastIndex = 0;
            while (matcher.find()) {
                newNodes.add(new Text(str.substring(lastIndex, matcher.start())));
                newNodes.add(new SelectableMember(matcher.group(defaultGroup)));
                lastIndex = matcher.end();
            }
            newNodes.add(new Text(str.substring(lastIndex)));
        }

        nodes.clear();
        nodes.addAll(newNodes);
    }

    private static class Visitor extends PrettyPrintVisitor {

        public Visitor(final PrettyPrinterConfiguration prettyPrinterConfiguration) {
            super(prettyPrinterConfiguration);
        }

        @Override
        public void visit(final FieldAccessExpr n, final Void arg) {
            n.setName(new SimpleName(SharedConstants.Processing.MEMBER_PREFIX + "fake-fake-" + n.getName().asString() + SharedConstants.Processing.MEMBER_SUFFIX));
            super.visit(n, arg);
        }

    }

    private static class DecompileService extends Service<String> {

        private final Jar jar;
        private final TopLevelClassMapping klass;

        DecompileService(final Jar jar, final TopLevelClassMapping klass) {
            this.jar = jar;
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
                    return DecompileService.this.jar.decompile(SymphonyMain.decompiler(), DecompileService.this.klass);
                }
            };
        }

    }

}
