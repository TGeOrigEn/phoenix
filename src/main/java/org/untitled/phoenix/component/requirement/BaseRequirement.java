package org.untitled.phoenix.component.requirement;

import org.untitled.phoenix.component.Component;

import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;

public abstract class BaseRequirement<TComponent extends Component> {

    private final @Nullable BaseRequirement<TComponent> requirement;

    private final @NotNull String description;

    private final @Nullable Object value;

    private final boolean negative;

    public BaseRequirement(@Nullable Object value, @NotNull String description, @Nullable BaseRequirement<TComponent> requirement, boolean negative) {
        this.requirement = requirement;
        this.description = description;
        this.negative = negative;
        this.value = value;
    }

    public abstract BaseRequirement<TComponent> and(BaseRequirement<TComponent> requirement);

    public abstract BaseRequirement<TComponent> or(BaseRequirement<TComponent> requirement);

    public abstract BaseRequirement<TComponent> toNegative();

    public abstract boolean isTrue(TComponent component);

    public @Nullable BaseRequirement<TComponent> getRequirement() {
        return requirement;
    }

    public @NotNull String getDescription() {
        return description;
    }

    public @Nullable Object getValue() {
        return value;
    }

    public boolean isNegative() {
        return negative;
    }

    @Override
    public String toString() {
        return String.format("'%s -> %s'", getDescription(), value);
    }
}
