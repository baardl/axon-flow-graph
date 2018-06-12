package io.baardl.axon.parser;

import io.baardl.axon.graph.HandlerDescriptor;

public class HandleDescriptorMapper {

    public static HandlerDescriptor buildHandlerDescriptor(MethodDto methodDto) {
        HandlerDescriptor handlerDescriptor = null;
        if (methodDto != null) {
            String fileName = methodDto.getFileName();
            String methodName = methodDto.getMethodName();
            String type = methodDto.getType();
            String handle = methodDto.getHandle();
            String packageName = methodDto.getPackageName();
            if (!handle.startsWith(packageName)) {
                handle = packageName + "." + handle;
            }
            String next = methodDto.getNext();
            if (next != null && !next.startsWith(packageName)) {
                next = packageName + "." + next;
            }
            handlerDescriptor = new HandlerDescriptor(fileName, type, methodName, next, handle);
        }
        return handlerDescriptor;
    }
}
