package example;

import example.button.Button;
import org.gems.WebComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;
import org.openqa.selenium.By;
import org.untitled.phoenix.component.Component;
import org.untitled.phoenix.component.Description;
import org.untitled.phoenix.component.requirement.BaseRequirement;
import org.untitled.phoenix.component.requirement.generic.Requirement;

import java.io.File;
import java.time.Duration;
import java.util.List;

public class Attachment extends Component {

    public static class Item extends Component {

        public static class Requirements {

            public static class Equals {

                public static @NotNull BaseRequirement<Item> byName(@NotNull String name) {
                    return new Requirement<>(Item::getName, name, "Имеет имя", String::contains);
                }
            }

            public static class Contains {

                public static @NotNull BaseRequirement<Item> byName(@NotNull String name) {
                    return new Requirement<>(Item::getName, name, "Содержит имя", String::contains);
                }
            }
        }

        public static final @NotNull Description DEFAULT_DESCRIPTION = new Description(By.cssSelector("tr[class*='x-grid-row']"), "Вложение");

        private static final @NotNull Description DOWNLOAD_BUTTON_DESCRIPTION = new Description(By.cssSelector("td[data-qtip='Скачать']"), "Кнопка 'Скачать'");

        private static final @NotNull Description DELETE_BUTTON_DESCRIPTION = new Description(By.cssSelector("td[data-qtip='Удалить']"), "Кнопка 'Удалить'");

        private static final @NotNull Description RECOVERY_BUTTON_DESCRIPTION = new Description(By.cssSelector("td[data-qtip='Восстановить']"), "Кнопка 'Восстановить'");

        private static final @NotNull Description NAME_DESCRIPTION = new Description(By.cssSelector("a[class*='download']"), "Имя");

        private final @NotNull Component recoveryButton;

        private final @NotNull Component downloadButton;

        private final @NotNull Component deleteButton;

        private final @NotNull Component name;

        public Item() {
            recoveryButton = findInside(() -> new WebComponent(RECOVERY_BUTTON_DESCRIPTION));
            downloadButton = findInside(() -> new WebComponent(DOWNLOAD_BUTTON_DESCRIPTION));
            deleteButton = findInside(() -> new WebComponent(DELETE_BUTTON_DESCRIPTION));
            name = findInside(() -> new WebComponent(NAME_DESCRIPTION));
        }

        @Override
        protected @NotNull Description initialize() {
            return DEFAULT_DESCRIPTION;
        }

        public @NotNull String getName() {
            return name.toAction().getText();
        }

        public void recovery() {
            toAction().hover();
            recoveryButton.toAction().click();
        }

        public void delete() {
            toAction().hover();
            deleteButton.toAction().click();
        }

        public @NotNull File download(Duration timeout) {
            return downloadButton.toAction().download(timeout, 1).get(0);
        }
    }

    public static final @NotNull Description DEFAULT_DESCRIPTION = new Description(By.cssSelector("div[id*='attachmenttip'][class*='x-tip x-layer']"), "Вложения");

    private static final @NotNull Description CLOSE_BUTTON_DESCRIPTION = new Description(By.cssSelector("img[class*='x-tool-close']"), "Кнопка 'Закрыть'");

    private final @NotNull Component closeButton;

    public Attachment() {
        closeButton = findInside(() -> new WebComponent(CLOSE_BUTTON_DESCRIPTION));
    }

    @Override
    protected @NotNull Description initialize() {
        return DEFAULT_DESCRIPTION;
    }

    public @NotNull @Unmodifiable List<File> download(Duration timeout, int countFiles) {
       return findInside(Button::new, Button.Requirements.Equals.byText("Скачать архив")).toAction().download(timeout, countFiles);
    }

    public void close() {
        closeButton.toAction().click();
    }
}
