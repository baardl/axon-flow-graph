package io.baardl.axon.graph;

import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;
import io.github.lukehutch.fastclasspathscanner.scanner.ClassInfo;
import io.github.lukehutch.fastclasspathscanner.scanner.FieldInfo;
import io.github.lukehutch.fastclasspathscanner.scanner.ScanResult;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.eventhandling.EventHandler;
import org.slf4j.Logger;

import java.util.List;
import java.util.Map;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Analyze the flow of commands and events.
 * Default is in io.baardl.axon.action package.
 */
public class Main {
    private static final Logger log = getLogger(Main.class);

    void scan(String packageName) {
        scanByAnnotation(packageName, CommandHandler.class);
        scanByAnnotation(packageName, EventHandler.class);
        scanForImportedClass(packageName, CommandGateway.class);

    }

    void scanByAnnotation(String packageName, Class annotationClass) {
        final List<String> annotatedClasses = new FastClasspathScanner(packageName)
                .enableMethodAnnotationIndexing().scan()
                .getNamesOfClassesWithMethodAnnotation(annotationClass.getName());
        for (String className : annotatedClasses) {
            log.info("{} has annotation {}", className, annotationClass.getName());
        }
    }

    void scanForImportedClass(String packageName, Class importedClass) {
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
                if (info.getType().equals(importedClass))
                log.info("{}, uses {} ", classInfo.getClassName(),info.getType().getName());
            }
        }
    }

    public static void main(String[] args) {
        log.info("Start");
        Main main = new Main();
        main.scan("io.baardl.axon.action");
        log.info("Done");
    }
}
