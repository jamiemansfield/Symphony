//******************************************************************************
// Copyright (c) Jamie Mansfield <https://jamiemansfield.me/>
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//******************************************************************************

package me.jamiemansfield.symphony.survey;

import me.jamiemansfield.lorenz.MappingSet;
import me.jamiemansfield.lorenz.model.Mapping;
import me.jamiemansfield.lorenz.model.jar.MethodDescriptor;
import me.jamiemansfield.symphony.analysis.InheritanceMap;
import org.objectweb.asm.commons.Remapper;

import java.util.Optional;

/**
 * A simple implementation of {@link Remapper} to remap based
 * on a {@link MappingSet}.
 */
public class SurveyRemapper extends Remapper {

    private final MappingSet mappings;
    private final InheritanceMap inheritanceMap;

    public SurveyRemapper(final MappingSet mappings, final InheritanceMap inheritanceMap) {
        this.mappings = mappings;
        this.inheritanceMap = inheritanceMap;
    }

    @Override
    public String map(final String typeName) {
        return this.mappings.getClassMapping(typeName)
                .map(Mapping::getFullDeobfuscatedName)
                .orElse(typeName);
    }

    private String getFieldName(final String owner, final String name) {
        final Optional<InheritanceMap.ClassInfo> info = this.inheritanceMap.classInfo(owner);
        if (info.isPresent()) {
            final Optional<InheritanceMap.ClassInfo> parentInfo = this.inheritanceMap.classInfo(info.get().getSuperName());
            if (parentInfo.isPresent()) {
                return this.getFieldName(parentInfo.get().getName(), name);
            }
        }

        return this.mappings.getClassMapping(owner)
                .flatMap(mapping -> mapping.getFieldMapping(name)
                        .map(Mapping::getDeobfuscatedName))
                .orElse(name);
    }

    @Override
    public String mapFieldName(final String owner, final String name, final String desc) {
        return this.getFieldName(owner, name);
    }

    @Override
    public String mapMethodName(final String owner, final String name, final String desc) {
        return this.mappings.getClassMapping(owner)
                .flatMap(mapping -> mapping.getMethodMapping(new MethodDescriptor(name, desc))
                        .map(Mapping::getDeobfuscatedName))
                .orElse(name);
    }

}
