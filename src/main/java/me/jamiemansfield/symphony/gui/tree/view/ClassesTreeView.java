//******************************************************************************
// Copyright (c) Jamie Mansfield <https://jamiemansfield.me/>
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//******************************************************************************

package me.jamiemansfield.symphony.gui.tree.view;

import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.KeyCode;
import me.jamiemansfield.symphony.gui.SymphonyMain;
import me.jamiemansfield.symphony.gui.tree.ClassElement;
import me.jamiemansfield.symphony.gui.tree.PackageElement;
import me.jamiemansfield.symphony.gui.tree.RootElement;
import me.jamiemansfield.symphony.gui.tree.TreeElement;
import me.jamiemansfield.symphony.gui.util.DisplaySettings;
import me.jamiemansfield.symphony.jar.Jar;
import me.jamiemansfield.symphony.jar.hierarchy.ClassHierarchyNode;
import me.jamiemansfield.symphony.jar.hierarchy.Hierarchy;
import me.jamiemansfield.symphony.jar.hierarchy.HierarchyNode;
import me.jamiemansfield.symphony.jar.hierarchy.PackageHierarchyNode;
import org.cadixdev.lorenz.model.TopLevelClassMapping;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * An extension of {@link TreeView} to display packaged classes.
 *
 * @author Jamie Mansfield
 * @since 0.1.0
 */
// TODO: Search bar
public class ClassesTreeView extends TreeView<TreeElement> {

    private final SymphonyMain symphony;
    private final View view;
    private final TreeItem<TreeElement> root;

    public ClassesTreeView(final SymphonyMain symphony, final View classesView) {
        this.symphony = symphony;
        this.view = classesView;
        this.setShowRoot(false);
        this.setCellFactory(view -> new SymphonyTreeCell());
        this.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) { // isDoubleClick
                this.open();
            }
        });
        this.setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                this.open();
            }
        });
        this.root = new TreeItem<>(new RootElement());
        this.root.setExpanded(true);
        this.setRoot(this.root);
    }

    /**
     * Opens/expands the currently selected class/package.
     */
    private void open() {
        final TreeItem<TreeElement> item = this.getSelectionModel().getSelectedItems().get(0);
        if (item == null) return;

        // Expand packages
        if (item.getValue() instanceof PackageElement) {
            item.setExpanded(!item.isExpanded());
        }
        // Open classes
        else {
            item.getValue().activate();
        }
    }

    /**
     * Clears the entire classes view.
     *
     * @return The set of expanded packages, prior to clearing
     */
    public Set<String> clear() {
        final Set<String> expanded = this.getExpandedPackages(new HashSet<>(), this.root);
        this.root.getChildren().clear();
        return expanded;
    }

    /**
     * Populates the class view with package and class entries.
     *
     * @param jar The jar
     * @param expanded The packages to expand, after initialisation
     */
    public void initialise(final Jar jar, final Set<String> expanded) {
        final Hierarchy hierarchy = Hierarchy.create(jar.classes().stream()
                .filter(name -> !name.contains("$"))
                .map(jar.getMappings()::getOrCreateTopLevelClassMapping)
                .filter(this.view)
                .collect(Collectors.toSet()));

        this.root.getChildren().addAll(hierarchy.getChildren().stream()
                .map(node -> this.generateTreeItem(node, expanded)).collect(Collectors.toSet()));
        this.root.getChildren().setAll(this.root.getChildren().sorted(Comparator.comparing(TreeItem::getValue)));
    }

    /**
     * Populates the class view with package and class entries.
     *
     * @param jar The jar
     */
    public void initialise(final Jar jar) {
        this.initialise(jar, Collections.emptySet());
    }

    private TreeItem<TreeElement> generateTreeItem(final HierarchyNode node, final Set<String> expanded) {
        final TreeItem<TreeElement> item;
        if (node instanceof PackageHierarchyNode) {
            PackageHierarchyNode n = (PackageHierarchyNode) node;
            final StringBuilder builder = new StringBuilder()
                    .append(n.getSimpleName());

            while (DisplaySettings.compactMiddlePackages() &&
                    !DisplaySettings.flattenPackages() &&
                    n.getChildren().size() == 1 &&
                    n.getChildren().get(0) instanceof PackageHierarchyNode) {
                n = (PackageHierarchyNode) n.getChildren().get(0);
                builder.append('.').append(n.getSimpleName());
            }

            item = new TreeItem<>(new PackageElement(this.symphony, n.getName(), builder.toString()));

            item.getChildren().addAll(n.getChildren().stream()
                    .map(e -> this.generateTreeItem(e, expanded)).collect(Collectors.toList()));
            item.getChildren().setAll(item.getChildren().sorted(Comparator.comparing(TreeItem::getValue)));
        }
        else if (node instanceof ClassHierarchyNode) {
            item = new TreeItem<>(new ClassElement(this.symphony, ((ClassHierarchyNode) node).getKlass()));
        }
        else {
            item = this.root;
        }

        if (expanded.contains(node.getName())) {
            item.setExpanded(true);
        }

        return item;
    }

    private Set<String> getExpandedPackages(final Set<String> packages, final TreeItem<TreeElement> item) {
        item.getChildren().filtered(TreeItem::isExpanded).forEach(pkg -> {
            this.getExpandedPackages(packages, pkg);
            if (pkg.getValue() instanceof PackageElement) {
                packages.add(((PackageElement) pkg.getValue()).getName());
            }
        });
        return packages;
    }

    /**
     * A representation of the classes to display.
     */
    public enum View implements Predicate<TopLevelClassMapping> {

        /**
         * Display all the classes in the JAR file.
         */
        ALL {
            @Override
            public boolean test(final TopLevelClassMapping topLevelClassMapping) {
                return true;
            }
        },

        /**
         * Only display classes in the JAR file, of which names haven't been
         * de-obfuscated.
         */
        OBFUSCATED {
            @Override
            public boolean test(final TopLevelClassMapping topLevelClassMapping) {
                return !topLevelClassMapping.hasDeobfuscatedName();
            }
        },

        /**
         * Only display classes in the JAR file, of which names have been
         * de-obfuscated.
         */
        DEOBFUSCATED {
            @Override
            public boolean test(final TopLevelClassMapping topLevelClassMapping) {
                return topLevelClassMapping.hasDeobfuscatedName();
            }
        },
        ;

    }

}
