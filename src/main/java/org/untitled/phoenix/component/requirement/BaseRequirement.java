package org.untitled.phoenix.component.requirement;

import org.untitled.phoenix.component.Component;

import org.jetbrains.annotations.NotNull;
import org.untitled.phoenix.component.requirement.generic.Requirement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class BaseRequirement<TComponent extends Component> {

    private final @NotNull List<LinearRequirement<TComponent>> requirements;

    private final boolean isNegative;

    protected BaseRequirement(boolean isNegative) {
        requirements = Collections.unmodifiableList(new ArrayList<>());
        this.isNegative = isNegative;
    }

    protected BaseRequirement(@NotNull BaseRequirement<TComponent> requirement, boolean isNegative) {
        requirements = requirement.requirements;
        this.isNegative = isNegative;
    }

    protected BaseRequirement(@NotNull BaseRequirement<TComponent> requirementA, @NotNull Operation operation, @NotNull BaseRequirement<TComponent> requirementB) {
        final var requirements = new ArrayList<LinearRequirement<TComponent>>();

        if (!requirementA.requirements.isEmpty() && !requirementB.requirements.isEmpty()) {
            if (!requirementA.requirements.get(0).getRequirement().requirements.isEmpty())
                requirements.addAll(requirementA.requirements);
            else requirements.add(new LinearRequirement<>(Operation.AND, requirementA));

            requirements.add(new LinearRequirement<>(operation, requirementB));
        }

        if (requirementA.requirements.isEmpty() && requirementB.requirements.isEmpty()) {
            requirements.add(new LinearRequirement<>(Operation.AND, requirementA));
            requirements.add(new LinearRequirement<>(operation, requirementB));
        }

        if (!requirementA.requirements.isEmpty() && requirementB.requirements.isEmpty()) {
            if (requirementA.isNegative())
                requirements.add(new LinearRequirement<>(operation, requirementA));
            else requirements.addAll(requirementA.requirements);

            requirements.add(new LinearRequirement<>(operation, requirementB));
        }

        if (requirementA.requirements.isEmpty() && !requirementB.requirements.isEmpty()) {
            if (requirementB.isNegative())
                requirements.add(new LinearRequirement<>(operation, requirementB));
            else requirements.addAll(requirementB.requirements);

            requirements.add(new LinearRequirement<>(operation, requirementA));
        }

        this.requirements = Collections.unmodifiableList(requirements);
        isNegative = false;
    }

    public abstract @NotNull BaseRequirement<TComponent> and(@NotNull BaseRequirement<TComponent> requirement);

    public abstract @NotNull BaseRequirement<TComponent> or(@NotNull BaseRequirement<TComponent> requirement);

    public abstract @NotNull BaseRequirement<TComponent> toNegative();

    public abstract boolean isTrue(@NotNull TComponent component);

    public final @NotNull List<LinearRequirement<TComponent>> getRequirements() {
        return requirements;
    }

    public final boolean isNegative() {
        return isNegative;
    }
}
