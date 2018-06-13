package io.baardl.axon.parser;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import io.baardl.axon.graph.MethodDescriptor;
import org.slf4j.Logger;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static io.baardl.axon.parser.MethodDescriptorMapper.buildMethodDescriptor;
import static org.slf4j.LoggerFactory.getLogger;

public class GatewayCallerMethodParser {
    private static final Logger log = getLogger(GatewayCallerMethodParser.class);

    public GatewayCallerMethodParser() {
    }

    public static void main(String[] args) throws Exception {
        String commandHandler = "io.baardl.axon.action.ActionService";
        String javaPath = "src/main/java/";
        GatewayCallerMethodParser parser = new GatewayCallerMethodParser();
        parser.parseFile(javaPath, commandHandler);


    }

    public List<MethodDescriptor> parseFile(String javaPath, String gatewayClassName) {
        List<MethodDescriptor> methodDescriptors = new ArrayList<>();
        FileInputStream in = buildInputStream(javaPath, gatewayClassName);
        if (in != null) {

            // parse it
            CompilationUnit cu = JavaParser.parse(in);

            // visit and print the methods names
            GatewayCallerMethodVisitor methodVisitor = new GatewayCallerMethodVisitor();
            cu.accept(methodVisitor, null);
            MethodDto methodDto = methodVisitor.getMethodDto();
            methodDto.setFileName(gatewayClassName);
            String packageName = cu.getPackageDeclaration().get().getName().asString();
            methodDto.setPackageName(packageName);
            log.trace("MethodDto: {}", methodDto);
            MethodDescriptor methodDescriptor = buildMethodDescriptor(methodDto);
            methodDescriptors.add(methodDescriptor);
        } else {
            log.trace("Failed to parse file: {}", gatewayClassName);
        }
        return methodDescriptors;
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
