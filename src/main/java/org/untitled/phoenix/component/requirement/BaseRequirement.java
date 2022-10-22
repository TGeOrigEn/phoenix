package org.untitled.phoenix.component.requirement;

import org.untitled.phoenix.component.Component;

import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;

public abstract class BaseRequirement<TComponent extends Component> {

    private final @NotNull String description;

    private final @Nullable Object value;

    private final boolean negative;

    public BaseRequirement(@Nullable Object value, @NotNull String description, boolean negative) {
        this.description = description;
        this.negative = negative;
        this.value = value;
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

    public abstract BaseRequirement<TComponent> and(BaseRequirement<TComponent> requirement);

    public abstract BaseRequirement<TComponent> or(BaseRequirement<TComponent> requirement);

    public abstract BaseRequirement<TComponent> toNegative();

    public abstract boolean isTrue(TComponent component);

    @Override
    public String toString() {
        return String.format("'%s -> %s'", getDescription(), value);
    }
}
