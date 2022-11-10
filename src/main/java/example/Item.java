package example;

import org.untitled.phoenix.component.Component;
import org.untitled.phoenix.component.Description;
import org.untitled.phoenix.component.requirement.BaseRequirement;
import org.untitled.phoenix.component.requirement.generic.Requirement;

import org.gems.WebComponent;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.By;

public class Item extends Component {

    public enum Option { TABLE, LAYER, MAP }

    public static class Requirements {

        public static class Equals {

            public static @NotNull BaseRequirement<Item> byName(@NotNull String name) {
                return new Requirement<>(Item::getName, name, "Имеет имя");
            }
        }

        public static class Contains {

            public static @NotNull BaseRequirement<Item> byName(@NotNull String name) {
                return new Requirement<>(Item::getName, name, "Содержит имя", String::contains);
            }
        }

        public static @NotNull BaseRequirement<Item> isExpendable(boolean isExpendable) {
            return new Requirement<>(Item::isExpendable, isExpendable, "Может разворачиваться");
        }

        public static @NotNull BaseRequirement<Item> isCheckable(boolean isCheckable) {
            return new Requirement<>(Item::isCheckable, isCheckable, "Может быть выделен");
        }

        public static @NotNull BaseRequirement<Item> isChecked(boolean isChecked) {
            return new Requirement<>(Item::isChecked, isChecked, "Выделен");
        }

        public static @NotNull BaseRequirement<Item> isExpand(boolean isExpand) {
            return new Requirement<>(Item::isExpand, isExpand, "Открыт");
        }
    }

    public static final @NotNull Description DEFAULT_DESCRIPTION = new Description(By.cssSelector("tr[class*='x-grid-row']"), "Элемент списка");

    private static final @NotNull Description EXPAND_BUTTON_DESCRIPTION = new Description(By.cssSelector("img[class*='plus']"), "Кнопка 'Развернуть/Свернуть'");

    private static final @NotNull Description CHECKBOX_DESCRIPTION = new Description(By.tagName("input"), "Флаг");

    private final @NotNull Component expandButton;

    private final @NotNull Component checkbox;

    public Item() {
        expandButton = findInside(() -> new WebComponent(EXPAND_BUTTON_DESCRIPTION));
        checkbox = findInside(() -> new WebComponent(CHECKBOX_DESCRIPTION));
    }

    @Override
    protected @NotNull Description initialize() {
        return DEFAULT_DESCRIPTION;
    }

    public @NotNull String getName() {
        return toAction().getAttribute("data-qtip");
    }

    public void expand() {
        expandButton.toAction().click();
    }

    public void open(@NotNull Option option) {
        toAction().hover();

        switch (option) {
            case MAP -> findInside(Button::new, Button.Requirements.Equals.byTip("Открыть карту")).click();
            case LAYER -> findInside(Button::new, Button.Requirements.Equals.byTip("Открыть слой")).click();
            case TABLE -> findInside(Button::new, Button.Requirements.Equals.byTip("Открыть таблицу")).click();
        }
    }

    public @NotNull Menu more() {
        toAction().hover();
        findInside(Button::new, Button.Requirements.Equals.byTip("Еще")).click();
        return find(Menu::new);
    }

    public boolean isChecked() {
        return checkbox.toAction().getCssClass().contains("x-tree-checkbox-checked");
    }

    public boolean isExpand() {
        return toAction().getCssClass().contains("x-grid-tree-node-expanded");
    }

    public boolean isExpendable() {
        return expandButton.isAvailable();
    }

    public boolean isCheckable() {
        return checkbox.isAvailable();
    }
}
