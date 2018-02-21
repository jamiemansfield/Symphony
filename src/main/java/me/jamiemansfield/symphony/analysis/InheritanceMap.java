//******************************************************************************
// Copyright (c) Jamie Mansfield <https://jamiemansfield.me/>
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//******************************************************************************

package me.jamiemansfield.symphony.analysis;

import me.jamiemansfield.lorenz.model.jar.MethodDescriptor;
import org.objectweb.asm.tree.ClassNode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class InheritanceMap {

    private final Map<String, ClassInfo> classes = new HashMap<>();
    private final SourceSet sources;

    public InheritanceMap(final SourceSet sources) {
        this.sources = sources;
    }

    /**
     * Gets the class information for the given class name.
     *
     * @param className The class name
     * @return The class information wrapped in an {@link Optional}
     */
    public Optional<ClassInfo> classInfo(final String className) {
        if (this.classes.containsKey(className)) {
            return Optional.of(this.classes.get(className));
        }
        else {
            return Optional.ofNullable(this.sources.get(className))
                    .flatMap(node -> {
                        final ClassInfo info = new ClassInfo(node);
                        this.classes.put(info.name, info);
                        return Optional.of(info);
                    });
        }
    }

    /**
     * A wrapper used to store inheritance information about classes.
     */
    public class ClassInfo {

        private final String name;
        private final String superName;
        private final List<String> interfaces = new ArrayList<>();
        private final List<String> fields = new ArrayList<>();
        private final List<MethodDescriptor> methods = new ArrayList<>();

        public ClassInfo(final ClassNode node) {
            this.name = node.name;
            this.superName = node.superName;
            this.interfaces.addAll(node.interfaces);
            node.fields.stream()
                    .map(fieldNode -> fieldNode.name)
                    .forEach(this.fields::add);
            node.methods.stream()
                    .map(methodNode -> new MethodDescriptor(methodNode.name, methodNode.desc))
                    .forEach(this.methods::add);
        }

        /**
         * Gets the name of the class.
         *
         * @return The class' name
         */
        public String getName() {
            return this.name;
        }

        /**
         * Gets the name of this class' super class.
         *
         * @return The super name
         */
        public String getSuperName() {
            return this.superName;
        }

        /**
         * Gets an immutable-view of all the interfaces of the class.
         *
         * @return The class' interfaces
         */
        public List<String> getInterfaces() {
            return Collections.unmodifiableList(this.interfaces);
        }

        /**
         * Gets an immutable-view of all the fields of the class.
         *
         * @return The class' fields
         */
        public List<String> getFields() {
            return Collections.unmodifiableList(this.fields);
        }

        /**
         * Gets an immutable-view of all the methods.
         *
         * @return The methods
         */
        public List<MethodDescriptor> getMethods() {
            return Collections.unmodifiableList(this.methods);
        }

    }

}
