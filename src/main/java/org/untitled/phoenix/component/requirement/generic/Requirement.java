package org.untitled.phoenix.component.requirement.generic;

import org.untitled.phoenix.component.requirement.BaseRequirement;
import org.untitled.phoenix.component.Component;

import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;
import org.untitled.phoenix.component.requirement.LinearRequirement;
import org.untitled.phoenix.component.requirement.Operation;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.Objects;

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
            return new Requirement<>(component -> component.toAction().getCssId(), value, "Содержит идентификатор", String::contains);
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
            return new Requirement<>(component -> component.toAction().getCssId(), value, "Имеет идентификатор");
        }

        public static <TComponent extends Component> @NotNull BaseRequirement<TComponent> byText(@Nullable String value) {
            return new Requirement<>(component -> component.toAction().getText(), value, "Имеет текст");
        }
    }

    private final @NotNull List<LinearRequirement<TComponent>> requirements = new ArrayList<>();

    private final @NotNull BiPredicate<@Nullable TValue, @Nullable TValue> condition;

    private final @NotNull Function<@NotNull TComponent, @Nullable TValue> function;

    private final @NotNull String description;

    private final @Nullable TValue value;

    public Requirement(@NotNull Function<@NotNull TComponent, @Nullable TValue> function, @Nullable TValue value, @NotNull String description) {
        this(function, value, description, Objects::equals);
    }

    public Requirement(@NotNull Function<@NotNull TComponent, @Nullable TValue> function, @Nullable TValue value, @NotNull String description, @NotNull BiPredicate<@Nullable TValue, @Nullable TValue> condition) {
        this(function, value, description, condition, false);
    }

    private Requirement(@NotNull Function<@NotNull TComponent, @Nullable TValue> function, @Nullable TValue value, @NotNull String description, @NotNull BiPredicate<@Nullable TValue, @Nullable TValue> condition, boolean isNegative) {
        super(isNegative);
        this.description = description;
        this.condition = condition;
        this.function = function;
        this.value = value;
    }

    private Requirement(@NotNull Requirement<TComponent, TValue> requirement, boolean isNegative) {
        super(isNegative);
        this.requirements.addAll(requirement.requirements);
        this.description = requirement.description;
        this.condition = requirement.condition;
        this.function = requirement.function;
        this.value = requirement.value;
    }

    private Requirement(@NotNull Requirement<TComponent, TValue> requirement, @NotNull Operation operation, @NotNull BaseRequirement<TComponent> baseRequirement) {
        super(requirement.isNegative());
        requirements.addAll(requirement.requirements);
        this.description = requirement.description;
        this.condition = requirement.condition;
        this.function = requirement.function;
        this.value = null;

        if (requirements.isEmpty()) {
            final var instance = new Requirement<>(requirement, isNegative());
            final var base = new Requirement<>(this, isNegative());

            base.requirements.add(new LinearRequirement<>(operation, baseRequirement));
            base.requirements.add(0, new LinearRequirement<>(Operation.AND, instance));
            requirements.add(new LinearRequirement<>(operation, base));
        } else
            requirements.add(new LinearRequirement<>(operation, baseRequirement));

        var s = 0;
    }

    public static <TComponent extends Component> @NotNull BaseRequirement<TComponent> byAvailable(boolean isAvailable) {
        return new Requirement<>(TComponent::isAvailable, isAvailable, "Доступен");
    }

    public static <TComponent extends Component> @NotNull BaseRequirement<TComponent> byDisplayed(boolean isDisplayed) {
        return new Requirement<>(component -> component.toAction().isDisplayed(), isDisplayed, "Отображается");
    }

    public static <TComponent extends Component> @NotNull BaseRequirement<TComponent> byReadonly(boolean isReadonly) {
        return new Requirement<>(component -> component.toAction().isReadonly(), isReadonly, "Только для чтения");
    }

    public static <TComponent extends Component> @NotNull BaseRequirement<TComponent> bySelected(boolean isSelected) {
        return new Requirement<>(component -> component.toAction().isSelected(), isSelected, "Выделен");
    }

    public static <TComponent extends Component> @NotNull BaseRequirement<TComponent> byEnabled(boolean isEnabled) {
        return new Requirement<>(component -> component.toAction().isEnabled(), isEnabled, "Включён");
    }

    @Override
    public boolean isTrue(@NotNull TComponent component) {

        if (requirements.isEmpty())
            return condition.test(function.apply(component), value) != isNegative();

        if (requirements.size() == 1)
            return switch (requirements.get(0).getOperation()) {
                case AND -> (condition.test(function.apply(component), value) && requirements.get(0).getRequirement().isTrue(component)) != isNegative();
                case OR -> (condition.test(function.apply(component), value) || requirements.get(0).getRequirement().isTrue(component)) != isNegative();
            };

        var result = requirements.get(0).getRequirement().isTrue(component);

        for (var index = 1; index < requirements.size(); index++)
            result = switch (requirements.get(index).getOperation()) {
                case AND -> result && requirements.get(index).getRequirement().isTrue(component);
                case OR -> result || requirements.get(index).getRequirement().isTrue(component);
            };

        return result != isNegative();
    }

    @Override
    public @NotNull BaseRequirement<TComponent> toNegative() {
        return new Requirement<>(this, !isNegative());
    }

    @Override
    public @NotNull BaseRequirement<TComponent> and(@NotNull BaseRequirement<TComponent> requirement) {
        return new Requirement<>(this, Operation.AND, requirement);
    }

    @Override
    public @NotNull BaseRequirement<TComponent> or(@NotNull BaseRequirement<TComponent> requirement) {
        return new Requirement<>(this, Operation.OR, requirement);
    }

    @Override
    public @NotNull String toString() {
        if (requirements.isEmpty())
            if (isNegative()) return String.format("НЕ '%s => %s'", description, value);
            else return String.format("'%s => %s'", description, value);
        else {
            final var descriptions = new ArrayList<>(requirements.stream().skip(1).map(requirement -> switch (requirement.getOperation()) {
                case OR -> String.format("ИЛИ %s", requirement.getRequirement());
                case AND -> String.format("И %s", requirement.getRequirement());
            }).toList());
            descriptions.add(0, requirements.get(0).getRequirement().toString());
            return requirements.size() == 1 ? String.join(" ", descriptions) : String.format("(%s)", String.join(" ", descriptions));
        }
    }
}
