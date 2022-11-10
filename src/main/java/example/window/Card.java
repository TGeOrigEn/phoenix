package example.window;

import org.gems.WebComponent;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.By;
import org.untitled.phoenix.component.Component;
import org.untitled.phoenix.component.Description;

import java.io.File;
import java.time.Duration;
import java.util.Comparator;

public class Card extends Window {

    public static class Section extends Component {

        public static final @NotNull Description DEFAULT_DESCRIPTION = new Description(By.cssSelector("div[class*='x-box-item x-panel-default'][id*='panel']"), "Секция");

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

    public static final @NotNull Description DEFAULT_DESCRIPTION = new Description(By.cssSelector("div[id^=window].grad-test-wincard"), "Карточка");

    private static final @NotNull Description BUTTON_PROPERTY_DESCRIPTION = new Description(By.cssSelector("a[style='cursor: pointer;']"), "Кнопка 'К свойствам'");

    private final @NotNull Component propertyButton;

    public Card() {
        propertyButton = findInside(() -> new WebComponent(BUTTON_PROPERTY_DESCRIPTION));
    }

    @Override
    public boolean isActive() {
        final var cards = Component.findEveryone(Card::new).stream().sorted(Comparator.comparing(Card::getCssIndex).reversed()).toList();
        if (cards.isEmpty()) throw new RuntimeException();
        return cards.get(0).getCssIndex() == getCssIndex();
    }

    @Override
    protected @NotNull Description initialize() {
        return DEFAULT_DESCRIPTION;
    }

    public @NotNull Window toProperty() {
        propertyButton.toAction().click();
        return this;
    }
}
