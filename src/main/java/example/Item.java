package example;

import geoMeta.QuickTip;
import org.gems.WebComponent;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.By;
import org.untitled.phoenix.component.Component;
import org.untitled.phoenix.component.Description;
import org.untitled.phoenix.component.requirement.BaseRequirement;
import org.untitled.phoenix.component.requirement.generic.Requirement;

import java.time.Duration;

public class Item extends Component {

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

        public static @NotNull BaseRequirement<Item> byExpand(boolean isExpand) {
            return new Requirement<>(Item::isExpand, isExpand, "Открыт");
        }

        public static @NotNull BaseRequirement<Item> byExpendable(boolean isExpendable){
            return new Requirement<>(Item::isExpendable, isExpendable, "Может разворачиваться");
        }
    }

    public static final @NotNull Description DEFAULT_DESCRIPTION = new Description(By.cssSelector("tr[class*='x-grid-row']"), "Элемент списка");

    private static final @NotNull Description OPEN_TABLE_BUTTON_DESCRIPTION = new Description(By.cssSelector("span[class*='fg-table']"), "Кнопка 'Открыть таблицу'");

    private static final @NotNull Description EXPAND_BUTTON_DESCRIPTION = new Description(By.cssSelector("img[class*='plus']"), "Кнопка 'Развернуть/Свернуть'");

    private final @NotNull Component openTableButton;

    private final @NotNull Component expandButton;

    public Item() {
        openTableButton = findInside(() -> new WebComponent(OPEN_TABLE_BUTTON_DESCRIPTION));
        expandButton = findInside(() -> new WebComponent(EXPAND_BUTTON_DESCRIPTION));
    }

    @Override
    protected @NotNull Description initialize() {
        return DEFAULT_DESCRIPTION;
    }

    public String getName() {
        return toAction().getAttribute("data-qtip");
    }

    public void expand() {
        expandButton.toAction().click();
    }

    public boolean isExpendable() {
        return expandButton.isAvailable();
    }

    public void openTable() {
        toAction().hover();
        openTableButton.toAction().click();
    }

    public boolean isExpand() {
        return toAction().getCssClass().contains("x-grid-tree-node-expanded");
    }
}
