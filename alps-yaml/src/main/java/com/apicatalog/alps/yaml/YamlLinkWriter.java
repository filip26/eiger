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

import java.util.Set;
import java.util.stream.Collectors;

import com.apicatalog.alps.dom.element.Link;
import com.apicatalog.yaml.Yaml;
import com.apicatalog.yaml.node.YamlNode;
import com.apicatalog.yaml.node.builder.YamlMappingBuilder;
import com.apicatalog.yaml.node.builder.YamlSequenceBuilder;

final class YamlLinkWriter {

    private YamlLinkWriter() {}

    public static final YamlNode toYaml(Set<Link> links) {

        if (links.size() == 1) {
            return toYaml(links.iterator().next());
        }

        final YamlSequenceBuilder yamlLinks = Yaml.createSequenceBuilder();

        links.stream().map(YamlLinkWriter::toYaml).forEach(yamlLinks::add);

        return yamlLinks.build();
    }

    public static final YamlNode toYaml(Link link) {

        final YamlMappingBuilder yamlLink = Yaml.createMappingBuilder();

        link.title().ifPresent(title -> yamlLink.add(YamlConstants.TITLE, title));
        
        if (link.href() != null) {
            yamlLink.add(YamlConstants.HREF, link.href().toString());
        }

        if (link.rel() != null && !link.rel().isBlank()) {
            yamlLink.add(YamlConstants.RELATION, link.rel());
        }
        
        // tag
        if (YamlDocumentWriter.isNotEmpty(link.tag())) {
            yamlLink.add(YamlConstants.TAG, link.tag().stream().map(Object::toString).collect(Collectors.joining(" ")));
        }

        return yamlLink.build();
    }
}
