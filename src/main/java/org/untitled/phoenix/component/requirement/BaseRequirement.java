package org.untitled.phoenix.component.requirement;

import org.untitled.phoenix.component.Component;

import org.jetbrains.annotations.NotNull;

public abstract class BaseRequirement<TComponent extends Component> {

    private final @NotNull String description;

    private final boolean negative;

    public BaseRequirement(@NotNull String description, boolean isNegative) {
        this.description = description;
        this.negative = isNegative;
    }

    public abstract @NotNull BaseRequirement<TComponent> and(@NotNull BaseRequirement<TComponent> requirement);

    public abstract @NotNull BaseRequirement<TComponent> or(@NotNull BaseRequirement<TComponent> requirement);

    public abstract @NotNull BaseRequirement<TComponent> toNegative();

    public abstract boolean isTrue(@NotNull TComponent component);

    public final @NotNull String getDescription() {
        return description;
    }

    public final boolean isNegative() {
        return negative;
    }
}
