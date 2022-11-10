package example;

import org.gems.WebComponent;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.By;
import org.untitled.phoenix.component.Component;
import org.untitled.phoenix.component.Description;
import org.untitled.phoenix.component.requirement.BaseRequirement;
import org.untitled.phoenix.component.requirement.generic.Requirement;

import java.util.Comparator;

public class Menu extends Component {

    public static class Option extends Component {

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

            public static @NotNull BaseRequirement<Option> isCheckable(boolean isCheckable) {
                return new Requirement<>(Option::isCheckable, isCheckable, "Является выделяемым");
            }

            public static @NotNull BaseRequirement<Option> isChecked(boolean isChecked) {
                return new Requirement<>(Option::isChecked, isChecked, "Является выделенным");
            }

            public static @NotNull BaseRequirement<Option> isEnabled(boolean isEnabled) {
                return new Requirement<>(Option::isEnabled, isEnabled, "Включен");
            }
        }

        public static final @NotNull Description DEFAULT_DESCRIPTION = new Description(By.cssSelector("div[id*='item'][class*='x-box-item']:not([style*=display])"), "Вариант");

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

        public boolean isCheckable() {
            return toAction().getAttribute("id").contains("check");
        }

        public boolean isChecked() {
            return !toAction().getCssClass().contains("unchecked");
        }

        public @NotNull String getText() {
            return text.toAction().getText();
        }

        public void click() {
            toAction().click();
        }

        public void hover() {
            toAction().hover();
        }
    }

    public static class Requirements {

        public static @NotNull BaseRequirement<Menu> isActive(boolean isActive) {
            return new Requirement<>(Menu::isActive, isActive, "Является активным");
        }
    }

    public static final @NotNull Description DEFAULT_DESCRIPTION = new Description(By.cssSelector("div[class*='x-menu x-layer']:not([style*=visibility])"), "Выпадающий список");

    @Override
    protected @NotNull Description initialize() {
        return DEFAULT_DESCRIPTION;
    }

    protected int getCssIndex() {
        return Integer.parseInt(toAction().getCssValue("z-index"));
    }

    public boolean isActive() {
        final var cards = Component.findEveryone(Menu::new).stream().sorted(Comparator.comparing(Menu::getCssIndex).reversed()).toList();
        if (cards.isEmpty()) throw new RuntimeException();
        return cards.get(0).getCssIndex() == getCssIndex();
    }
}
