# SaneJava

This is a compiler plugin aimed at improving my quality of life when developing java. The name came from a joke. The
only currently supported feature is checking that switches on enums are exhaustive. I do use linters such as Spotbugs 
and PMD besides this in my projects.

## Adding to Maven

**This does not actually work yet**

You need to add two things into your "pom.xml" file. One dependency:

```
<dependency>
    <groupId>com.github.xplosunn</groupId>
    <artifactId>sane-java</artifactId>
    <version>0.1</version>
    <scope>system</scope>
</dependency>
```

And one plugin configuration (within build):
```
<compilerArgs>
    <arg>-Xplugin:SaneJava</arg>
</compilerArgs>
```

## Requirements

* JDK 8

## Using directly on javac

```
$ mvn compile
$ mvn jar:jar
$ javac -processorpath /target/sane-java-0.1.jar -Xplugin:SaneJava src/test/java/com/explosunn/sanejava/ExhaustiveEnumTest.java 
```

## License

Feel free to use it, fork it, distribute it or any other thing you want. The authors are not responsible for any usage
or consequences thereof. 
