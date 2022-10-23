package org.untitled.phoenix.exception;

import org.untitled.phoenix.component.condition.BaseCondition;
import org.untitled.phoenix.component.Component;

import org.jetbrains.annotations.NotNull;

import java.time.Duration;

public final class ComponentConditionException extends RuntimeException {

    private final @NotNull BaseCondition condition;

    private final @NotNull Component component;

    private final @NotNull Duration timeout;

    public ComponentConditionException(@NotNull Component component, @NotNull BaseCondition condition, @NotNull Duration timeout){
        this.component = component;
        this.condition = condition;
        this.timeout = timeout;
    }

    @Override
    public String toString() {
        return String.format("%s :: Не выполнял условие %s на протяжении %d миллисекунд.", component, condition, timeout.toMillis());
    }
}
