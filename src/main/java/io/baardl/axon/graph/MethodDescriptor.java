package io.baardl.axon.graph;

import com.google.gson.annotations.SerializedName;

/**
 * {
 "name": "io.baardl.axon.action.ActionCommandHandler",
 "type": "CommandHandler",
 "method_name": "create",
 "next": "io.baardl.axon.action.ActionCreatedEvent"
 }
 */
public class MethodDescriptor {
    private final String name;
    private final String type;
    @SerializedName("method_name")
    private String methodName;
    private String next;

    public MethodDescriptor(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public MethodDescriptor(String name, String type, String methodName, String next) {
        this.name = name;
        this.type = type;
        this.methodName = methodName;
        this.next = next;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    @Override
    public String toString() {
        return "MethodDescriptor{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", methodName='" + methodName + '\'' +
                ", next='" + next + '\'' +
                '}';
    }
}
