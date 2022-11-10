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

            public static @NotNull BaseRequirement<Field> byValue(@NotNull String value) {
                return new Requirement<>(Field::getValue, value, "Имеет значение");
            }

            public static @NotNull BaseRequirement<Field> byTitle(@NotNull String title) {
                return new Requirement<>(Field::getTitle, title, "Имеет заголовок");
            }
        }

        public static class Contains {

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
    }

    public static final @NotNull Description DEFAULT_DESCRIPTION = new Description(By.cssSelector("div[class*=x-field]"), "Поле");

    private static final @NotNull Description LABEL_DESCRIPTION = new Description(By.cssSelector("span[class*='x-form-item-label-inner']"), "Лейбл");

    private static final @NotNull Description REQUIRED_DESCRIPTION = new Description(By.cssSelector("div[class*='x-form-required-field']"), "Обязательность");

    protected final @NotNull Component required;

    protected final @NotNull Component label;

    public Field() {
        required = findInside(() -> new WebComponent(REQUIRED_DESCRIPTION));
        label = findInside(() -> new WebComponent(LABEL_DESCRIPTION));
    }

    protected abstract @NotNull Component getInput();

    @Override
    protected @NotNull Description initialize() {
        return DEFAULT_DESCRIPTION;
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

    public @NotNull String getValue() {
        return getInput().toAction().getValue();
    }

    public boolean isReadonly() {
        return getInput().toAction().isReadonly();
    }
}
