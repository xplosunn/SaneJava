# SaneJava

[![Codacy Badge](https://api.codacy.com/project/badge/Grade/051d728372604558ae93c5780acbcb34)](https://www.codacy.com/app/gi.ciberon/SaneJava?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=xplosunn/SaneJava&amp;utm_campaign=Badge_Grade)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.xplosunn/sane-java/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.xplosunn/sane-java)

Java 8 compiler plugin that fails compilation if switches on enums are non-exhaustive.

## Adding to Maven

You need to add two things into your "pom.xml" file. One dependency:

```
<dependency>
    <groupId>com.github.xplosunn</groupId>
    <artifactId>sane-java</artifactId>
    <version>0.0.2</version>
    <scope>provided</scope>
</dependency>
```

And one maven-compiler-plugin configuration (within build):
```
<compilerArgs>
    <arg>-Xplugin:SaneJava</arg>
</compilerArgs>
```

## Requirements

  * JDK 8

## Using directly on javac

```console
$ mvn compile
$ mvn jar:jar
$ javac -processorpath /target/sane-java-0.0.2.jar -Xplugin:SaneJava src/test/java/com/explosunn/sanejava/ExhaustiveEnumTest.java 
```
