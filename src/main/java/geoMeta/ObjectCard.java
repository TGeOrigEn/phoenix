package geoMeta;

import org.untitled.phoenix.component.Component;
import org.untitled.phoenix.component.Description;
import org.gems.WebComponent;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.By;

public class ObjectCard extends Component {

    private static final Description SAVE_BUTTON_DESCRIPTION = new Description(By.cssSelector("a[data-qtip='Сохранить']"), "Кнопка 'Сохранить'");

    private static final Description CLOSE_BUTTON_DESCRIPTION = new Description(By.cssSelector("img[class='x-tool-img x-tool-close']"), "Кнопка 'Закрыть'");

    private final Component closeButton;

    private final Component saveButton;

    public ObjectCard() {
        closeButton = findInside(() -> new WebComponent(CLOSE_BUTTON_DESCRIPTION));
        saveButton = findInside(() -> new WebComponent(SAVE_BUTTON_DESCRIPTION));
    }

    @Override
    protected @NotNull Description initialize() {
        return new Description(By.cssSelector("div[id^=window].grad-test-wincard"), "Карточка объекта");
    }

    public void save() {
        saveButton.toAction().click();
    }

    public void close() {
        closeButton.toAction().click();
    }
}
