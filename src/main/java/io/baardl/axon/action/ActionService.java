package io.baardl.axon.action;

import org.axonframework.commandhandling.gateway.CommandGateway;

public class ActionService {

    private final CommandGateway commandGateway;

    public ActionService(CommandGateway commandGateway) {
        this.commandGateway = commandGateway;
    }

    public void create(String action) {
        CreateActionCommand command = new CreateActionCommand(action);
        commandGateway.sendAndWait(command);
    }
}
