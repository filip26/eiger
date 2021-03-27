/*
 * Copyright 2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.apicatalog.alps.yaml;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import com.apicatalog.alps.dom.element.Descriptor;
import com.apicatalog.alps.dom.element.DescriptorType;
import com.apicatalog.yaml.Yaml;
import com.apicatalog.yaml.node.YamlNode;
import com.apicatalog.yaml.node.builder.YamlMappingBuilder;
import com.apicatalog.yaml.node.builder.YamlSequenceBuilder;

final class YamlDescriptorWriter {

    private YamlDescriptorWriter() {}

    public static final YamlNode toYaml(final Set<Descriptor> descriptors, final boolean verbose) {

        if (descriptors.size() == 1) {
            return toYaml(descriptors.iterator().next(), verbose);
        }

        final YamlSequenceBuilder yamlDescriptors = Yaml.createSequenceBuilder();

        descriptors.stream().filter(Objects::nonNull).map(d -> YamlDescriptorWriter.toYaml(d, verbose)).forEach(yamlDescriptors::add);

        return yamlDescriptors.build();
    }

    public static final YamlNode toYaml(final Descriptor descriptor, final boolean verbose) {

        if (descriptor == null) {
            throw new IllegalArgumentException("The 'descriptor' parameter cannot be null.");
        }

        final YamlMappingBuilder yamlDescriptor = Yaml.createMappingBuilder();

        descriptor.id().ifPresent(id -> yamlDescriptor.add(YamlConstants.ID, id.toString()));

        if (descriptor.type() != null && !DescriptorType.SEMANTIC.equals(descriptor.type())) {
            yamlDescriptor.add(YamlConstants.TYPE, descriptor.type().name().toLowerCase());

        } else if (verbose) {
            yamlDescriptor.add(YamlConstants.TYPE, DescriptorType.SEMANTIC.name().toLowerCase());
        }

        descriptor.href().ifPresent(href -> yamlDescriptor.add(YamlConstants.HREF, href.toString()));
        descriptor.definition().ifPresent(def -> yamlDescriptor.add(YamlConstants.DEFINITION, def.toString()));
        descriptor.name().ifPresent(name -> yamlDescriptor.add(YamlConstants.NAME, name));
        descriptor.returnType().ifPresent(rt -> yamlDescriptor.add(YamlConstants.RETURN_TYPE, rt.toString()));
        descriptor.title().ifPresent(title -> yamlDescriptor.add(YamlConstants.TITLE, title));

        // tag
        if (YamlDocumentWriter.isNotEmpty(descriptor.tag())) {
            yamlDescriptor.add(YamlConstants.TAG, descriptor.tag().stream().map(Object::toString).collect(Collectors.joining(" ")));
        }

        // documentation
        YamlDocumentationWriter.toYaml(descriptor.documentation(), verbose).ifPresent(doc -> yamlDescriptor.add(YamlConstants.DOCUMENTATION, doc));

        // descriptors
        if (YamlDocumentWriter.isNotEmpty(descriptor.descriptors())) {
            yamlDescriptor.add(YamlConstants.DESCRIPTOR, toYaml(descriptor.descriptors(), verbose));
        }

        // links
        if (YamlDocumentWriter.isNotEmpty(descriptor.links())) {
            yamlDescriptor.add(YamlConstants.LINK, YamlLinkWriter.toYaml(descriptor.links()));
        }

        // extensions
        if (YamlDocumentWriter.isNotEmpty(descriptor.extensions())) {
            yamlDescriptor.add(YamlConstants.EXTENSION, YamlExtensionWriter.toYaml(descriptor.extensions()));
        }

        return yamlDescriptor.build();
    }
}
