# ALPS CLI

Transform and validate [Application-Level Profile Semantics (ALPS)](https://tools.ietf.org/html/draft-amundsen-richardson-foster-alps-02) documents.

![Java CI with Maven](https://github.com/filip26/alps/workflows/Java%20CI%20with%20Maven/badge.svg)
![CodeQL](https://github.com/filip26/alps/workflows/CodeQL/badge.svg)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

## Table of Contents  
- [Features](#features)
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

## Installation

Download the latest package:

- [alps-cli-0.4.1-ubuntu-20.04.zip](https://github.com/filip26/alps/suites/1598565863/artifacts/29118802)
- [alps-cli-0.4.1-macos-11.0.zip](https://github.com/filip26/alps/suites/1598565863/artifacts/29118801)

Extract the zip content and make `alps` command executable.

```ShellSession
> chmod +x alps
```

## Usage

```ShellSession
> ./alps -h
Usage: alps [-h] [COMMAND]

Transform and validate ALPS documents

Options:
  -h, --help   display a help message

Commands:
  validate   Validate ALPS document
  transform  Transform documents into ALPS
```

```ShellSession
> ./alps -h transform
Usage: alps transform [-pv] [-s=(xml|json|oas)] [-t=(xml|json|yaml)] [<file>]

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
> ./alps -h validate
Usage: alps validate [-s=(json|xml)] [<file>]

Validate ALPS document

Parameters:
      [<file>]              input file

Options:
  -s, --source=(json|xml)   source media type, e.g. --source=json for alps+json
```

### Examples

#### Validation

```ShellSession
> wget -q -O- https://raw.githubusercontent.com/alps-io/profiles/master/xml/contacts.xml | alps validate --source=xml
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
> wget -q -O- https://raw.githubusercontent.com/OAI/OpenAPI-Specification/master/examples/v3.0/petstore.yaml | alps transform --source=oas --target=yaml
```

`ALPS+XML` :arrow_right: `ALPS+YAML`
```bash
> wget -q -O- https://raw.githubusercontent.com/alps-io/profiles/master/xml/contacts.xml | alps transform --source=xml --target=yaml
```

## Contributing

Your contribution is welcome! There are many ways to motivate developers or speed up development:

- develop
  - implement a new feature 
  - fix an existing issue
  - improve an existing implementation
- test
  - report a bug
  - implement a test case
- document
  - write javadoc
  - write a tutorial
  - proofread an existing documentation
- promote
  - star, share, the project
  - write an article
- sponsor
  - your requests get top priority
  - you will get a badge

### Roadmap

- [x] ~0.1 `JsonParser` & `JsonWriter`~
- [x] ~0.2 `XmlParser` & `XmlWriter`~
- [x] ~0.3 CLI - validation, transformations (`ALPS+JSON` :left_right_arrow: `ALPS+XML`)~
  - [x] ~0.3.1 `YamlWriter` (`ALPS+JSON`/`ALPS+XML` :arrow_right: `ALPS+YAML`)~
- [x] ~0.4 OpenAPI Specification (`OAS` :arrow_right: `ALPS`)~
  - [ ] 0.4.1 Native Executables (Ubuntu, MacOS) 
- [ ] 0.5 `YamlParser` (`ALPS+YAML` :arrow_right: `ALPS+JSON`/`ALPS+XML`)
- [ ] 0.6 Effective Document Processor
- [ ] 0.7 Semantic Equivalence
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
- [draft-amundsen-richardson-foster-alps-02](https://tools.ietf.org/html/draft-amundsen-richardson-foster-alps-02)
- [alps.io group](https://groups.google.com/g/alps-io)
- [alps.io homapge](http://alps.io/)

## Commercial Support
Commercial support is available at filip26@gmail.com
