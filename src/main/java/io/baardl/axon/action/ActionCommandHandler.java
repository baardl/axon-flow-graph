package io.baardl.axon.action;

import org.axonframework.commandhandling.CommandHandler;

import static org.axonframework.commandhandling.model.AggregateLifecycle.apply;

public class ActionCommandHandler {

    private final ActionsRepository actionsRepository;

    public ActionCommandHandler(ActionsRepository actionsRepository) {
        this.actionsRepository = actionsRepository;
    }

    @CommandHandler
    public void create(CreateActionCommand actionCommand) {
        Action action = new Action(actionCommand.getAction());
        actionsRepository.add(action);
        apply(new ActionCreatedEvent(action.getId()));
    }
}
