# Application-Level Profile Semantics for Java

An implementation of [Application-Level Profile Semantics](https://tools.ietf.org/html/draft-amundsen-richardson-foster-alps-02).

## Resources
- [draft-amundsen-richardson-foster-alps-02](https://tools.ietf.org/html/draft-amundsen-richardson-foster-alps-02)
- [alps.io homapge](http://alps.io/)

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

Add JSON-P provider, if it is not on the classpath already.

```xml
<dependency>
    <groupId>org.glassfish</groupId>
    <artifactId>jakarta.json</artifactId>
    <version>1.1.6</version>
</dependency>
```



