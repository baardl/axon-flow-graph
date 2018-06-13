package io.baardl.axon.parser;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.slf4j.LoggerFactory.getLogger;

public class GatewayCallerMethodVisitor extends VoidVisitorAdapter<Void> {
    private static final Logger log = getLogger(GatewayCallerMethodVisitor.class);
    private MethodDto methodDto;

    @Override
    public void visit(MethodDeclaration n, Void arg) {
        /*
        Look for methods looking like this:
        public void create(String action) {
            CreateActionCommand command = new CreateActionCommand(action);
            commandGateway.sendAndWait(command);
        }
         */
        log.info("Method: {}", n.getName());
        MethodDto methodDto = new MethodDto(n.getNameAsString());

        Optional<BlockStmt> body = n.getBody();
        log.info("Method body: {}", body);
//		((MethodCallExpr) ((ExpressionStmt) n.getBody().value.statements.get(2)).expression).arguments.get(0);
        methodDto = paseBody(n.getBody(), methodDto);
        super.visit(n, arg);
        this.methodDto =  methodDto;
    }


    private MethodDto paseBody(Optional<BlockStmt> body, MethodDto methodDto) {
        if (body.get().toString().contains("sendAndWait")) {
            NodeList<Statement> statements = body.get().getStatements();
            NodeList<Expression> arguments;
            Map<String, String> createdObjects = new HashMap<>();
            for (Statement statement : statements) {
                if (statement.isExpressionStmt()) {
                    Expression expression = ((ExpressionStmt) statement).getExpression();
                    if (expression.isMethodCallExpr()) {
                        arguments = ((MethodCallExpr) expression).getArguments();
                        for (Expression argument : arguments) {
                                if (argument instanceof NameExpr) {
                                    String name = ((NameExpr) argument).getNameAsString();
                                    String parentName = ((MethodCallExpr)((NameExpr)argument).getParentNode().get()).getNameAsString();
                                    if (parentName.equals("sendAndWait")) {
                                        String commandClass = createdObjects.get(name);
                                        methodDto.setType("CommandGateway");
                                        methodDto.setNext(commandClass);
                                    }
                                }
                            }

                    } else if (expression.isVariableDeclarationExpr()) {
                        Expression variableCreateExpression = ((ExpressionStmt) statement).getExpression();
                        log.trace("Create object {}", variableCreateExpression);
                        String name = ((VariableDeclarationExpr) expression).getVariables().get(0).getNameAsString();
                        String type = ((VariableDeclarationExpr) expression).getVariables().get(0).getTypeAsString();
                        if (type.contains("Command")) {
                            createdObjects.put(name, type);
                        }
                    }
                }
            }
        } else {
            methodDto = null;
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