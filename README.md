# Eiger

Transform and validate [Application-Level Profile Semantics (ALPS)](https://tools.ietf.org/html/draft-amundsen-richardson-foster-alps-06) documents.

![Java CI with Maven](https://github.com/filip26/eiger/workflows/Java%20CI%20with%20Maven/badge.svg)
[![Language grade: Java](https://img.shields.io/lgtm/grade/java/g/filip26/alps-cli.svg?logo=lgtm&logoWidth=18)](https://lgtm.com/projects/g/filip26/alps-cli/context:java)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=alps-cli&metric=sqale_rating)](https://sonarcloud.io/dashboard?id=alps-cli)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=alps-cli&metric=security_rating)](https://sonarcloud.io/dashboard?id=alps-cli)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

## Table of Contents  
- [Features](#features)
- [Service](#service)
- [CLI](#cli)
  - [Installation](#installation)
  - [Usage](#usage)
- [Contributing](#contributing)  
- [Resources](#resources)  
- [Commercial Support](#commercial-support)

## Features

Mode | `ALPS+XML` | `ALPS+JSON` | `ALPS+YAML` | `OpenAPI 3.0`
--- | :---: | :---: | :---: | :---:
read |   :heavy_check_mark:  |  :heavy_check_mark:  | | :heavy_check_mark:  
write |  :heavy_check_mark:  |  :heavy_check_mark:  |  :heavy_check_mark:  |  

## Service

[https://eiger.apicatalog.com](https://eiger.apicatalog.com)

## CLI

### Usage

```ShellSession
> ./eiger -h
Usage: eiger [-h] [COMMAND]

Transform and validate ALPS documents

Options:
  -h, --help   display a help message

Commands:
  validate   Validate ALPS document
  transform  Transform documents into ALPS
```

```ShellSession
> ./eiger -h transform
Usage: eiger transform [-pv] [-s=(xml|json|oas)] [-t=(xml|json|yaml)] [<file>]

Transform documents into ALPS.

Parameters:
      [<file>]    input file

Options:
  -s, --source=(xml|json|oas)
                  source media type, e.g. --source=oas for OpenAPI
  -t, --target=(xml|json|yaml)
                  target media type, e.g. --target=yaml for alps+yaml
  -p, --pretty    print pretty JSON|XML
  -v, --verbose   include default values

```

```ShellSession
> ./eiger -h validate
Usage: eiger validate [-s=(json|xml)] [<file>]

Validate ALPS document

Parameters:
      [<file>]              input file

Options:
  -s, --source=(json|xml)   source media type, e.g. --source=json for alps+json
```

### Examples

#### Validation

```ShellSession
> wget -q -O- https://raw.githubusercontent.com/alps-io/profiles/master/xml/contacts.xml | eiger validate --source=xml
```

```YAML
# Valid ALPS document
- document: 
    media_type: application/alps+xml
    version: 1.0
    statistics:
      descriptors: 8
      docs: 5
      links: 1
      extensions: 0
```

#### Transformation

`OpenAPI` :arrow_right: `ALPS+YAML`
```bash
> wget -q -O- https://raw.githubusercontent.com/OAI/OpenAPI-Specification/master/examples/v3.0/petstore.yaml | eiger transform --source=oas --target=yaml
```

`ALPS+XML` :arrow_right: `ALPS+YAML`
```bash
> wget -q -O- https://raw.githubusercontent.com/alps-io/profiles/master/xml/contacts.xml | eiger transform --source=xml --target=yaml
```

## Contributing

All PR's welcome!

### Roadmap

- [x] ~0.1 `JsonParser` & `JsonWriter`~
- [x] ~0.2 `XmlParser` & `XmlWriter`~
- [x] ~0.3 CLI - validation, transformations (`ALPS+JSON` :left_right_arrow: `ALPS+XML`)~
  - [x] ~0.3.1 `YamlWriter` (`ALPS+JSON`/`ALPS+XML` :arrow_right: `ALPS+YAML`)~
- [x] ~0.4 OpenAPI Specification (`OAS` :arrow_right: `ALPS`)~
  - [x] ~0.4.1 Native Executables (Ubuntu, MacOS)~
- [ ] 0.5 Effective Profile Processor
- [ ] 0.6 Similarity  / Semantic Equivalence
- [ ] 0.7 `YamlParser` (`ALPS+YAML` :arrow_right: `ALPS+JSON`/`ALPS+XML`)
- [ ] TBD


### Building

Fork and clone the project repository.
Compile sources:

```ShellSession
> ./mvnw clean package install
```

## Resources
- [ALPS Specification Documents](https://github.com/alps-io/spec)
- [A Method for Unified API Design](http://amundsen.com/talks/2020-04-goto-unified/index.html)
- [draft-amundsen-richardson-foster-alps-06](https://tools.ietf.org/html/draft-amundsen-richardson-foster-alps)
- [alps.io group](https://groups.google.com/g/alps-io)
- [alps.io homepage](http://alps.io/)
- [ALPS Illustrated!](https://bit.ly/3tZ42Mq)

## Commercial Support
Commercial support is available at filip26@gmail.com
