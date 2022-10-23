# SaneJava

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.xplosunn/sane-java/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.xplosunn/sane-java)

Java 8 compiler plugin that fails compilation if switches on enums are non-exhaustive.

## Example

Given the following enum:

```java
public enum E {
    A,
    B,
    C
}
```
This will not compile (case for `C` is missing):

```java
public int faultyMethod(E e) {
    switch(e) {
        case A:
        case B:
            return 1;
        default:
            return 0;
    }
}
```
And this will compile:

```java
public void goodMethod(E e) {
     switch (e) {
         case A:
             break;
         case B:
             break;
         case C:
             break;
     }
}
```

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
