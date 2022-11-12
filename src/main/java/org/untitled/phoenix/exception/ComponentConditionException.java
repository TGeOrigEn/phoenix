package org.untitled.phoenix.exception;

import org.untitled.phoenix.component.condition.BaseCondition;
import org.untitled.phoenix.component.Component;

import org.jetbrains.annotations.NotNull;

import java.time.Duration;

public class ComponentConditionException extends ComponentException {

    public ComponentConditionException(@NotNull Component component, @NotNull BaseCondition condition, @NotNull Duration timeout) {
        super(component, String.format("%s на протяжении %d миллисекунд", condition, timeout.toMillis()));
    }
}
