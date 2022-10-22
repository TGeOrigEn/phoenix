package org.untitled.phoenix.component.condition;

import org.untitled.phoenix.component.Component;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class BaseCondition {

    private final @NotNull Component component;

    private final @NotNull String description;

    private final @Nullable Object value;

    private boolean enabled = true;

    public BaseCondition(@NotNull Component component, @Nullable Object value, @NotNull String description) {
        this.description = description;
        this.component = component;
        this.value = value;
    }

    public final @NotNull String getDescription() {
        return description;
    }

    public final @NotNull Component getComponent()
    {
        return component;
    }

    public final void setEnabled(boolean value) {
        enabled = value;
    }

    public final @Nullable Object getValue() {
        return value;
    }

    public final boolean isEnabled() {
        return enabled;
    }

    public abstract boolean isTrue();
}
