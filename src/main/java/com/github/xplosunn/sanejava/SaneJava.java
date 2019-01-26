package com.github.xplosunn.sanejava;

import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.util.JavacTask;
import com.sun.source.util.Plugin;
import com.sun.source.util.TaskEvent;
import com.sun.source.util.TaskListener;

public class SaneJava implements Plugin {

    public static final String NAME = "SaneJava";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void init(JavacTask task, String[] args) {
        System.out.println("Running the SaneJava compiler plugin!");

        task.setTaskListener(new TaskListener() {
            @Override
            public void started(TaskEvent taskEvent) {
            }

            @Override
            public void finished(TaskEvent taskEvent) {
                if (taskEvent.getKind().equals(TaskEvent.Kind.ANALYZE)) {
                    CompilationUnitTree compilationUnit = taskEvent.getCompilationUnit();
                    new PatternTreeVisitor(task).scan(compilationUnit, null);
                }
            }
        });
    }

}
