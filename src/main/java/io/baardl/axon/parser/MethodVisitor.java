package io.baardl.axon.parser;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import org.slf4j.Logger;

import java.util.Optional;

import static org.slf4j.LoggerFactory.getLogger;

public class MethodVisitor extends VoidVisitorAdapter<Void> {
    private static final Logger log = getLogger(MethodVisitor.class);
    private MethodDto methodDto;

    @Override
    public void visit(MethodDeclaration n, Void arg) {
            /* here you can access the attributes of the method.
             this method will be called for all methods in this
             CompilationUnit, including inner class methods */
        log.info("Method: {}", n.getName());
        MethodDto methodDto = new MethodDto(n.getNameAsString());
        String annotation = parseAnnotations(n);
        if (annotation != null) {
            methodDto.setType(annotation);
        }
        Optional<BlockStmt> body = n.getBody();
        log.info("Method body: {}", body);
//		((MethodCallExpr) ((ExpressionStmt) n.getBody().value.statements.get(2)).expression).arguments.get(0);
        methodDto = paseBody(n.getBody(), methodDto);
        super.visit(n, arg);
        this.methodDto =  methodDto;
    }

    private String parseAnnotations(MethodDeclaration n) {
        String annotation = null;
        NodeList<AnnotationExpr> annotations = n.getAnnotations();
        if (annotations != null && annotations.size() > 0) {
            annotation = n.getAnnotations().get(0).getNameAsString();
        }
        return annotation;
    }

    private MethodDto paseBody(Optional<BlockStmt> body, MethodDto methodDto) {
        NodeList<Statement> statements = body.get().getStatements();
        NodeList<Expression> arguments;
        for (Statement statement : statements) {
            if (statement.isExpressionStmt()) {
                if (((ExpressionStmt) statement).getExpression().isMethodCallExpr()) {
                    arguments = ((MethodCallExpr) ((ExpressionStmt) statement).getExpression()).getArguments();
                    for (Expression argument : arguments) {
                        if (argument.toString().contains("Event")) {
                            log.trace("Contains Event: {}", argument);
                            ObjectCreationExpr objectCreationExpr = ((ObjectCreationExpr) argument);
                            log.trace("Name: {}", objectCreationExpr);
                            methodDto.setNext(objectCreationExpr.getType().getName().asString());
                        }
                    }
                }
            }
        }
        return methodDto;
        //((MethodCallExpr) ((ExpressionStmt) statement).expression).getArguments().get(0).toString()
        //((MethodCallExpr) ((ExpressionStmt) statement).expression).getArguments()
//		((MethodCallExpr) ((ExpressionStmt) body.value.statements.get(2)).expression).arguments.get(0);
    }

    public MethodDto getMethodDto() {
        return methodDto;
    }
}