package io.baardl.axon.action;

import java.util.ArrayList;
import java.util.List;

public class ActionsRepository {
    private final List<Action> actions;

    public ActionsRepository() {
        actions = new ArrayList<>();
    }

    public void add(Action action) {
        actions.add(action);
    }
}
