//******************************************************************************
// Copyright (c) Jamie Mansfield <https://jamiemansfield.me/>
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//******************************************************************************

package me.jamiemansfield.symphony.gui.tree.view;

import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import me.jamiemansfield.symphony.gui.SymphonyMain;
import me.jamiemansfield.symphony.gui.tree.ClassElement;
import me.jamiemansfield.symphony.gui.tree.PackageElement;
import me.jamiemansfield.symphony.gui.tree.RootElement;
import me.jamiemansfield.symphony.gui.tree.TreeElement;
import me.jamiemansfield.symphony.gui.util.DisplaySettings;
import me.jamiemansfield.symphony.jar.Jar;
import org.cadixdev.lorenz.model.TopLevelClassMapping;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

/**
 * An extension of {@link TreeView} to display packaged classes.
 *
 * @author Jamie Mansfield
 * @since 0.1.0
 */
// TODO: Search bar
public class ClassesTreeView extends TreeView<TreeElement> {

    private final SymphonyMain symphony;
    private final ClassesView classesView;
    private final TreeItem<TreeElement> treeRoot;

    public ClassesTreeView(final SymphonyMain symphony, final ClassesView classesView) {
        this.symphony = symphony;
        this.classesView = classesView;
        this.setShowRoot(false);
        this.setCellFactory(view -> new SymphonyTreeCell());
        this.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) { // isDoubleClick
                final TreeItem<TreeElement> item = this.getSelectionModel().getSelectedItems().get(0);
                if (item == null) return;
                item.getValue().activate();
            }
        });
        this.treeRoot = new TreeItem<>(new RootElement());
        this.treeRoot.setExpanded(true);
        this.setRoot(this.treeRoot);
    }

    /**
     * Clears the entire classes view.
     *
     * @return The set of expanded packages, prior to clearing
     */
    public Set<String> clear() {
        final Set<String> expanded = this.getExpandedPackages(new HashSet<>(), this.treeRoot);
        this.treeRoot.getChildren().clear();
        return expanded;
    }

    /**
     * Populates the class view with package and class entries.
     *
     * @param jar The jar
     * @param expanded The packages to expand, after initialisation
     */
    public void initialise(final Jar jar, final Set<String> expanded) {
        final Map<String, TreeItem<TreeElement>> packageCache = new HashMap<>();
        jar.classes().stream()
                .filter(name -> !name.contains("$"))
                .map(jar.getMappings()::getOrCreateTopLevelClassMapping)
                .filter(this.classesView)
                .forEach(klass -> this.getPackageItem(packageCache, klass.getDeobfuscatedPackage()).getChildren()
                        .add(new TreeItem<>(new ClassElement(this.symphony, klass))));

        // sort
        packageCache.values().forEach(item -> {
            item.getChildren().setAll(item.getChildren().sorted(Comparator.comparing(TreeItem::getValue)));
        });
        this.treeRoot.getChildren().setAll(this.treeRoot.getChildren().sorted(Comparator.comparing(TreeItem::getValue)));

        // reopen packages
        expanded.forEach(pkg -> {
            final TreeItem<TreeElement> packageItem = packageCache.get(pkg);
            if (packageItem == null) return;
            packageItem.setExpanded(true);
        });
    }

    /**
     * Populates the class view with package and class entries.
     *
     * @param jar The jar
     */
    public void initialise(final Jar jar) {
        this.initialise(jar, Collections.emptySet());
    }

    private TreeItem<TreeElement> getPackageItem(final Map<String, TreeItem<TreeElement>> cache, final String packageName) {
        if (packageName.isEmpty()) return this.treeRoot;
        if (cache.containsKey(packageName)) return cache.get(packageName);

        final TreeItem<TreeElement> parent;
        if (packageName.lastIndexOf('/') != -1 && !DisplaySettings.flattenPackages()) {
            parent = this.getPackageItem(cache, packageName.substring(0, packageName.lastIndexOf('/')));
        }
        else {
            parent = this.treeRoot;
        }
        final TreeItem<TreeElement> packageItem = new TreeItem<>(new PackageElement(this.symphony, packageName));
        parent.getChildren().add(packageItem);
        cache.put(packageName, packageItem);
        return packageItem;
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
    public enum ClassesView implements Predicate<TopLevelClassMapping> {

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
