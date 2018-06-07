package io.baardl.axon.parser;

import java.io.FileInputStream;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;

public class MethodParser {

	public static void main(String[] args) throws Exception {
		// creates an input stream for the file to be parsed
		FileInputStream in = new FileInputStream("src/main/java/io/baardl/axon/action/ActionCommandHandler.java");

		// parse it
		CompilationUnit cu = JavaParser.parse(in);

		// visit and print the methods names
		cu.accept(new MethodVisitor(), null);
	}
}
