package io.baardl.axon.action;

public class CreateActionCommand {
    private final String action;

    public CreateActionCommand(String action) {
        this.action = action;
    }

    public String getAction() {
        return action;
    }
}
