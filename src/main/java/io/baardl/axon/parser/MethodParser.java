package io.baardl.axon.parser;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import org.slf4j.Logger;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import static org.slf4j.LoggerFactory.getLogger;

public class MethodParser {
	private static final Logger log = getLogger(MethodParser.class);

	public MethodParser() {
	}

	public static void main(String[] args) throws Exception {
		String commandHandler = "io.baardl.axon.action.ActionCommandHandler";
		String javaPath = "src/main/java/";
		MethodParser parser = new MethodParser();
		parser.parseFile(javaPath, commandHandler);


	}

	void parseFile(String javaPath, String commandHandler) {
		// creates an input stream for the file to be parsed
		FileInputStream in = buildInputStream(javaPath, commandHandler);
		if (in != null) {

			// parse it
			CompilationUnit cu = JavaParser.parse(in);

			// visit and print the methods names
			MethodVisitor methodVisitor = new MethodVisitor();
			cu.accept(methodVisitor, null);
			MethodDto methodDto = methodVisitor.getMethodDto();
			String packageName = cu.getPackageDeclaration().get().getName().asString();
			methodDto.setPackageName(packageName);
			log.trace("MethodDto: {}", methodDto);
		} else {
			log.trace("Failed to parse file: {}", commandHandler);
		}
	}

	FileInputStream buildInputStream(String javaPath, String className) {

		FileInputStream in = null;
		String filePath = javaPath + className.replace(".", "/") + ".java";
		try {
			in = new FileInputStream(filePath);
		} catch (FileNotFoundException e) {
			log.trace("Failed to read file {}, reason {}", filePath, e.getMessage());
		}
		return in;
	}
}
