package org.untitled.phoenix.component.requirement;

import org.jetbrains.annotations.NotNull;
import org.untitled.phoenix.component.Component;

public final class LinearRequirement<TComponent extends Component> {

    private final @NotNull Operation operation;

    private final @NotNull BaseRequirement<TComponent> requirement;

    public LinearRequirement(@NotNull Operation operation, @NotNull BaseRequirement<TComponent> requirement) {
        this.requirement = requirement;
        this.operation = operation;
    }

    public @NotNull BaseRequirement<TComponent> getRequirement() {
        return requirement;
    }

    public @NotNull Operation getOperation() {
        return operation;
    }

    @Override
    public String toString() {
        return switch (operation) {
            case AND -> String.format("И %S", requirement);
            case OR -> String.format("ИЛИ %S", requirement);
        };
    }
}
