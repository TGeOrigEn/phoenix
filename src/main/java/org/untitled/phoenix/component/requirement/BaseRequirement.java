package org.untitled.phoenix.component.requirement;

import org.untitled.phoenix.component.Component;

import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;

public abstract class BaseRequirement<TComponent extends Component> {

    public enum Operation {OR, AND}

    private @NotNull String description;

    private final @Nullable Object value;

    private @Nullable BaseRequirement<TComponent> requirement = null;

    private @NotNull Operation operation = Operation.AND;

    private boolean negative = false;

    public BaseRequirement(@Nullable Object value, @NotNull String description) {
        this.description = description;
        this.value = value;
    }

    public final @NotNull BaseRequirement<TComponent> and(@NotNull BaseRequirement<TComponent> requirement) {
        requirement.description = String.format("%s И %s", this, requirement.description);
        requirement.operation = Operation.AND;
        requirement.requirement = this;
        return requirement;
    }

    public final @NotNull BaseRequirement<TComponent> or(@NotNull BaseRequirement<TComponent> requirement) {
        requirement.description = String.format("%s ИЛИ %s", this, requirement.description);
        requirement.operation = Operation.OR;
        requirement.requirement = this;
        return requirement;
    }

    public final BaseRequirement<TComponent> toNegative() {
        negative = !negative;
        return this;
    }

    public abstract boolean isTrue(TComponent component);

    public final @Nullable BaseRequirement<TComponent> getRequirement() {
        return requirement;
    }

    public final @NotNull String getDescription() {
        return description;
    }

    public final @Nullable Object getValue() {
        return value;
    }

    public final boolean isNegative() {
        return negative;
    }

    public final @NotNull Operation getOperation() {
        return operation;
    }

    @Override
    public String toString() {
        return String.format("'%s -> %s'", getDescription(), value);
    }
}
