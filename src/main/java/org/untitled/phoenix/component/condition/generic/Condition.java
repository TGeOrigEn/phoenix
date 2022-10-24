package org.untitled.phoenix.component.condition.generic;

import org.jetbrains.annotations.Contract;
import org.untitled.phoenix.component.requirement.BaseRequirement;
import org.untitled.phoenix.component.condition.BaseCondition;
import org.untitled.phoenix.component.Component;

import org.jetbrains.annotations.NotNull;

public final class Condition<TComponent extends Component> extends BaseCondition {

    private final @NotNull BaseRequirement<TComponent> requirement;

    private final @NotNull TComponent component;

    public Condition(@NotNull TComponent component, @NotNull BaseRequirement<TComponent> requirement) {
        super(component, requirement.getValue(), requirement.getDescription());
        this.requirement = requirement;
        this.component = component;
    }

    @Override
    public boolean isTrue() {
        return requirement.isTrue(component);
    }

    @Override
    public @NotNull String toString() {
        return requirement.getCompletedDescription();
    }
}
