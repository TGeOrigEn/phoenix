package org.untitled.phoenix.component;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public final class Requirement<TComponent extends Component> {

    private final Function<@NotNull TComponent, Object> function;

    private final @NotNull String description;

    private final @Nullable Object value;

    public Requirement(Function<@NotNull TComponent, @Nullable Object> function, @Nullable Object value, @NotNull String description) {
        this.description = description;
        this.function = function;
        this.value = value;
    }

    public static @NotNull Requirement<Component> byAvailable(boolean isAvailable) {
        return new Requirement<>(Component::isAvailable, isAvailable, "Доступен");
    }

    public static @NotNull Requirement<Component> byDisplayed(boolean isDisplayed) {
        return new Requirement<>(component -> component.toAction().isDisplayed(), isDisplayed, "Отображается");
    }

    public static @NotNull Requirement<Component> bySelected(boolean isSelected) {
        return new Requirement<>(component -> component.toAction().isSelected(), isSelected, "Выделен");
    }

    public static @NotNull Requirement<Component> byEnabled(boolean isEnabled) {
        return new Requirement<>(component -> component.toAction().isEnabled(), isEnabled, "Включён");
    }

    public static @NotNull Requirement<Component> byValue(String value){
        return new Requirement<>(component -> component.toAction().getValue(), value, "Имеет значение");
    }

    public static @NotNull Requirement<Component> byText(String text) {
        return new Requirement<>(component -> component.toAction().getText(), text, "Имеет текст");
    }

    public Function<@NotNull TComponent, @Nullable Object> getFunction() {
        return function;
    }

    public @NotNull String getDescription() {
        return description;
    }

    public @Nullable Object getValue() {
        return value;
    }
}
