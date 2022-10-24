package org.untitled.phoenix.component.requirement;

import org.untitled.phoenix.component.Component;

import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseRequirement<TComponent extends Component> {

    private @Nullable BaseRequirement<TComponent> requirement;

    private @NotNull Operation operation = Operation.AND;

    private final @NotNull List<LinearRequirement<TComponent>> requirements = new ArrayList<>();

    private final @NotNull String description;

    private final @Nullable Object value;

    private boolean negative = false;

    public BaseRequirement(@Nullable Object value, @NotNull String description) {
        this.description = description;
        this.value = value;
    }

    public static <TComponent extends Component> @NotNull BaseRequirement<TComponent> add(@NotNull BaseRequirement<TComponent> requirementA, @NotNull Operation operation, @NotNull BaseRequirement<TComponent> requirementB) {
        requirementA.requirement = requirementB;
        requirementA.operation = operation;
        return requirementA;
    }

    public final @NotNull BaseRequirement<TComponent> add(@NotNull Operation operation, @NotNull BaseRequirement<TComponent> requirement) {
        requirements.add(new LinearRequirement<>(operation, requirement));
        return this;
    }

    public final BaseRequirement<TComponent> toNegative() {
        negative = !negative;
        return this;
    }

    public abstract boolean isTrue(TComponent component);

    public final @Nullable BaseRequirement<TComponent> getRequirement() {
        return requirement;
    }

    public final @NotNull List<LinearRequirement<TComponent>> getRequirements() {
        return requirements;
    }

    public final @NotNull String getDescription() {
        return description;
    }

    public final @Nullable Object getValue() {
        return value;
    }

    public final @NotNull Operation getOperation() {
        return operation;
    }

    public final boolean isNegative() {
        return negative;
    }

    public final @NotNull String getCompletedDescription() {
       return String.join(" ", getCompletedDescription(this, new ArrayList<>()));
    }

    private static <TComponent extends Component> @NotNull List<String> getCompletedDescription(BaseRequirement<TComponent> requirement, List<String> buffer) {
        if (buffer == null)
            buffer = new ArrayList<>();

        buffer.add(requirement.toString());

        if (requirement.requirement != null)
            getCompletedDescription(requirement.requirement, buffer);

        return buffer;
    }

    @Override
    public String toString() {
        return negative
                ? String.format("(НЕ '%s => %s')", getDescription(), value)
                : String.format("('%s => %s')", getDescription(), value);
    }
}
