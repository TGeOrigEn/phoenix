package example.card;

import org.gems.WebComponent;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.By;
import org.untitled.phoenix.component.Component;
import org.untitled.phoenix.component.Description;

public class Section extends Component {

    public static final @NotNull Description DEFAULT_DESCRIPTION = new Description(By.cssSelector("div[class*='x-panel x-box-item x-panel-default']"), "Секция");

    private static final @NotNull Description BUTTON_EXPAND_DESCRIPTION = new Description(By.tagName("img"), "Кнопка 'Развернуть/Свернуть'");

    private static final @NotNull Description TITLE_DESCRIPTION = new Description(By.cssSelector("div[id*='title'] span"), "Заголовок");

    private final @NotNull Component buttonExpand;

    private final @NotNull Component title;

    public Section() {
        buttonExpand = findInside(() -> new WebComponent(BUTTON_EXPAND_DESCRIPTION));
        title = findInside(() -> new WebComponent(TITLE_DESCRIPTION));
    }

    @Override
    protected @NotNull Description initialize() {
        return DEFAULT_DESCRIPTION;
    }

    public boolean isExpand() {
       return buttonExpand.toAction().getCssClass().contains("x-tool-collapse-top");
    }

    public @NotNull String getTitle() {
        return title.toAction().getText();
    }

    public void expand() {
        buttonExpand.toAction().click();
    }
}
