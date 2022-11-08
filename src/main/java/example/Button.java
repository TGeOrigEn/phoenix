package example;

import example.menu.Menu;
import org.gems.WebComponent;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.By;
import org.untitled.phoenix.component.Component;
import org.untitled.phoenix.component.Description;
import org.untitled.phoenix.component.requirement.BaseRequirement;
import org.untitled.phoenix.component.requirement.generic.Requirement;

public class Button extends Component {

    public static class Requirements {

        public static class Equals {

            public static @NotNull BaseRequirement<Button> byText(@NotNull String text) {
                return new Requirement<>(Button::getText, text, "Имеет текст");
            }

            public static @NotNull BaseRequirement<Button> byTip(@NotNull String tip) {
                return new Requirement<>(Button::getTip, tip, "Имеет подсказку");
            }
        }

        public static class Contains {

            public static @NotNull BaseRequirement<Button> byText(@NotNull String text) {
                return new Requirement<>(Button::getText, text, "Содержит текст", String::contains);
            }

            public static @NotNull BaseRequirement<Button> byTip(@NotNull String tip) {
                return new Requirement<>(Button::getTip, tip, "Содержит подсказку", String::contains);
            }
        }

        public static @NotNull BaseRequirement<Button> isDropdown(boolean isDropdown) {
            return new Requirement<>(Button::isDropdown, isDropdown, "Является выпадающим списком");
        }

        public static @NotNull BaseRequirement<Button> isEnabled(boolean isEnabled) {
            return new Requirement<>(Button::isEnabled, isEnabled, "Включен");
        }
    }

    public static final @NotNull Description DEFAULT_DESCRIPTION = new Description(By.cssSelector("a[class*='x-btn']:not([style*=display])"), "Кнопка");

    private static final @NotNull Description ARROW_DESCRIPTION = new Description(By.cssSelector("[class*='x-btn-arrow-right']"), "Стрелка");

    private static final @NotNull Description TEXT_DESCRIPTION = new Description(By.cssSelector("span[class*='x-btn-inner']"), "Текст");

    private final @NotNull Component arrow;

    private final @NotNull Component text;

    public Button() {
        arrow = findInside(() -> new WebComponent(ARROW_DESCRIPTION));
        text = findInside(() -> new WebComponent(TEXT_DESCRIPTION));
    }

    @Override
    protected @NotNull Description initialize() {
        return DEFAULT_DESCRIPTION;
    }

    public @NotNull String getTip() {
        return toAction().getAttribute("data-qtip");
    }

    public @NotNull String getText() {
        return this.text.toAction().getText();
    }

    public boolean isEnabled() {
        return toAction().getCssClass().contains("disabled");
    }

    public boolean isDropdown() {
        return arrow.isAvailable();
    }

    public void click() {
        toAction().click();
    }

    public Menu clickOnArrow() {
        toAction().click();
        return find(Menu::new);
    }
}
