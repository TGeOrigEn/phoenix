package geoMeta;

import org.untitled.phoenix.component.Component;
import org.untitled.phoenix.component.Description;
import org.gems.WebComponent;
import org.untitled.phoenix.component.Requirement;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.By;

public class Item extends Component {

    public static @NotNull Requirement<Item> byName(@NotNull String name) {
        return new Requirement<>(Item::getName, name, "Имеет имя");
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
}
