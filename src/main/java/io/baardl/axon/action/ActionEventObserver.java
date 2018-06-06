package io.baardl.axon.action;

import org.axonframework.eventhandling.EventHandler;
import org.slf4j.Logger;

import static org.slf4j.LoggerFactory.getLogger;

public class ActionEventObserver {
    private static final Logger log = getLogger(ActionEventObserver.class);

    @EventHandler
    public void on(ActionCreatedEvent createdEvent) {
        log.info("Created action with id {}", createdEvent.getActionId());
    }
}
