package io.baardl.axon.parser;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class MethodVisitor extends VoidVisitorAdapter<Void> {
	private static final Logger log = LoggerFactory.getLogger(MethodVisitor.class);
	@Override
	public void visit(MethodDeclaration n, Void arg) {
            /* here you can access the attributes of the method.
             this method will be called for all methods in this
             CompilationUnit, including inner class methods */
            log.info("Method: {}", n.getName());
		Optional<BlockStmt> body = n.getBody();
            log.info("Method body: {}", body);
		System.out.println(n.getName());
		super.visit(n, arg);
	}
}