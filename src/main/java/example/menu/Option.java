package example.menu;

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

        public static @NotNull BaseRequirement<Option> isEnabled(boolean isEnabled) {
            return new Requirement<>(Option::isEnabled, isEnabled, "Включен");
        }
    }

    public static final @NotNull Description DEFAULT_DESCRIPTION = new Description(By.cssSelector("div[id*='menuitem'][class*='x-box-item']:not([style*=display])"), "Вариант");

    private static final @NotNull Description TEXT_DESCRIPTION = new Description(By.cssSelector("span[class*='x-menu-item-text']"), "Текст");

    private final @NotNull Component text;

    public Option() {
        text = findInside(() -> new WebComponent(TEXT_DESCRIPTION));
    }

    @Override
    protected @NotNull Description initialize() {
        return DEFAULT_DESCRIPTION;
    }

    public boolean isEnabled() {
        return toAction().getCssClass().contains("x-menu-item-disabled");
    }

    public @NotNull String getText() {
       return text.toAction().getText();
    }

    public void click() {
        toAction().click();
    }
}
