package com.apicatalog.alps;

import java.net.URI;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.apicatalog.alps.dom.element.Descriptor;
import com.apicatalog.alps.dom.element.DescriptorType;
import com.apicatalog.alps.dom.element.Documentation;
import com.apicatalog.alps.dom.element.Extension;
import com.apicatalog.alps.dom.element.Link;

final class DescriptorBuilderImpl implements DescriptorBuilder {

    private final DescriptorImpl descriptor;

    public DescriptorBuilderImpl() {
        this.descriptor = new DescriptorImpl();

        this.descriptor.descriptors = new LinkedHashSet<>();
        this.descriptor.documentation = new LinkedHashSet<>();
        this.descriptor.links = new LinkedHashSet<>();
        this.descriptor.extensions = new LinkedHashSet<>();
    }

    public final DescriptorBuilder add(Descriptor descriptor) {
        this.descriptor.descriptors.add(descriptor);
        return this;
    }

    public final DescriptorBuilder add(DescriptorBuilderImpl descriptor) {
        return add(descriptor.build());
    }

    public final DescriptorBuilder add(Link link) {
        this.descriptor.links.add(link);
        return this;
    }

    public final DescriptorBuilder add(Extension extension) {
        this.descriptor.extensions.add(extension);
        return this;
    }

    public final DescriptorBuilder add(Documentation documentation) {
        this.descriptor.documentation.add(documentation);
        return this;
    }

    public final DescriptorBuilder id(URI id) {
        descriptor.id = id;
        return this;
    }

    public final DescriptorBuilder title(String title) {
        descriptor.title = title;
        return this;
    }

    public final DescriptorBuilder name(String name) {
        descriptor.name = name;
        return this;
    }

    public final DescriptorBuilder href(URI href) {
        descriptor.href = href;
        return this;
    }

    public final DescriptorBuilder definition(URI definition) {
        descriptor.definition = definition;
        return this;
    }

    public final DescriptorBuilder returnType(URI returnType) {
        descriptor.returnType = returnType;
        return this;
    }

    public final DescriptorBuilder tag(List<String> tag) {
        descriptor.tag = tag;
        return this;
    }

    public final Descriptor build() {
        return descriptor;
    }

    @Override
    public DescriptorBuilder add(DescriptorBuilder descriptor) {
        return add(descriptor.build());
    }

    @Override
    public DescriptorBuilder add(DocumentationBuilder documentation) {
        return add(documentation.build());
    }

    @Override
    public DescriptorBuilder add(ExtensionBuilder extension) {
        return add(extension.build());
    }

    @Override
    public DescriptorBuilder add(LinkBuilder link) {
        return add(link.build());
    }

    @Override
    public DescriptorBuilder type(DescriptorType type) {
        descriptor.type = type;
        return this;
    }

    static final class DescriptorImpl implements Descriptor {

        URI id;

        URI href;

        URI definition;

        String name;

        DescriptorType type;

        URI returnType;

        String title;

        List<String> tag;

        Set<Documentation> documentation;

        Set<Descriptor> descriptors;

        Set<Link> links;

        Set<Extension> extensions;

        @Override
        public Optional<URI> id() {
            return Optional.ofNullable(id);
        }

        @Override
        public Optional<URI> href() {
            return Optional.ofNullable(href);
        }

        @Override
        public Optional<String> name() {
            return Optional.ofNullable(name);
        }

        @Override
        public DescriptorType type() {
            return type;
        }

        @Override
        public Optional<URI> returnType() {
            return Optional.ofNullable(returnType);
        }

        @Override
        public Optional<String> title() {
            return Optional.ofNullable(title);
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
        public Set<Descriptor> descriptors() {
            return descriptors;
        }

        @Override
        public Set<Link> links() {
            return links;
        }

        @Override
        public Optional<URI> definition() {
            return Optional.ofNullable(definition);
        }

        @Override
        public List<String> tag() {
            return tag != null ? tag : Collections.emptyList();
        }
    }
}
