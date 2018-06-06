package io.baardl.axon.action;

public class ActionCreatedEvent {
    private final String  actionId;
    public ActionCreatedEvent(String actionId) {
        this.actionId = actionId;
    }

    public String getActionId() {
        return actionId;
    }
}
