package io.baardl.axon.graph;

import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventhandling.EventHandler;
import org.slf4j.Logger;

import java.util.List;

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

    }

    private void scanByAnnotation(String packageName, Class annotationClass) {
        final List<String> annotatedClasses = new FastClasspathScanner(packageName)
                .enableMethodAnnotationIndexing().scan()
                .getNamesOfClassesWithMethodAnnotation(annotationClass.getName());
        for (String className : annotatedClasses) {
            log.info("{} has annotation {}", className, annotationClass.getName());
        }
    }

    public static void main(String[] args) {
        log.info("Start");
        Main main = new Main();
        main.scan("io.baardl.axon.action");
        log.info("Done");
    }
}
