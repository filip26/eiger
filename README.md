# Application-Level Profile Semantics

An implementation of [Application-Level Profile Semantics](https://tools.ietf.org/html/draft-amundsen-richardson-foster-alps-02).

![Java CI with Maven](https://github.com/filip26/alps/workflows/Java%20CI%20with%20Maven/badge.svg)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)



## Contributing

Your contribution is welcome. 

### Building

Fork and clone the project repository:

```bash
> git clone git@github.com:REPLACE_WITH_GITHUB_ID/alps-java.git
```

Compile sources:

```bash
> cd alps-java
> mvn package install
```

### Installation

```xml
<dependency>
    <groupId>com.apicatalog</groupId>
    <artifactId>alps-jsonp</artifactId>
    <version>0.1-SNAPSHOT</version>
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
### Roadmap

- [ ] 0.1 JSON Parser
- [ ] 0.2 Effective Document Processor
- [ ] TBD

## Resources
- [draft-amundsen-richardson-foster-alps-02](https://tools.ietf.org/html/draft-amundsen-richardson-foster-alps-02)
- [alps.io homapge](http://alps.io/)

