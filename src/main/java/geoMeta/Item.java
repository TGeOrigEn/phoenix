package geoMeta;

import org.untitled.phoenix.component.Component;
import org.untitled.phoenix.component.Description;
import org.gems.WebComponent;
import org.untitled.phoenix.component.requirement.BaseRequirement;
import org.untitled.phoenix.component.requirement.generic.Requirement;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.By;

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

    private static final Description EXPAND_BUTTON_DESCRIPTION = new Description(By.cssSelector("img[class*='plus']"), "Кнопка 'Развернуть'");

    private static final Description OPEN_TABLE_BUTTON_DESCRIPTION = new Description(By.cssSelector("a[data-qtip='Открыть таблицу']"), "Кнопка 'Открыть таблицу'");

    private final WebComponent openTableButton;

    private final WebComponent expandButton;

    public Item() {
        openTableButton = findInside(() -> new WebComponent(OPEN_TABLE_BUTTON_DESCRIPTION));
        expandButton = findInside(() -> new WebComponent(EXPAND_BUTTON_DESCRIPTION));
    }

    @Override
    protected @NotNull Description initialize() {
        return new Description(By.cssSelector("tr[class*='x-grid-row']"), "Элемент списка");
    }

    public void openTable() {
        toAction().hover();
        openTableButton.toAction().click();
    }

    public String getName() {
        return toAction().getAttribute("data-qtip");
    }

    public void expand() {
        expandButton.toAction().click();
        find(QuickTip::new).toAction().hover();
    }

    public boolean isExpendable() {
        return expandButton.isAvailable();
    }

    public boolean isExpand(){
       return toAction().getCssClass().contains("x-grid-tree-node-expanded");
    }
}
