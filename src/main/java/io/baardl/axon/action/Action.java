package io.baardl.axon.action;

import java.util.UUID;

public class Action {
    private final String action;
    private final String id;

    public Action(String action) {
        this.action = action;
        this.id = UUID.randomUUID().toString();
    }

    public String getAction() {
        return action;
    }

    public String getId() {
        return id;
    }
}
