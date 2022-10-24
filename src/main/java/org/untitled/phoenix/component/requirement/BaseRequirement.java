package org.untitled.phoenix.component.requirement;

import org.untitled.phoenix.component.Component;

import org.jetbrains.annotations.NotNull;

public abstract class BaseRequirement<TComponent extends Component> {

    private final boolean negative;

    public BaseRequirement(boolean isNegative) {
        this.negative = isNegative;
    }

    public abstract @NotNull BaseRequirement<TComponent> and(@NotNull BaseRequirement<TComponent> requirement);

    public abstract @NotNull BaseRequirement<TComponent> or(@NotNull BaseRequirement<TComponent> requirement);

    public abstract @NotNull BaseRequirement<TComponent> toNegative();

    public abstract boolean isTrue(@NotNull TComponent component);

    public final boolean isNegative() {
        return negative;
    }
}
