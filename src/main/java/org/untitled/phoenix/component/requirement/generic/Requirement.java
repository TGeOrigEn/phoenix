package org.untitled.phoenix.component.requirement.generic;

import org.untitled.phoenix.component.requirement.BaseRequirement;
import org.untitled.phoenix.component.Component;

import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.Objects;

public final class Requirement<TComponent extends Component, TValue> extends BaseRequirement<TComponent> {

    private enum Operation {OR, AND}

    public static class Contains {

        public static @NotNull Requirement<Component, String> byAttribute(@NotNull String name, @Nullable String value) {
            return new Requirement<>(component -> component.toAction().getAttribute(name), value, String.format("Содержит значение атрибута '%s'", name), String::contains);
        }

        public static @NotNull Requirement<Component, String> byCssClass(@Nullable String value) {
            return new Requirement<>(component -> component.toAction().getCssClass(), value, "Содержит значение класса", String::contains);
        }

        public static @NotNull Requirement<Component, String> byValue(@Nullable String value) {
            return new Requirement<>(component -> component.toAction().getValue(), value, "Содержит значение", String::contains);
        }

        public static @NotNull Requirement<Component, String> byCssId(@Nullable String value) {
            return new Requirement<>(component -> component.toAction().getCssId(), value, "Содержит идентификатор", String::contains);
        }

        public static @NotNull Requirement<Component, String> byText(@Nullable String text) {
            return new Requirement<>(component -> component.toAction().getText(), text, "Содержит текст", String::contains);
        }
    }

    public static class Equals {

        public static @NotNull Requirement<Component, String> byAttribute(@NotNull String name, @Nullable String value) {
            return new Requirement<>(component -> component.toAction().getAttribute(name), value, String.format("Имеет значение атрибута '%s'", name));
        }

        public static @NotNull Requirement<Component, String> byCssClass(@Nullable String value) {
            return new Requirement<>(component -> component.toAction().getCssClass(), value, "Имеет значение класса");
        }

        public static @NotNull Requirement<Component, String> byValue(@Nullable String value) {
            return new Requirement<>(component -> component.toAction().getValue(), value, "Имеет значение");
        }

        public static @NotNull Requirement<Component, String> byCssId(@Nullable String value) {
            return new Requirement<>(component -> component.toAction().getCssId(), value, "Имеет идентификатор");
        }

        public static @NotNull Requirement<Component, String> byText(@Nullable String value) {
            return new Requirement<>(component -> component.toAction().getText(), value, "Имеет текст");
        }
    }

    private final @NotNull BiPredicate<@Nullable TValue, @Nullable TValue> condition;

    private final @NotNull Function<@NotNull TComponent, @Nullable TValue> function;

    private final @Nullable BaseRequirement<TComponent> requirement;

    private final @NotNull Operation operation;

    private final @Nullable TValue value;

    public Requirement(@NotNull Function<@NotNull TComponent, @Nullable TValue> function, @Nullable TValue value, @NotNull String description) {
        this(function, value, description, Objects::equals, null, Operation.OR, false);
    }

    public Requirement(@NotNull Function<@NotNull TComponent, @Nullable TValue> function, @Nullable TValue value, @NotNull String description, @NotNull BiPredicate<@Nullable TValue, @Nullable TValue> condition) {
        this(function, value, description, condition, null, Operation.OR, false);
    }

    private Requirement(@NotNull Function<@NotNull TComponent, @Nullable TValue> function, @Nullable TValue value, @NotNull String description, @NotNull BiPredicate<@Nullable TValue, @Nullable TValue> condition, @Nullable BaseRequirement<TComponent> requirement, @NotNull Operation operation, boolean negative) {
        super(value, description, negative);
        this.requirement = requirement;
        this.condition = condition;
        this.operation = operation;
        this.function = function;
        this.value = value;
    }

    public static @NotNull Requirement<Component, Boolean> byAvailable(boolean isAvailable) {
        return new Requirement<>(Component::isAvailable, isAvailable, "Доступен");
    }

    public static @NotNull Requirement<Component, Boolean> byDisplayed(boolean isDisplayed) {
        return new Requirement<>(component -> component.toAction().isDisplayed(), isDisplayed, "Отображается");
    }

    public static @NotNull Requirement<Component, Boolean> byReadonly(boolean isReadonly) {
        return new Requirement<>(component -> component.toAction().isReadonly(), isReadonly, "Только для чтения");
    }

    public static @NotNull Requirement<Component, Boolean> bySelected(boolean isSelected) {
        return new Requirement<>(component -> component.toAction().isSelected(), isSelected, "Выделен");
    }

    public static @NotNull Requirement<Component, Boolean> byEnabled(boolean isEnabled) {
        return new Requirement<>(component -> component.toAction().isEnabled(), isEnabled, "Включён");
    }

    @Override
    public boolean isTrue(TComponent component) {
        return requirement == null
                ? condition.test(function.apply(component), value) != isNegative() : operation == Operation.AND
                ? condition.test(function.apply(component), value) != isNegative() && requirement.isTrue(component)
                : condition.test(function.apply(component), value) != isNegative() || requirement.isTrue(component);
    }

    @Override
    public @NotNull BaseRequirement<TComponent> and(BaseRequirement<TComponent> requirement) {
        return new Requirement<>(function, value, String.format("%s и %s", getDescription(), requirement.getDescription()), condition, requirement, Operation.AND, false);
    }

    @Override
    public @NotNull BaseRequirement<TComponent> or(BaseRequirement<TComponent> requirement) {
        return new Requirement<>(function, value, String.format("%s или %s", getDescription(), requirement.getDescription()), condition, requirement, Operation.OR, false);
    }

    @Override
    public @NotNull BaseRequirement<TComponent> toNegative() {
        return new Requirement<>(function, value, getDescription(), condition, requirement, operation, !isNegative());
    }
}
