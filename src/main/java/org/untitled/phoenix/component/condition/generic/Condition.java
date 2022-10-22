package org.untitled.phoenix.component.condition.generic;

import org.untitled.phoenix.component.Requirement;
import org.untitled.phoenix.component.Component;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.Function;

public final class Condition<TComponent extends Component> extends org.untitled.phoenix.component.condition.Condition {

    private final @NotNull Function<@NotNull TComponent, @Nullable Object> condition;

    private final @NotNull TComponent component;

    public Condition(@NotNull TComponent component, @NotNull Requirement<TComponent> requirement) {
        super(component, requirement.getValue(), requirement.getDescription());
        this.condition = requirement.getFunction();
        this.component = component;
    }

    @Override
    public boolean isTrue() {
        return Objects.equals(condition.apply(component), getValue());
    }
}
