package example.table;

import example.field.Field;
import example.field.nested.TextField;
import org.gems.WebComponent;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.untitled.phoenix.component.Component;
import org.untitled.phoenix.component.Description;
import org.untitled.phoenix.component.requirement.BaseRequirement;
import org.untitled.phoenix.component.requirement.generic.Requirement;

public class CoordinateTable extends Component {

    public static class Item extends Component {

        public static class Requirements {

            public static class Equals {

                public static @NotNull BaseRequirement<Item> byX(@NotNull String value) {
                    return new Requirement<>(Item::getX, value, "Имеет значение в столбце 'X'");
                }

                public static @NotNull BaseRequirement<Item> byY(@NotNull String value) {
                    return new Requirement<>(Item::getY, value, "Имеет значение в столбце 'Y'");
                }
            }

            public static class Contains {

                public static @NotNull BaseRequirement<Item> ByX(@NotNull String value) {
                    return new Requirement<>(Item::getX, value, "Содержит значение в столбце 'X'", String::contains);
                }

                public static @NotNull BaseRequirement<Item> ByY(@NotNull String value) {
                    return new Requirement<>(Item::getY, value, "Содержит значение в столбце 'Y'", String::contains);
                }
            }
        }

        public static final @NotNull Description DEFAULT_DESCRIPTION = new Description(By.cssSelector("table[class^='x-grid-item']"), "Элемент");

        private static final @NotNull Description REMOVE_BUTTON_DESCRIPTION = new Description(By.cssSelector("span[class*='fg-remove']"), "Кнопка 'Удалить'");

        private static final @NotNull Description ADD_BUTTON_DESCRIPTION = new Description(By.cssSelector("span[class*='fg-add']"), "Кнопка 'Добавить'");

        private static final @NotNull Description VALUE_DESCRIPTION = new Description(By.cssSelector("div[class*='x-grid-cell-inner']"), "Значение");

        private final @NotNull Component addButton;

        private final @NotNull Component removeButton;

        public Item() {
            addButton = findInside(() -> new WebComponent(ADD_BUTTON_DESCRIPTION));
            removeButton = findInside(() -> new WebComponent(REMOVE_BUTTON_DESCRIPTION));
        }

        @Override
        protected @NotNull Description initialize() {
            return DEFAULT_DESCRIPTION;
        }

        public void setX(@NotNull String value) {
            setValue("X", value);
        }

        public void setY(@NotNull String value) {
            setValue("Y", value);
        }

        public @NotNull String getX() {
            return getValue("X");
        }

        public @NotNull String getY() {
            return getValue("Y");
        }

        public void remove() {
            removeButton.toAction().click();
        }

        public void add() {
            addButton.toAction().click();
        }

        private void setValue(@NotNull String columnName, @NotNull String value) {
            final var column = find(CoordinateTable::new).findInside(Header::new, Header.Requirements.Equals.byName(columnName));
            Component.should(column, Requirement.isAvailable(true), column.getTimeout());
            findInside(() -> new WebComponent(VALUE_DESCRIPTION.copy(column.getIndex()))).toAction().click();
            final var s = find(CoordinateTable::new).findInside(TextField::new, Requirement.isDisplayed(true));
            s.sendKeys(Keys.LEFT_CONTROL + "A" + Keys.DELETE);
            s.sendKeys(value);
        }

        private @NotNull String getValue(@NotNull String columnName) {
            final var column = find(CoordinateTable::new).findInside(Header::new, Header.Requirements.Equals.byName(columnName));
            Component.should(column, Requirement.isAvailable(true), column.getTimeout());
            return findInside(() -> new WebComponent(VALUE_DESCRIPTION.copy(column.getIndex()))).toAction().getText();
        }
    }

    public static final @NotNull Description DEFAULT_DESCRIPTION = new Description(By.cssSelector("div[id*='coordinatestable2'][class='x-panel x-fit-item x-panel-default']"), "Таблица координат");

    private static final @NotNull Description ADD_PART_BUTTON_DESCRIPTION = new Description(By.cssSelector("div[id*='glyphLabel']"), "Кнопка '+ Добавить часть'");

    private final @NotNull Component addPartButton;

    public CoordinateTable() {
        addPartButton = findInside(() -> new WebComponent(ADD_PART_BUTTON_DESCRIPTION));
    }

    @Override
    protected @NotNull Description initialize() {
        return DEFAULT_DESCRIPTION;
    }

    public Item getItemBy(BaseRequirement<Item> requirement) {
        return findInside(Item::new, requirement);
    }

    public void addPart() {
        addPartButton.toAction().click();
    }
}
