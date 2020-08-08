# Application-Level Profile Semantics

An implementation of [Application-Level Profile Semantics](https://tools.ietf.org/html/draft-amundsen-richardson-foster-alps-02).

![Java CI with Maven](https://github.com/filip26/alps/workflows/Java%20CI%20with%20Maven/badge.svg)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

## Contributing

Your contribution is welcome. 

### Roadmap

- [x] ~0.1 JSON Parser & Writer~
- [ ] 0.2 XML Parser & Writer
- [ ] 0.3 CLI - validation, transformations (JSON <-> XML)
- [ ] 0.4 Effective Document Processor
- [ ] TBD

### Building

Fork and clone the project repository.
Compile sources:

```bash
> cd alps
> mvn package install
```

### Installation

#### ALPS+JSON

```xml
<dependency>
    <groupId>com.apicatalog</groupId>
    <artifactId>alps-jsonp</artifactId>
    <version>0.2-SNAPSHOT</version>
</dependency>
```

Add [JSON-P](https://javaee.github.io/jsonp/) provider, if it is not on the classpath already.

```xml
<dependency>
    <groupId>org.glassfish</groupId>
    <artifactId>jakarta.json</artifactId>
    <version>1.1.6</version>
</dependency>
```
#### ALPS+XML

```xml
<dependency>
    <groupId>com.apicatalog</groupId>
    <artifactId>alps-xml</artifactId>
    <version>0.2-SNAPSHOT</version>
</dependency>

```

## CLI 

### Build

```bash
> git clone git@github.com:filip26/alps.git
> cd alps
> mvn package
> chmod +x alps-cli/bin/alps.sh
```

### Install
Set ALPS_HOME and PATH variables.

e.g.
``` bash
> export ALPS_HOME=/home/filip/alps
> export PATH=$PATH:/home/filip/alps/alps-cli/bin
```
### Run

```bash
> alps.sh validate [{-s|--source}={json|xml}] [input]
> alps.sh transform [{-s|--source}={json|xml}] [input] {-t|--target}={json|xml} [output]
> alps.sh [{-h|--help}]
```


## Resources
- [draft-amundsen-richardson-foster-alps-02](https://tools.ietf.org/html/draft-amundsen-richardson-foster-alps-02)
- [alps.io homapge](http://alps.io/)
- [A Method for Unified API Design](http://amundsen.com/talks/2020-04-goto-unified/index.html)

