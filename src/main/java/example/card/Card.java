package example.card;

import org.gems.WebComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;
import org.openqa.selenium.By;
import org.untitled.phoenix.component.Component;
import org.untitled.phoenix.component.Description;

import java.time.Duration;
import java.util.Comparator;
import java.util.List;

public class Card extends Component {

    public static class Requirements {

    }

    public static final @NotNull Description DEFAULT_DESCRIPTION = new Description(By.cssSelector("div[id^=window].grad-test-wincard"), "Карточка");

    private static final @NotNull Description BUTTON_MAXIMIZE_DESCRIPTION = new Description(By.cssSelector("img[class*='x-tool-maximize']"), "Кнопка 'Развернуть'");

    private static final @NotNull Description BUTTON_PROPERTY_DESCRIPTION = new Description(By.cssSelector("a[style='cursor: pointer;']"), "Кнопка 'К свойствам'");

    private static final @NotNull Description BUTTON_MINIMIZE_DESCRIPTION = new Description(By.cssSelector("img[class*='x-tool-minimize']"), "Кнопка 'Свернуть'");

    private static final @NotNull Description BUTTON_CLOSE_DESCRIPTION = new Description(By.cssSelector("img[class*='x-tool-close']"), "Кнопка 'Закрыть'");

    private static final @NotNull Description TITLE_DESCRIPTION = new Description(By.cssSelector("div[class*='x-title-item']"), "Заголовок");

    private final @NotNull Component buttonProperty;

    private final @NotNull Component buttonMinimize;

    private final @NotNull Component buttonMaximize;

    private final @NotNull Component buttonClose;

    private final @NotNull Component title;

    public Card() {
        buttonProperty = findInside(() -> new WebComponent(BUTTON_PROPERTY_DESCRIPTION));
        buttonMinimize = findInside(() -> new WebComponent(BUTTON_MINIMIZE_DESCRIPTION));
        buttonMaximize = findInside(() -> new WebComponent(BUTTON_MAXIMIZE_DESCRIPTION));
        buttonClose = findInside(() -> new WebComponent(BUTTON_CLOSE_DESCRIPTION));
        title = findInside(() -> new WebComponent(TITLE_DESCRIPTION));
    }

    public static @NotNull Card getActiveCard(Duration timeout) {
        final var cards = Component.findEveryone(Card::new, timeout).stream().sorted(Comparator.comparing(Card::getCssIndex)).toList();
        if (cards.isEmpty()) throw new RuntimeException();
        return cards.get(0);
    }

    @Override
    protected @NotNull Description initialize() {
        return DEFAULT_DESCRIPTION;
    }

    public @NotNull String getTitle() {
        return title.toAction().getText();
    }

    public void maximize() {
        buttonMaximize.toAction().click();
    }

    public void minimize() {
        buttonMinimize.toAction().click();
    }

    public void close() {
        buttonClose.toAction().click();
    }

    public @NotNull Card toProperty() {
        buttonProperty.toAction().click();
        return this;
    }

    private int getCssIndex() {
        return Integer.parseInt(toAction().getAttribute("z-index"));
    }
}
