package com.github.xplosunn.sanejava;

import com.sun.source.tree.*;
import com.sun.source.util.*;

import javax.lang.model.element.*;
import javax.lang.model.type.*;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.util.List;
import java.util.stream.Collectors;

import static javax.lang.model.element.ElementKind.ENUM_CONSTANT;

public class PatternTreeVisitor extends TreePathScanner<Void, Void> {

    private final SourcePositions sourcePositions;
    private final Trees trees;
    private final Types types;

    private final TypeMirror enumType;

    private CompilationUnitTree currCompUnit;

    private int issueCount;

    public PatternTreeVisitor(JavacTask task) {
        types = task.getTypes();
        trees = Trees.instance(task);
        sourcePositions = trees.getSourcePositions();

        // utility to operate on program elements
        Elements elements = task.getElements();
        // create the type element
        enumType = elements.getTypeElement("java.lang.Enum").asType();

        issueCount = 0;
    }

    @Override
    public Void visitCompilationUnit(CompilationUnitTree tree, Void p) {
        currCompUnit = tree;
        Void visit = super.visitCompilationUnit(tree, p);

        if (issueCount > 0) {
            String errorMessage = "Found " + issueCount + " issues.";
            System.out.println(errorMessage);
            throw new IssueFoundException(errorMessage);
        }

        return visit;
    }

    @Override
    public Void visitSwitch(SwitchTree node, Void aVoid) {
        ParenthesizedTree expression = (ParenthesizedTree) node.getExpression();
        TypeMirror expressionTypeMirror = trees.getTypeMirror(new TreePath(getCurrentPath(), expression));
        if (isEnumExpression(expressionTypeMirror)) {
            EnumMembersVisitor enumMembersVisitor = new EnumMembersVisitor();
            types.asElement(expressionTypeMirror).accept(enumMembersVisitor, null);

            if (!allEnumMembersCovered(enumMembersVisitor.enumMembers, node.getCases())) {
                error(node);
            }
        }
        return super.visitSwitch(node, aVoid);
    }

    private boolean isEnumExpression(TypeMirror expressionTypeMirror) {
        return types.directSupertypes(expressionTypeMirror).stream().anyMatch(t -> types.erasure(t).equals(types.erasure(enumType)));
    }

    private boolean allEnumMembersCovered(List<Element> elements, List<? extends CaseTree> caseTrees) {
        List<String> found = caseTrees.stream().filter(caseTree -> caseTree.getExpression() != null).map(caseTree -> {
            ExpressionTree caseExpression = caseTree.getExpression();
            return caseExpression.toString();
        }).collect(Collectors.toList());

        boolean containsAll =
                found.containsAll(elements.stream().map(el -> el.getSimpleName().toString()).collect(Collectors.toList()));

        return containsAll;
    }

    private void error(Tree tree) {
        ++issueCount;
        System.out.println(errorMessage(getLineNumber(tree), currCompUnit.getSourceFile().toUri().toString()));
    }

    private long getLineNumber(Tree tree) {
        // map offsets to line numbers in source file
        LineMap lineMap = currCompUnit.getLineMap();
        if (lineMap == null)
            return -1;
        // find offset of the specified AST node
        long position = sourcePositions.getStartPosition(currCompUnit, tree);
        return lineMap.getLineNumber(position);
    }

    public static String errorMessage(long line, String file) {
        return "Found a non-exhaustive enum switch in " + file + ":" + line;
    }

    public static class IssueFoundException extends RuntimeException {
        public IssueFoundException(String message) {
            super(message);
        }
    }

    private static class EnumMembersVisitor implements ElementVisitor<Void, Void> {

        private List<Element> enumMembers;

        @Override
        public Void visitType(TypeElement e, Void aVoid) {
            enumMembers = e.getEnclosedElements().stream()
                    .filter(elem -> elem.getKind().equals(ENUM_CONSTANT))
                    .collect(Collectors.toList());
            return null;
        }

        @Override
        public Void visit(Element e, Void aVoid) {
            return null;
        }

        @Override
        public Void visit(Element e) {
            return null;
        }

        @Override
        public Void visitPackage(PackageElement e, Void aVoid) {
            return null;
        }

        @Override
        public Void visitVariable(VariableElement e, Void aVoid) {
            return null;
        }

        @Override
        public Void visitExecutable(ExecutableElement e, Void aVoid) {
            return null;
        }

        @Override
        public Void visitTypeParameter(TypeParameterElement e, Void aVoid) {
            return null;
        }

        @Override
        public Void visitUnknown(Element e, Void aVoid) {
            return null;
        }
    }

}
