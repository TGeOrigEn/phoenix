package org.untitled.phoenix.component.requirement.generic;

import org.untitled.phoenix.component.requirement.BaseRequirement;
import org.untitled.phoenix.component.Component;

import org.untitled.phoenix.component.requirement.Operation;

import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;

import java.util.function.*;
import java.util.*;

public final class Requirement<TComponent extends Component, TValue> extends BaseRequirement<TComponent> {

    public static class Contains {

        public static <TComponent extends Component> @NotNull BaseRequirement<TComponent> byAttribute(@NotNull String name, @Nullable String value) {
            return new Requirement<>(component -> component.toAction().getAttribute(name), value, String.format("Содержит значение атрибута '%s'", name), String::contains);
        }

        public static <TComponent extends Component> @NotNull BaseRequirement<TComponent> byCssClass(@Nullable String value) {
            return new Requirement<>(component -> component.toAction().getCssClass(), value, "Содержит значение класса", String::contains);
        }

        public static <TComponent extends Component> @NotNull BaseRequirement<TComponent> byValue(@Nullable String value) {
            return new Requirement<>(component -> component.toAction().getValue(), value, "Содержит значение", String::contains);
        }

        public static <TComponent extends Component> @NotNull BaseRequirement<TComponent> byCssId(@Nullable String value) {
            return new Requirement<>(component -> component.toAction().getAttribute("id"), value, "Содержит идентификатор", String::contains);
        }

        public static <TComponent extends Component> @NotNull BaseRequirement<TComponent> byText(@Nullable String text) {
            return new Requirement<>(component -> component.toAction().getText(), text, "Содержит текст", String::contains);
        }
    }

    public static class Equals {

        public static <TComponent extends Component> @NotNull BaseRequirement<TComponent> byAttribute(@NotNull String name, @Nullable String value) {
            return new Requirement<>(component -> component.toAction().getAttribute(name), value, String.format("Имеет значение атрибута '%s'", name));
        }

        public static <TComponent extends Component> @NotNull BaseRequirement<TComponent> byCssClass(@Nullable String value) {
            return new Requirement<>(component -> component.toAction().getCssClass(), value, "Имеет значение класса");
        }

        public static <TComponent extends Component> @NotNull BaseRequirement<TComponent> byValue(@Nullable String value) {
            return new Requirement<>(component -> component.toAction().getValue(), value, "Имеет значение");
        }

        public static <TComponent extends Component> @NotNull BaseRequirement<TComponent> byCssId(@Nullable String value) {
            return new Requirement<>(component -> component.toAction().getAttribute("id"), value, "Имеет идентификатор");
        }

        public static <TComponent extends Component> @NotNull BaseRequirement<TComponent> byText(@Nullable String value) {
            return new Requirement<>(component -> component.toAction().getText(), value, "Имеет текст");
        }
    }

    private final @Nullable Function<@NotNull TComponent, TValue> function;

    private final @Nullable BiPredicate<TValue, TValue> condition;

    private final @Nullable String description;

    private final @Nullable TValue value;

    public Requirement(@NotNull Function<@NotNull TComponent, TValue> function, @Nullable TValue value, @NotNull String description) {
        this(function, value, description, Objects::equals);
    }

    public Requirement(@NotNull Function<@NotNull TComponent, TValue> function, @Nullable TValue value, @NotNull String description, @NotNull BiPredicate<TValue, TValue> condition) {
        super(false);
        this.description = description;
        this.condition = condition;
        this.function = function;
        this.value = value;
    }

    private Requirement(@NotNull Requirement<TComponent, TValue> requirement, boolean isNegative) {
        super(requirement, isNegative);
        this.description = requirement.description;
        this.condition = requirement.condition;
        this.function = requirement.function;
        this.value = requirement.value;
    }

    private Requirement(@NotNull Requirement<TComponent, TValue> requirement, @NotNull Operation.Operator operation, @NotNull BaseRequirement<TComponent> baseRequirement) {
        super(requirement, operation, baseRequirement);
        this.description = null;
        this.condition = null;
        this.function = null;
        this.value = null;
    }

    public static <TComponent extends Component> @NotNull BaseRequirement<TComponent> isAvailable(boolean isAvailable) {
        return new Requirement<>(TComponent::isAvailable, isAvailable, "Является доступным");
    }

    public static <TComponent extends Component> @NotNull BaseRequirement<TComponent> isDisplayed(boolean isDisplayed) {
        return new Requirement<>(component -> component.toAction().isDisplayed(), isDisplayed, "Отображается");
    }

    public static <TComponent extends Component> @NotNull BaseRequirement<TComponent> isReadonly(boolean isReadonly) {
        return new Requirement<>(component -> component.toAction().isReadonly(), isReadonly, "Только для чтения");
    }

    public static <TComponent extends Component> @NotNull BaseRequirement<TComponent> isSelected(boolean isSelected) {
        return new Requirement<>(component -> component.toAction().isSelected(), isSelected, "Является выделенным");
    }

    public static <TComponent extends Component> @NotNull BaseRequirement<TComponent> isEnabled(boolean isEnabled) {
        return new Requirement<>(component -> component.toAction().isEnabled(), isEnabled, "Является включённым");
    }

    @Override
    public boolean isTrue(@NotNull TComponent component) {

        if (getOperations().isEmpty())
            if (condition != null && function != null)
                return condition.test(function.apply(component), value) != isNegative();
            else throw new RuntimeException();

        if (getOperations().size() == 1)
            return getOperations().get(0).getRequirement().isTrue(component) != isNegative();

        var isTrue = getOperations().get(0).getRequirement().isTrue(component);

        for (var index = 1; index < getOperations().size(); index++)
            switch (getOperations().get(index).getOperator()) {
                case AND:
                    isTrue = isTrue && getOperations().get(index).getRequirement().isTrue(component);
                case OR:
                    isTrue = isTrue || getOperations().get(index).getRequirement().isTrue(component);
            }

        return isTrue != isNegative();
    }

    @Override
    public @NotNull BaseRequirement<TComponent> toNegative() {
        return new Requirement<>(this, !isNegative());
    }

    @Override
    public @NotNull BaseRequirement<TComponent> and(@NotNull BaseRequirement<TComponent> requirement) {
        return new Requirement<>(this, Operation.Operator.AND, requirement);
    }

    @Override
    public @NotNull BaseRequirement<TComponent> or(@NotNull BaseRequirement<TComponent> requirement) {
        return new Requirement<>(this, Operation.Operator.OR, requirement);
    }

    @Override
    public @NotNull String toString() {
        if (getOperations().isEmpty())
            if (isNegative()) return String.format("НЕ '%s => %s'", description, value);
            else return String.format("'%s => %s'", description, value);
        else {
            final var descriptions = new ArrayList<>(List.of(getOperations().stream().skip(1).map(requirement -> {
                if (requirement.getOperator() == Operation.Operator.OR)
                    return String.format("ИЛИ %s", requirement.getRequirement());
                else return String.format("И %s", requirement.getRequirement());
            }).toArray(String[]::new)));

            descriptions.add(0, getOperations().get(0).getRequirement().toString());

            return isNegative()
                    ? String.format("НЕ (%s)", String.join(" ", descriptions))
                    : String.format("(%s)", String.join(" ", descriptions));
        }
    }
}
