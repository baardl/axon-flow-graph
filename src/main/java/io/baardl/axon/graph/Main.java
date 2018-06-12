package io.baardl.axon.graph;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.baardl.axon.parser.MethodParser;
import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;
import io.github.lukehutch.fastclasspathscanner.scanner.ClassInfo;
import io.github.lukehutch.fastclasspathscanner.scanner.FieldInfo;
import io.github.lukehutch.fastclasspathscanner.scanner.ScanResult;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.eventhandling.EventHandler;
import org.slf4j.Logger;

import java.lang.reflect.Method;
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

    List<String> scan(String packageName) {
        List<String> classes = new ArrayList<>();
        List<String> commandHandlers = scanByAnnotation(packageName, CommandHandler.class);
        List<String> eventHandlers = scanByAnnotation(packageName, EventHandler.class);
        List<String> commandGatewayCallers = scanForImportedClass(packageName, CommandGateway.class);
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

    Class findEventClass(String className) {
        Class<?> animalClass = null;
        try {
            animalClass = Class.forName(className);
            Method[] methods = animalClass.getDeclaredMethods();
            log.trace("Methods: {}", methods);

        } catch (ClassNotFoundException e) {
            log.trace("Class not found.", e);
        }
        return null;
    }

    public static void main(String[] args) {
        log.info("Start");
        Main main = new Main();
        List<String> classes = main.scan("io.baardl.axon.action");
//        main.scan("no.nrk.musikk");
//        main.findEventClass("io.baardl.axon.action.ActionCommandHandler");
        main.printJson("src/main/java/", classes);
        log.info("Done");
    }

    void printJson(String javaPath, List<String> classes) {
        MethodParser methodParser = new MethodParser();
        List<HandlerDescriptor> handlerDescriptors = new ArrayList<>();
        /*
        for (String className : classes) {
            List<HandlerDescriptor> handlerDescriptor = methodParser.parseFile(javaPath, className);
            handlerDescriptors.addAll(handlerDescriptor);
        }
        */
        List<HandlerDescriptor> parsedDescriptors = methodParser.parseFile(javaPath, "io.baardl.axon.action.ActionCommandHandler");
        for (HandlerDescriptor descriptor : parsedDescriptors) {
            log.trace("Descriptor {}", descriptor);
        }

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        log.trace("Json: {}", gson.toJson(parsedDescriptors));



    }
}
