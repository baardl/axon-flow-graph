package io.baardl.axon.graph;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.baardl.axon.parser.GatewayCallerMethodParser;
import io.baardl.axon.parser.MethodParser;
import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;
import io.github.lukehutch.fastclasspathscanner.scanner.ClassInfo;
import io.github.lukehutch.fastclasspathscanner.scanner.FieldInfo;
import io.github.lukehutch.fastclasspathscanner.scanner.ScanResult;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.eventhandling.EventHandler;
import org.slf4j.Logger;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Analyze the flow of commands and events.
 * Default is in io.baardl.axon.action package.
 */
public class Main {
    private static final Logger log = getLogger(Main.class);

    private List<String> commandHandlers;
    private List<String> eventHandlers;
    private List<String> commandGatewayCallers;

    List<String> scan(String packageName) {
        List<String> classes = new ArrayList<>();
        commandHandlers = scanByAnnotation(packageName, CommandHandler.class);
        eventHandlers = scanByAnnotation(packageName, EventHandler.class);
        commandGatewayCallers = scanForImportedClass(packageName, CommandGateway.class);
        classes.addAll(commandHandlers);
        classes.addAll(eventHandlers);
        classes.addAll(commandGatewayCallers);
        return classes;

    }

    List<String> scanByAnnotation(String packageName, Class annotationClass) {
        final List<String> annotatedClasses = new FastClasspathScanner(packageName)
                .enableMethodAnnotationIndexing().scan()
                .getNamesOfClassesWithMethodAnnotation(annotationClass.getName());
        for (String className : annotatedClasses) {
            log.info("{} has annotation {}", className, annotationClass.getName());
        }

        return annotatedClasses;
    }

    List<String> scanForImportedClass(String packageName, Class importedClass) {
        List<String> usingImportedClass = new ArrayList<>();
        ScanResult scanResult = new FastClasspathScanner(packageName)
                // Must call this before .scan()
                .enableFieldInfo()
                .ignoreFieldVisibility()
                .scan();
        Map<String, ClassInfo> classMap = scanResult
                .getClassNameToClassInfo();
        for (ClassInfo classInfo : classMap.values()) {
            List<FieldInfo> fieldInfo = classInfo.getFieldInfo();
            for (FieldInfo info : fieldInfo) {
                if (info.getType().equals(importedClass)) {
                    log.info("{}, uses {} ", classInfo.getClassName(), info.getType().getName());
                    usingImportedClass.add(classInfo.getClassName());
                }
            }
        }
        return usingImportedClass;
    }

    public static void main(String[] args) {
        log.info("Start");
        Main main = new Main();
        String packageName = "io.baardl.axon.action";
        List<String> classes = main.scan(packageName);
//        main.scan("no.nrk.musikk");
//        main.findEventClass("io.baardl.axon.action.ActionCommandHandler");
        main.printJson("src/main/java/", packageName);
        log.info("Done");
    }

    void printJson(String javaPath, String packageName) {
        MethodParser methodParser = new MethodParser();
        GatewayCallerMethodParser gatewayParser = new GatewayCallerMethodParser();
        List<MethodDescriptor> parsedDescriptors = new ArrayList<>();
        List<String> handlerClasses = new ArrayList<>();
        handlerClasses.addAll(commandHandlers);
        handlerClasses.addAll(eventHandlers);
        for (String className : handlerClasses) {
            List<HandlerDescriptor> handlerDescriptors = methodParser.parseFile(javaPath, className);
            parsedDescriptors.addAll(handlerDescriptors);
        }

        for (String commandGatewayCaller : commandGatewayCallers) {
            List<MethodDescriptor> gatewayCallerDescriptors = gatewayParser.parseFile(javaPath, commandGatewayCaller);
            parsedDescriptors.addAll(gatewayCallerDescriptors);
        }
        /*
        List<HandlerDescriptor> parsedDescriptors = methodParser.parseFile(javaPath, "io.baardl.axon.action.ActionCommandHandler");
        for (HandlerDescriptor descriptor : parsedDescriptors) {
            log.trace("Descriptor {}", descriptor);
        }
        */

        AxonFlowGraph axonFlowGraph = new AxonFlowGraph(packageName, parsedDescriptors);
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        String json = gson.toJson(axonFlowGraph);
        log.trace("Json: {}", json);
        String outputFile = "axon-flow-graph.json";
        writeJsonToFile(outputFile, json, packageName);

    }

    private void writeJsonToFile(String outputFile, String json, String packageName) {
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(outputFile);
            byte[] contentInBytes = json.getBytes();

            out.write(contentInBytes);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            log.error("Failed to write json to file. Filename {}, json: {}. Reason {}", outputFile, json, e.getMessage());
        } catch (IOException e) {
            log.error("Failed to write json to file. Filename {}, json: {}. Reason {}", outputFile, json, e.getMessage());
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                //ignore
            }
        }
    }
}
