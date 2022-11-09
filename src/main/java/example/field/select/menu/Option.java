package example.field.select.menu;

import org.gems.WebComponent;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.By;
import org.untitled.phoenix.component.Component;
import org.untitled.phoenix.component.Description;
import org.untitled.phoenix.component.requirement.BaseRequirement;
import org.untitled.phoenix.component.requirement.generic.Requirement;

public class Option extends Component {

    public static class Requirements {

        public static class Equals {

            public static @NotNull BaseRequirement<Option> byText(@NotNull String text) {
                return new Requirement<>(Option::getText, text, "Имеет текст");
            }
        }

        public static class Contains {

            public static @NotNull BaseRequirement<Option> byText(@NotNull String text) {
                return new Requirement<>(Option::getText, text, "Содержит текст", String::contains);
            }
        }
    }

    public static final @NotNull Description DEFAULT_DESCRIPTION = new Description(By.cssSelector("[class*='x-boundlist-item']"), "Вариант");

    private final @NotNull Description DIV_DESCRIPTION = new Description(By.tagName("div"), "Текст");

    private final @NotNull Component text;

    public Option() {
        text = findInside(() -> new WebComponent(DIV_DESCRIPTION));
    }

    @Override
    protected @NotNull Description initialize() {
        return DEFAULT_DESCRIPTION;
    }

    public @NotNull String getText() {
       return text.isAvailable() ? text.toAction().getText() : toAction().getText();
    }

    public void click() {
        toAction().click();
    }
}
