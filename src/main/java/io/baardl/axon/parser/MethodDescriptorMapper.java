package io.baardl.axon.parser;

import io.baardl.axon.graph.MethodDescriptor;

public class MethodDescriptorMapper {

    public static MethodDescriptor buildMethodDescriptor(MethodDto methodDto) {
        MethodDescriptor handlerDescriptor = null;
        if (methodDto != null) {
            String fileName = methodDto.getFileName();
            String methodName = methodDto.getMethodName();
            String type = methodDto.getType();
            String packageName = methodDto.getPackageName();
            String next = methodDto.getNext();
            if (next != null && !next.startsWith(packageName)) {
                next = packageName + "." + next;
            }
            handlerDescriptor = new MethodDescriptor(fileName, type, methodName, next);
        }
        return handlerDescriptor;
    }
}
