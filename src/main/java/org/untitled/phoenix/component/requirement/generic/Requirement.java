package org.untitled.phoenix.component.requirement.generic;

import org.untitled.phoenix.component.requirement.BaseRequirement;
import org.untitled.phoenix.component.requirement.Operator;
import org.untitled.phoenix.component.Component;

import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.ArrayList;
import java.util.Objects;

public final class Requirement<TComponent extends Component, TValue> extends BaseRequirement<TComponent> {

    public static class Contains {

        public static <TComponent extends Component> @NotNull BaseRequirement<TComponent> byAttribute(@NotNull String name, @Nullable String value) {
            return new Requirement<>(component -> component.getAction().getAttribute(name), value, String.format("Содержит значение атрибута '%s'", name), String::contains);
        }

        public static <TComponent extends Component> @NotNull BaseRequirement<TComponent> byCssClass(@Nullable String value) {
            return new Requirement<>(component -> component.getAction().getAttribute("class"), value, "Содержит значение класса", String::contains);
        }

        public static <TComponent extends Component> @NotNull BaseRequirement<TComponent> byValue(@Nullable String value) {
            return new Requirement<>(component -> component.getAction().getValue(), value, "Содержит значение", String::contains);
        }

        public static <TComponent extends Component> @NotNull BaseRequirement<TComponent> byCssId(@Nullable String value) {
            return new Requirement<>(component -> component.getAction().getAttribute("id"), value, "Содержит идентификатор", String::contains);
        }

        public static <TComponent extends Component> @NotNull BaseRequirement<TComponent> byText(@Nullable String text) {
            return new Requirement<>(component -> component.getAction().getText(), text, "Содержит текст", String::contains);
        }
    }

    public static class Equals {

        public static <TComponent extends Component> @NotNull BaseRequirement<TComponent> byAttribute(@NotNull String name, @Nullable String value) {
            return new Requirement<>(component -> component.getAction().getAttribute(name), value, String.format("Имеет значение атрибута '%s'", name));
        }

        public static <TComponent extends Component> @NotNull BaseRequirement<TComponent> byCssClass(@Nullable String value) {
            return new Requirement<>(component -> component.getAction().getAttribute("class"), value, "Имеет значение класса");
        }

        public static <TComponent extends Component> @NotNull BaseRequirement<TComponent> byValue(@Nullable String value) {
            return new Requirement<>(component -> component.getAction().getValue(), value, "Имеет значение");
        }

        public static <TComponent extends Component> @NotNull BaseRequirement<TComponent> byCssId(@Nullable String value) {
            return new Requirement<>(component -> component.getAction().getAttribute("id"), value, "Имеет идентификатор");
        }

        public static <TComponent extends Component> @NotNull BaseRequirement<TComponent> byText(@Nullable String value) {
            return new Requirement<>(component -> component.getAction().getText(), value, "Имеет текст");
        }
    }

    private final @Nullable BiPredicate<@Nullable TValue, @Nullable TValue> condition;

    private final @Nullable Function<@NotNull TComponent, @Nullable TValue> function;

    private final @Nullable String description;

    private final @Nullable TValue value;

    public Requirement(@NotNull Function<@NotNull TComponent, @Nullable TValue> function, @Nullable TValue value, @NotNull String description) {
        this(function, value, description, Objects::equals);
    }

    public Requirement(@NotNull Function<@NotNull TComponent, @Nullable TValue> function, @Nullable TValue value, @NotNull String description, @NotNull BiPredicate<@Nullable TValue, @Nullable TValue> condition) {
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

    private Requirement(@NotNull Requirement<TComponent, TValue> requirement, @NotNull Operator operation, @NotNull BaseRequirement<TComponent> baseRequirement) {
        super(requirement, operation, baseRequirement);
        this.description = null;
        this.condition = null;
        this.function = null;
        this.value = null;
    }

    public static <TComponent extends Component> @NotNull BaseRequirement<TComponent> byAvailable(boolean isAvailable) {
        return new Requirement<>(TComponent::isAvailable, isAvailable, "Доступен");
    }

    public static <TComponent extends Component> @NotNull BaseRequirement<TComponent> byDisplayed(boolean isDisplayed) {
        return new Requirement<>(component -> component.getAction().isDisplayed(), isDisplayed, "Отображается");
    }

    public static <TComponent extends Component> @NotNull BaseRequirement<TComponent> byReadonly(boolean isReadonly) {
        return new Requirement<>(component -> component.getAction().isReadonly(), isReadonly, "Только для чтения");
    }

    public static <TComponent extends Component> @NotNull BaseRequirement<TComponent> bySelected(boolean isSelected) {
        return new Requirement<>(component -> component.getAction().isSelected(), isSelected, "Выделен");
    }

    public static <TComponent extends Component> @NotNull BaseRequirement<TComponent> byEnabled(boolean isEnabled) {
        return new Requirement<>(component -> component.getAction().isEnabled(), isEnabled, "Включён");
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
            isTrue = switch (getOperations().get(index).getOperator()) {
                case AND -> isTrue && getOperations().get(index).getRequirement().isTrue(component);
                case OR -> isTrue || getOperations().get(index).getRequirement().isTrue(component);
            };

        return isTrue != isNegative();
    }

    @Override
    public @NotNull BaseRequirement<TComponent> toNegative() {
        return new Requirement<>(this, !isNegative());
    }

    @Override
    public @NotNull BaseRequirement<TComponent> and(@NotNull BaseRequirement<TComponent> requirement) {
        return new Requirement<>(this, Operator.AND, requirement);
    }

    @Override
    public @NotNull BaseRequirement<TComponent> or(@NotNull BaseRequirement<TComponent> requirement) {
        return new Requirement<>(this, Operator.OR, requirement);
    }

    @Override
    public @NotNull String toString() {
        if (getOperations().isEmpty())
            if (isNegative()) return String.format("НЕ '%s => %s'", description, value);
            else return String.format("'%s => %s'", description, value);
        else {
            final var descriptions = new ArrayList<>(getOperations().stream().skip(1).map(requirement -> switch (requirement.getOperator()) {
                case OR -> String.format("ИЛИ %s", requirement.getRequirement());
                case AND -> String.format("И %s", requirement.getRequirement());
            }).toList());

            descriptions.add(0, getOperations().get(0).getRequirement().toString());

            return isNegative()
                    ? String.format("НЕ (%s)", String.join(" ", descriptions))
                    : String.format("(%s)", String.join(" ", descriptions));
        }
    }
}
