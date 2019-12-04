//******************************************************************************
// Copyright (c) Jamie Mansfield <https://jamiemansfield.me/>
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//******************************************************************************

package me.jamiemansfield.symphony.jar.index;

import org.cadixdev.bombe.type.FieldType;
import org.cadixdev.bombe.type.MethodDescriptor;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * An indexed class, containing basic information of both
 * it and its members.
 *
 * @author Jamie Mansfield
 * @since 0.1.0
 */
public class IndexedClass {

    private final String name;
    private final Set<IndexedField> fields;
    private final Set<IndexedMethod> methods;

    public IndexedClass(final String name, final Set<IndexedField> fields, final Set<IndexedMethod> methods) {
        this.name = name;
        this.fields = fields;
        this.methods = methods;
    }

    public String getName() {
        return this.name;
    }

    public Set<IndexedField> getFields() {
        return Collections.unmodifiableSet(this.fields);
    }

    public Set<IndexedMethod> getMethods() {
        return Collections.unmodifiableSet(this.methods);
    }

    public static class Builder {

        private final String name;
        private final Set<IndexedField> fields = new HashSet<>();
        private final Set<IndexedMethod> methods = new HashSet<>();

        public Builder(final String name) {
            this.name = name;
        }

        public Builder field(final String name, final int access, final FieldType type) {
            this.fields.add(new IndexedField(name, access, type));
            return this;
        }

        public Builder method(final String name, final int access, final MethodDescriptor desc) {
            this.methods.add(new IndexedMethod(name, access, desc));
            return this;
        }

        public IndexedClass build() {
            return new IndexedClass(this.name, this.fields, this.methods);
        }

    }

}
