package org.untitled.phoenix.component.requirement;

import org.jetbrains.annotations.NotNull;
import org.untitled.phoenix.component.Component;

public final class Operation<TComponent extends Component> {

    private final @NotNull Operator operator;

    private final @NotNull BaseRequirement<TComponent> requirement;

    public Operation(@NotNull Operator operator, @NotNull BaseRequirement<TComponent> requirement) {
        this.requirement = requirement;
        this.operator = operator;
    }

    public @NotNull BaseRequirement<TComponent> getRequirement() {
        return requirement;
    }

    public @NotNull Operator getOperator() {
        return operator;
    }

    @Override
    public String toString() {
        switch (operator) {
            case AND: return String.format("И %S", requirement);
            case OR: return String.format("ИЛИ %S", requirement);
            default: throw new RuntimeException();
        }
    }
}
