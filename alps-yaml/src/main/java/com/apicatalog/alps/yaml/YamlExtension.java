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
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import com.apicatalog.alps.dom.element.Extension;
import com.apicatalog.yaml.Yaml;
import com.apicatalog.yaml.YamlMappingBuilder;
import com.apicatalog.yaml.YamlNode;
import com.apicatalog.yaml.YamlSequenceBuilder;

class YamlExtension implements Extension {

    private URI id;
    private URI href;
    private String value;
    
    private Map<String, String> attributes;
    
    @Override
    public Optional<URI> href() {
        return Optional.ofNullable(href);
    }

    @Override
    public URI id() {
        return id;
    }

    @Override
    public Optional<String> value() {
        return Optional.ofNullable(value);
    }
    
    @Override
    public Map<String, String> attributes() {
        return attributes;
    }
    
    public static final YamlNode toYaml(Set<Extension> extensions) {
        
        if (extensions.size() == 1) {
            return toYaml(extensions.iterator().next());
        }
        
        final YamlSequenceBuilder yamlExt = Yaml.createSequenceBuilder();
        
        extensions.stream().map(YamlExtension::toYaml).forEach(yamlExt::add);
        
        return yamlExt.build();
    }

    public static final YamlNode toYaml(Extension extension) {
        
        final YamlMappingBuilder yamlExt = Yaml.createMappingBuilder();

        yamlExt.add(YamlConstants.ID, extension.id().toString());

        extension.href().ifPresent(href -> yamlExt.add(YamlConstants.HREF, href.toString()));
        extension.value().ifPresent(value -> yamlExt.add(YamlConstants.VALUE, value));
        
        // custom attributes
        extension
                .attributes()
                .forEach(yamlExt::add);
        
        return yamlExt.build();
    }
}
