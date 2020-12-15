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

import java.net.URI;
import java.util.Set;

import com.apicatalog.alps.dom.element.Link;
import com.apicatalog.yaml.Yaml;
import com.apicatalog.yaml.node.YamlNode;
import com.apicatalog.yaml.node.builder.YamlMappingBuilder;
import com.apicatalog.yaml.node.builder.YamlSequenceBuilder;

final class YamlLink implements Link {

    private URI href;
    private String rel;
    
    @Override
    public URI href() {
        return href;
    }

    @Override
    public String rel() {
        return rel;
    }
    
    public static final YamlNode toYaml(Set<Link> links) {
        
        if (links.size() == 1) {
            return toYaml(links.iterator().next());
        }
        
        final YamlSequenceBuilder yamlLinks = Yaml.createSequenceBuilder();
        
        links.stream().map(YamlLink::toYaml).forEach(yamlLinks::add);
        
        return yamlLinks.build();
    }

    public static final YamlNode toYaml(Link link) {
        
        final YamlMappingBuilder yamlLink = Yaml.createMappingBuilder();
        
        if (link.href() != null) {
            yamlLink.add(YamlConstants.HREF, link.href().toString());
        }
        
        if (link.rel() != null && !link.rel().isBlank()) {
            yamlLink.add(YamlConstants.RELATION, link.rel());
        }
        
        return yamlLink.build();
    }
}
