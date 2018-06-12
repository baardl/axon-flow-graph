package io.baardl.axon.graph;

/**
 * {
 "name": "io.baardl.axon.action.ActionCommandHandler",
 "type": "CommandHandler",
 "method_name": "create",
 "handle": "io.baardl.axon.action.CreateActionCommand",
 "next": "io.baardl.axon.action.ActionCreatedEvent"
 }
 */
public class HandlerDescriptor extends MethodDescriptor {
    private final String handle;

    /**
     *
     * @param name
     * @param type
     * @param handle
     */
    public HandlerDescriptor(String name, String type, String handle) {
        super(name, type);
        this.handle = handle;
    }

    /**
     *
     * @param name
     * @param type
     * @param methodName
     * @param next
     * @param handle
     */
    public HandlerDescriptor(String name, String type, String methodName, String next, String handle) {
        super(name, type, methodName, next);
        this.handle = handle;
    }

    public String getHandle() {
        return handle;
    }

    @Override
    public String toString() {
        return "HandlerDescriptor{" +
                "handle='" + handle + '\'' +
                '}' + super.toString();
    }
}
