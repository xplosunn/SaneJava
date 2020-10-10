# SaneJava

[![Build Status](https://travis-ci.org/xplosunn/SaneJava.svg?branch=master)](https://travis-ci.org/xplosunn/SaneJava)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/051d728372604558ae93c5780acbcb34)](https://www.codacy.com/app/gi.ciberon/SaneJava?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=xplosunn/SaneJava&amp;utm_campaign=Badge_Grade)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.xplosunn/sane-java/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.xplosunn/sane-java)

This is a compiler plugin aimed at improving my quality of life when developing java. The name came from a joke. The
only currently supported feature is checking that switches on enums are exhaustive. I do use linters such as Spotbugs 
and PMD besides this in my projects.

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
$ javac -processorpath /target/sane-java-0.0.1.jar -Xplugin:SaneJava src/test/java/com/explosunn/sanejava/ExhaustiveEnumTest.java 
```
