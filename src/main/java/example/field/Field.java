package example.field;

import org.gems.WebComponent;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.By;
import org.untitled.phoenix.component.Component;
import org.untitled.phoenix.component.Description;
import org.untitled.phoenix.component.requirement.BaseRequirement;
import org.untitled.phoenix.component.requirement.generic.Requirement;

public abstract class Field extends Component {

    public static class Requirements {

        public static class Equals {

            public static @NotNull BaseRequirement<Field> byPlaceholder(@NotNull String placeholder) {
                return new Requirement<>(Field::getPlaceholder, placeholder, "Имеет шаблонный текст");
            }

            public static @NotNull BaseRequirement<Field> byValue(@NotNull String value) {
                return new Requirement<>(Field::getValue, value, "Имеет значение");
            }

            public static @NotNull BaseRequirement<Field> byTitle(@NotNull String title) {
                return new Requirement<>(Field::getTitle, title, "Имеет заголовок");
            }
        }

        public static class Contains {

            public static @NotNull BaseRequirement<Field> byPlaceholder(@NotNull String placeholder) {
                return new Requirement<>(Field::getPlaceholder, placeholder, "Содержит шаблонный текст", String::contains);
            }

            public static @NotNull BaseRequirement<Field> byValue(@NotNull String value) {
                return new Requirement<>(Field::getValue, value, "Имеет значение", String::contains);
            }

            public static @NotNull BaseRequirement<Field> byTitle(@NotNull String title) {
                return new Requirement<>(Field::getTitle, title, "Имеет заголовок", String::contains);
            }
        }

        public static @NotNull BaseRequirement<Field> isReadonly(boolean isReadonly) {
            return new Requirement<>(Field::isReadonly, isReadonly, "Является только для чтения");
        }

        public static @NotNull BaseRequirement<Field> isRequired(boolean isRequired) {
            return new Requirement<>(Field::isRequired, isRequired, "Является обязательным");
        }

        public static @NotNull BaseRequirement<Field> isCorrect(boolean isCorrect) {
            return new Requirement<>(example.field.Field::isRequired, isCorrect, "Является правильным");
        }
    }

    public static final @NotNull Description DEFAULT_DESCRIPTION = new Description(By.cssSelector("div[class*=x-field]:not([style*='display'])"), "Поле");

    private static final @NotNull Description LABEL_DESCRIPTION = new Description(By.cssSelector("span[class*='x-form-item-label-inner']"), "Название");

    private static final @NotNull Description REQUIRED_DESCRIPTION = new Description(By.cssSelector("div[class*='x-form-required-field']"), "Обязательность");

    private static final @NotNull Description INCORRECT_DESCRIPTION = new Description(By.cssSelector("div[role='alert']"), "Неправильность");

    protected final @NotNull Component incorrect;

    protected final @NotNull Component required;

    protected final @NotNull Component label;

    public Field() {
        incorrect = findInside(() -> new WebComponent(INCORRECT_DESCRIPTION));
        required = findInside(() -> new WebComponent(REQUIRED_DESCRIPTION));
        label = findInside(() -> new WebComponent(LABEL_DESCRIPTION));
    }

    protected abstract @NotNull Component getInput();

    @Override
    protected @NotNull Description initialize() {
        return DEFAULT_DESCRIPTION;
    }

    public @NotNull String getPlaceholder() {
        return getInput().toAction().getAttribute("placeholder");
    }

    public @NotNull String getTitle() {
        return label.toAction().getText();
    }

    public boolean isRequired() {
        return required.isAvailable();
    }

    public void setValue(@NotNull String value) {
        getInput().toAction().setValue(value);
    }

    public void sendKeys(@NotNull String value) { getInput().toAction().sendKeys(value); }

    public @NotNull String getValue() {
        return getInput().toAction().getValue();
    }

    public boolean isReadonly() {
        return getInput().toAction().isReadonly();
    }

    public boolean isCorrect() {
        return !incorrect.isAvailable();
    }
}
