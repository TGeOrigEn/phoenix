package org.untitled.phoenix.component.requirement;

import org.untitled.phoenix.component.Component;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class BaseRequirement<TComponent extends Component> {

    private final @NotNull List<Operation<TComponent>> operations;

    private final boolean isNegative;

    protected BaseRequirement(boolean isNegative) {
        operations = Collections.unmodifiableList(new ArrayList<>());
        this.isNegative = isNegative;
    }

    protected BaseRequirement(@NotNull BaseRequirement<TComponent> requirement, boolean isNegative) {
        operations = requirement.operations;
        this.isNegative = isNegative;
    }

    protected BaseRequirement(@NotNull BaseRequirement<TComponent> requirementA, @NotNull Operator operation, @NotNull BaseRequirement<TComponent> requirementB) {
        final var requirements = new ArrayList<Operation<TComponent>>();

        if (!requirementA.operations.isEmpty() && !requirementB.operations.isEmpty()) {
            if (!requirementA.operations.get(0).getRequirement().operations.isEmpty())
                requirements.addAll(requirementA.operations);
            else requirements.add(new Operation<>(Operator.AND, requirementA));

            requirements.add(new Operation<>(operation, requirementB));
        }

        if (requirementA.operations.isEmpty() && requirementB.operations.isEmpty()) {
            requirements.add(new Operation<>(Operator.AND, requirementA));
            requirements.add(new Operation<>(operation, requirementB));
        }

        if (!requirementA.operations.isEmpty() && requirementB.operations.isEmpty()) {
            if (requirementA.isNegative())
                requirements.add(new Operation<>(operation, requirementA));
            else requirements.addAll(requirementA.operations);

            requirements.add(new Operation<>(operation, requirementB));
        }

        if (requirementA.operations.isEmpty() && !requirementB.operations.isEmpty()) {
            if (requirementB.isNegative())
                requirements.add(new Operation<>(operation, requirementB));
            else requirements.addAll(requirementB.operations);

            requirements.add(new Operation<>(operation, requirementA));
        }

        this.operations = Collections.unmodifiableList(requirements);
        isNegative = false;
    }

    public abstract @NotNull BaseRequirement<TComponent> and(@NotNull BaseRequirement<TComponent> requirement);

    public abstract @NotNull BaseRequirement<TComponent> or(@NotNull BaseRequirement<TComponent> requirement);

    public abstract @NotNull BaseRequirement<TComponent> toNegative();

    public abstract boolean isTrue(@NotNull TComponent component);

    public final @NotNull List<Operation<TComponent>> getOperations() {
        return operations;
    }

    public final boolean isNegative() {
        return isNegative;
    }
}
