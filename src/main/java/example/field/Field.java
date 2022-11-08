package example.field;

import example.Button;
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

            public static @NotNull BaseRequirement<Field> byTitle(@NotNull String title) {
                return new Requirement<>(Field::getTitle, title, "Имеет заголовок");
            }
        }

        public static class Contains {

            public static @NotNull BaseRequirement<Field> byTitle(@NotNull String title) {
                return new Requirement<>(Field::getTitle, title, "Имеет заголовок", String::contains);
            }
        }
    }

    public static final Description DEFAULT_DESCRIPTION = new Description(By.cssSelector("div[class*=x-field]"), "Поле");

    private static final Description LABEL_DESCRIPTION = new Description(By.cssSelector("span[class*='x-form-item-label-inner']"), "Лейбл");

    private static final Description REQUIRED_DESCRIPTION = new Description(By.cssSelector("div[class*='x-form-required-field']"), "Обязательность");

    protected final Component required;

    protected final Component label;

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
