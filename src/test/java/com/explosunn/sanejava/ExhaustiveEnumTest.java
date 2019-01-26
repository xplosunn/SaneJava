package com.explosunn.sanejava;

import com.github.xplosunn.sanejava.SaneJava;
import org.junit.Test;

import javax.tools.*;
import java.io.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class ExhaustiveEnumTest {

    @Test
    public void test() throws Exception {
        List<String> caughtOutput = new LinkedList<>();
        PrintStream myStream = new PrintStream(System.out) {
            @Override
            public void println(String x) {
                caughtOutput.add(x);
                super.println(x);
            }
        };
        System.setOut(myStream);

        try {
            compile("EnumTest", getFileFromResources("codesamples/EnumTest.java"));
            fail();
        } catch (RuntimeException e) {

        }

        assertEquals("Found a non-exhaustive enum switch in file://EnumTest.java:10", caughtOutput.get(1));
        assertEquals(3, caughtOutput.size());
    }

    private String getFileFromResources(String fileName) throws FileNotFoundException {

        StringBuilder result = new StringBuilder();

        //Get file from resources folder
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(fileName).getFile());

        try (Scanner scanner = new Scanner(file)) {

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                result.append(line).append("\n");
            }

            scanner.close();

        }

        return result.toString();

    }

    public void compile(String qualifiedClassName, String testSource) {
        StringWriter output = new StringWriter();

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        List<SimpleSourceFile> compilationUnits = singletonList(new SimpleSourceFile(qualifiedClassName, testSource));
        List<String> arguments = new ArrayList<>();
        arguments.addAll(asList("-classpath", System.getProperty("java.class.path"), "-Xplugin:" + SaneJava.NAME));
        JavaCompiler.CompilationTask task =
                compiler.getTask(output, compiler.getStandardFileManager(null, null, null), null, arguments, null, compilationUnits);

        task.call();
    }

    public class SimpleSourceFile extends SimpleJavaFileObject {

        private final String content;

        public SimpleSourceFile(String qualifiedClassName, String testSource) {
            super(URI.create(String.format("file://%s%s",
                    qualifiedClassName.replaceAll("\\.", "/"),
                    JavaFileObject.Kind.SOURCE.extension)),
                    JavaFileObject.Kind.SOURCE);
            content = testSource;
        }

        @Override
        public CharSequence getCharContent(boolean ignoreEncodingErrors) {
            return content;
        }
    }
}
