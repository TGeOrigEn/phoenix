package org.untitled.phoenix.component.requirement.generic;

import org.untitled.phoenix.component.requirement.BaseRequirement;
import org.untitled.phoenix.component.Component;

import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;

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

    private final @NotNull BiPredicate<@Nullable TValue, @Nullable TValue> condition;

    private final @NotNull Function<@NotNull TComponent, @Nullable TValue> function;

    private final @Nullable TValue value;

    public Requirement(@NotNull Function<@NotNull TComponent, @Nullable TValue> function, @Nullable TValue value, @NotNull String description) {
        this(function, value, description, Objects::equals);
    }

    public Requirement(@NotNull Function<@NotNull TComponent, @Nullable TValue> function, @Nullable TValue value, @NotNull String description, @NotNull BiPredicate<@Nullable TValue, @Nullable TValue> condition) {
        super(value, description);
        this.condition = condition;
        this.function = function;
        this.value = value;
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
    public boolean isTrue(TComponent component) {
        return getRequirement() == null
                ? condition.test(function.apply(component), value) != isNegative()
                : getOperation() == Operation.AND
                ? condition.test(function.apply(component), value) != isNegative() && getRequirement().isTrue(component)
                : condition.test(function.apply(component), value) != isNegative() || getRequirement().isTrue(component);
    }
}
