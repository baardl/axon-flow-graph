# axon-flow-graph
Visualize the flow of commands and events in an Axon Framework service

## Generate dependency json

```
mvn clean install
java -jar target/axon-flow-graph-${project.version}.jar
```

Output will be in the file axon-flow-graph.json

## Work in progress:

### Java Parsin

[Parse Method](src/main/java/io/baardl/axon/parser/MethodParser.java)
- https://javaparser.org/
