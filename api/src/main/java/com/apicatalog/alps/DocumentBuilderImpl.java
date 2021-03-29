package com.apicatalog.alps;

import java.net.URI;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.apicatalog.alps.dom.Document;
import com.apicatalog.alps.dom.DocumentVersion;
import com.apicatalog.alps.dom.element.Descriptor;
import com.apicatalog.alps.dom.element.Documentation;
import com.apicatalog.alps.dom.element.Extension;
import com.apicatalog.alps.dom.element.Link;

final class DocumentBuilderImpl implements DocumentBuilder {

    final DocumentImpl document;

    public DocumentBuilderImpl(DocumentVersion version) {
        this.document = new DocumentImpl(version);

        this.document.documentation = new LinkedHashSet<>();
        this.document.links = new LinkedHashSet<>();
        this.document.descriptors = new LinkedHashSet<>();
        this.document.extensions = new LinkedHashSet<>();
    }

    public Document build() {
        return document;
    }

    public DocumentBuilderImpl add(Descriptor descriptor) {
        document.descriptors.add(descriptor);
        return this;
    }

    public DocumentBuilderImpl add(DescriptorBuilder descriptor) {
        return add(descriptor.build());
    }

    public DocumentBuilderImpl add(Documentation documentation) {
        document.documentation.add(documentation);
        return this;
    }

    public DocumentBuilderImpl add(DocumentationBuilder documentation) {
        return add(documentation.build());
    }

    public DocumentBuilderImpl add(Extension extension) {
        document.extensions.add(extension);
        return this;
    }

    @Override
    public DocumentBuilder add(ExtensionBuilder extension) {
        return add(extension.build());
    }

    public DocumentBuilderImpl add(Link link) {
        document.links.add(link);
        return this;
    }

    @Override
    public DocumentBuilder add(LinkBuilder link) {
        return add(link.build());
    }

    @Override
    public DocumentBuilder base(URI baseUri) {
        document.setBaseUri(baseUri);
        return this;
    }

    static final class DocumentImpl implements Document {

        protected DocumentVersion version;

        protected URI baseUri;

        protected Set<Documentation> documentation;

        protected Set<Link> links;

        protected Set<Extension> extensions;

        protected Set<Descriptor> descriptors;

        public DocumentImpl(DocumentVersion version) {
            this.version = version;
        }

        @Override
        public DocumentVersion version() {
            return version;
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

        public void setBaseUri(URI baseUri) {
            this.baseUri = baseUri;
        }

        @Override
        public Optional<Descriptor> findById(final URI id) {
            return descriptors.stream().filter(d -> d.id().isPresent() && id.equals(d.id().get())).findFirst();
        }

        @Override
        public Set<Descriptor> findByName(final String name) {
            return descriptors
                        .stream()
                        .filter(d -> d.name().isPresent() && name.equals(d.name().get()))
                        .collect(Collectors.toSet());
        }
    }
}
