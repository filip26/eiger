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
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.apicatalog.alps.dom.Document;
import com.apicatalog.alps.dom.DocumentVersion;
import com.apicatalog.alps.dom.element.Descriptor;
import com.apicatalog.alps.dom.element.Documentation;
import com.apicatalog.alps.dom.element.Extension;
import com.apicatalog.alps.dom.element.Link;
import com.apicatalog.yaml.Yaml;
import com.apicatalog.yaml.node.YamlMapping;
import com.apicatalog.yaml.node.builder.YamlMappingBuilder;

public class YamlDocument implements Document {

    private DocumentVersion version;
    
    private URI baseUri;
    
    private Set<Documentation> documentation;
    
    private Set<Link> links;
    
    private Set<Extension> extensions;
    
    private Map<URI, Descriptor> descriptors;
    
    public YamlDocument() {
        this.version = DocumentVersion.VERSION_1_0;
    }
    
    @Override
    public Optional<Descriptor> findById(final URI id) {
        return Optional.ofNullable(descriptors.get(id));
    }

    @Override
    public Set<Descriptor> findByName(final String name) {
        return descriptors
                    .values().stream()
                    .filter(d -> d.name().isPresent() && name.equals(d.name().get()))
                    .collect(Collectors.toSet());
    }

    @Override
    public DocumentVersion version() {
        return version;
    }

    @Override
    public Set<Descriptor> descriptors() {
        return descriptors
                    .values().stream()
                    .filter(d -> d.parent().isEmpty())
                    .collect(Collectors.toSet())
                    ;
    }

    @Override
    public Collection<Descriptor> allDescriptors() {
        return descriptors.values();
    }

    @Override
    public Set<Link> links() {
        return links;
    }

    @Override
    public Set<Documentation> documentation() {
        return documentation;
    }
    
    @Override
    public Set<Extension> extensions() {
        return extensions;
    }

    @Override
    public URI baseUri() {
        return baseUri;
    }

    public static final YamlMapping toYaml(final Document document, final boolean verbose) {
        
        final YamlMappingBuilder alps = Yaml.createMappingBuilder();
        
        // version
        alps.add(YamlConstants.VERSION, Yaml.createScalar(YamlConstants.VERSION_1_0));
        
        // documentation
        YamlDocumentation.toYaml(document.documentation(), verbose).ifPresent(doc -> alps.add(YamlConstants.DOCUMENTATION, doc));

        // links
        if (isNotEmpty(document.links())) {
            alps.add(YamlConstants.LINK, YamlLink.toYaml(document.links()));
        }
        
        // descriptors
        if (isNotEmpty(document.descriptors())) {
            alps.add(YamlConstants.DESCRIPTOR, YamlDescriptor.toYaml(document.descriptors(), verbose));
        }
        
        // extensions
        if (isNotEmpty(document.extensions())) {
            alps.add(YamlConstants.EXTENSION, YamlExtension.toYaml(document.extensions()));            
        }

        return Yaml.createMappingBuilder().add(YamlConstants.ROOT, alps).build();
    }
    
    protected static final boolean isNotEmpty(final Collection<?> collection) {
        return collection != null && !collection.isEmpty();
    }
}
