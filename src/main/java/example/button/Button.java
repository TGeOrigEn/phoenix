package example.button;

import example.Menu;
import org.gems.WebComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;
import org.openqa.selenium.By;
import org.untitled.phoenix.component.Component;
import org.untitled.phoenix.component.Description;
import org.untitled.phoenix.component.requirement.BaseRequirement;
import org.untitled.phoenix.component.requirement.generic.Requirement;

import java.io.File;
import java.time.Duration;
import java.util.List;

public class Button extends Component {

    // div[class^='x-component']:not([style*='display']) a[style*="cursor: pointer"]

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

        public static @NotNull BaseRequirement<Button> isEnabled(boolean isEnabled) {
            return new Requirement<>(Button::isEnabled, isEnabled, "Включен");
        }
    }

    public static final @NotNull Description DEFAULT_DESCRIPTION = new Description(By.cssSelector("a[class*='x-btn']:not([style*='display: none'])"), "Кнопка");

    private static final @NotNull Description TEXT_DESCRIPTION = new Description(By.cssSelector("span[class*='x-btn-inner']"), "Текст");

    private final @NotNull Component text;

    public Button() {
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

    public void click() {
        toAction().click();
    }

    public @NotNull @Unmodifiable List<File> download(@NotNull Duration timeout, int countFiles) {
        return toAction().download(timeout, countFiles);
    }

    public @NotNull Menu clickAsDropdown() {
        toAction().click();
        return find(Menu::new);
    }
}
