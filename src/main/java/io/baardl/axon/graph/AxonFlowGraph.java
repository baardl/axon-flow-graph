package io.baardl.axon.graph;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class AxonFlowGraph {
    @SerializedName("package_name")
    private String packageName;

    @SerializedName("start")
    private List<MethodDescriptor> start = new ArrayList<>();

    @SerializedName("command_handlers")
    private List<HandlerDescriptor> commandHandlers = new ArrayList<>();

    @SerializedName("event_handlers")
    private List<HandlerDescriptor> eventHandlers = new ArrayList<>();

    public AxonFlowGraph(String packageName, List<MethodDescriptor> handlers) {
        this.packageName = packageName;
        updateHandlers(handlers);
    }

    public void updateHandlers(List<MethodDescriptor> handlers) {
        for (MethodDescriptor handler : handlers) {
            String type = handler.getType();
            if (type == null) {
                type = "start";
            }
            switch (type) {
                case "CommandHandler":
                    commandHandlers.add((HandlerDescriptor)handler);
                    break;
                case "EventHandler":
                    eventHandlers.add((HandlerDescriptor)handler);
                    break;
                case "start":
                    start.add(handler);
                    break;
                default:
                    start.add(handler);
            }
        }

    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public List<HandlerDescriptor> getCommandHandlers() {
        return commandHandlers;
    }

    public void setCommandHandlers(List<HandlerDescriptor> commandHandlers) {
        this.commandHandlers = commandHandlers;
    }

    public List<MethodDescriptor> getStart() {
        return start;
    }

    public void setStart(List<MethodDescriptor> start) {
        this.start = start;
    }

    public List<HandlerDescriptor> getEventHandlers() {
        return eventHandlers;
    }

    public void setEventHandlers(List<HandlerDescriptor> eventHandlers) {
        this.eventHandlers = eventHandlers;
    }
}
