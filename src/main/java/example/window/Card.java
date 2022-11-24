package example.window;

import org.gems.WebComponent;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.untitled.phoenix.component.Component;
import org.untitled.phoenix.component.Description;
import org.untitled.phoenix.component.requirement.BaseRequirement;
import org.untitled.phoenix.component.requirement.generic.Requirement;
import org.untitled.phoenix.configuration.Configuration;

import java.io.File;
import java.security.Key;
import java.util.Comparator;

public class Card extends Window {

    public static class Section extends Component {

        public static class Requirements {

            public static class Equal {
                public static @NotNull BaseRequirement<Section> byTitle(@NotNull String title) {
                    return new Requirement<>(Section::getTitle, title, "Имеет заголовок");
                }
            }

            public static class Contains {
                public static @NotNull BaseRequirement<Section> byTitle(@NotNull String title) {
                    return new Requirement<>(Section::getTitle, title, "Имеет заголовок", String::contains);
                }
            }

            public static @NotNull BaseRequirement<Section> isExpand(boolean isExpand) {
                return new Requirement<>(Section::isExpand, isExpand, "Является развёрнутым");
            }
        }

        public static final @NotNull Description DEFAULT_DESCRIPTION = new Description(By.cssSelector("div:not([style*='display'])>div[class*=x-panel].x-box-item[id*='panel']:not([style*='visibility'])"), "Секция");

        private static final @NotNull Description BUTTON_EXPAND_DESCRIPTION = new Description(By.cssSelector("img[class*='x-tool-img']"), "Кнопка 'Развернуть/Свернуть'");

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
            return buttonExpand.toAction().getCssClass().contains("x-tool-collapse");
        }

        public @NotNull String getTitle() {
            return title.toAction().getText();
        }

        public void expand() {
            buttonExpand.toAction().click();
        }
    }

    private static final @NotNull String DEFAULT_SELECTOR = "div[id^=window].grad-test-wincard:not([style*='display'])";

    private static final @NotNull String UPLOAD_INPUT_SELECTOR = "input[type='file']";

    public static final @NotNull Description DEFAULT_DESCRIPTION = new Description(By.cssSelector(DEFAULT_SELECTOR), "Карточка");

    private static final @NotNull Description UPLOAD_INPUT_DESCRIPTION = new Description(By.cssSelector(UPLOAD_INPUT_SELECTOR), "Ввод для вложений");

    private static final @NotNull Description BUTTON_PROPERTY_DESCRIPTION = new Description(By.cssSelector("a[style='cursor: pointer;']"), "Кнопка 'К свойствам'");

    private final @NotNull Component propertyButton;

    private final @NotNull Component uploadInput;

    public Card() {
        propertyButton = findInside(() -> new WebComponent(BUTTON_PROPERTY_DESCRIPTION));
        uploadInput = findInside(() -> new WebComponent(UPLOAD_INPUT_DESCRIPTION));
    }

    @Override
    public boolean isActive() {
        final var cards = Component.findEveryone(Card::new).stream().sorted(Comparator.comparing(Card::getCssIndex).reversed()).toArray(Card[]::new);
        if (cards.length == 0) throw new RuntimeException();
        return cards[0].getCssIndex() == getCssIndex();
    }

    @Override
    protected @NotNull Description initialize() {
        return DEFAULT_DESCRIPTION;
    }

    public void upload(@NotNull File file) {
        uploadInput.toAction().sendKeys(file.getAbsolutePath());
        final var script = String.format("document.querySelectorAll(`%s`)[%d].querySelectorAll(`%s`)[%d].value = ''", DEFAULT_SELECTOR, getIndex(), UPLOAD_INPUT_SELECTOR, uploadInput.getIndex());
        ((JavascriptExecutor) Configuration.getWebDriver()).executeScript(script);
    }

    public @NotNull Window toProperty() {
        propertyButton.toAction().click();
        return this;
    }
}
