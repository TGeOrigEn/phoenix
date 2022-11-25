package example.table;

import example.Menu;
import example.Spinner;
import example.button.Button;
import example.field.Field;
import example.field.nested.SearchField;
import example.window.Alert;
import example.window.Card;
import example.window.Window;
import org.gems.WebComponent;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.By;
import org.untitled.phoenix.component.Component;
import org.untitled.phoenix.component.Description;
import org.untitled.phoenix.component.requirement.BaseRequirement;
import org.untitled.phoenix.component.requirement.generic.Requirement;

import java.time.Duration;

public class ViewPanel extends Component {

    public static class Tab extends Component {

        public static class Requirements {

            public static class Equals {

                public static @NotNull BaseRequirement<Tab> byName(@NotNull String name) {
                    return new Requirement<>(Tab::getName, name, "Имеет имя");
                }
            }

            public static class Contains {

                public static @NotNull BaseRequirement<Tab> byName(@NotNull String name) {
                    return new Requirement<>(Tab::getName, name, "Содержит имя", String::contains);
                }
            }

            public static @NotNull BaseRequirement<Tab> isActive(boolean isActive) {
                return new Requirement<>(Tab::isActive, isActive, "Является активным");
            }

            public static @NotNull BaseRequirement<Tab> isFiltered(boolean isFiltered) {
                return new Requirement<>(Tab::isFiltered, isFiltered, "Является отфильтрованным");
            }
        }

        public static final @NotNull Description DEFAULT_DESCRIPTION = new Description(By.cssSelector("a[class^='x-tab x-unselectable']"), "Вкладка");

        private static final @NotNull Description FILTER_ICON_DESCRIPTION = new Description(By.cssSelector("span[class*='fg-map-filtered']"), "Иконка фильтрации");

        private static final @NotNull Description CLOSE_BUTTON_DESCRIPTION = new Description(By.cssSelector("span[class='x-tab-close-btn']"), "Кнопка 'Закрыть'");

        private static final @NotNull Description TEXT_DESCRIPTION = new Description(By.cssSelector("span[class='x-tab-inner x-tab-inner-default']"), "Текст");

        private final @NotNull Component closeButton;

        private final @NotNull Component filterIcon;

        private final @NotNull Component text;

        public Tab() {
            closeButton = findInside(() -> new WebComponent(CLOSE_BUTTON_DESCRIPTION));
            filterIcon = findInside(() -> new WebComponent(FILTER_ICON_DESCRIPTION));
            text = findInside(() -> new WebComponent(TEXT_DESCRIPTION));
        }

        @Override
        protected @NotNull Description initialize() {
            return DEFAULT_DESCRIPTION;
        }

        public boolean isActive() {
            return toAction().getCssClass().contains("x-tab-active");
        }

        public @NotNull String getName() {
            return text.toAction().getText();
        }

        public boolean isFiltered() {
            return filterIcon.isAvailable();
        }

        public void close() {
            closeButton.toAction().click();
        }
    }

    public static class Item extends Component {

        public static class Requirements {

            public static class Equals {

                public static @NotNull BaseRequirement<Item> byValue(@NotNull String columnName, @NotNull String value) {
                    return new Requirement<>(item -> item.getValue(columnName), value, String.format("Имеет значение в столбце '%s'", columnName));
                }
            }

            public static class Contains {

                public static @NotNull BaseRequirement<Item> byValue(@NotNull String columnName, @NotNull String value) {
                    return new Requirement<>(item -> item.getValue(columnName), value, String.format("Содержит значение в столбце '%s'", columnName), String::contains);
                }
            }
        }

        public enum Option { MAP, ATTACHMENT, CARD }

        public static final @NotNull Description DEFAULT_DESCRIPTION = new Description(By.cssSelector("div[id*='normal'] table[class^='x-grid-item']"), "Элемент");

        private static final @NotNull Description CONTAINER_FOR_BUTTONS_DESCRIPTION = new Description(By.cssSelector("div[id*='locked'] table[class^='x-grid-item']"), "Контейнер для кнопок");

        private static final @NotNull Description OPEN_CARD_BUTTON_DESCRIPTION = new Description(By.cssSelector("span[data-qtip='Открыть карточку объекта']"), "Кнопка 'Открыть карточку объекта'");

        private static final @NotNull Description SHOW_ON_MAP_BUTTON_DESCRIPTION = new Description(By.cssSelector("span[data-qtip='Показать объект на карте']"), "Кнопка 'Показать объект на карте'");

        private static final @NotNull Description ATTACHMENT_BUTTON_DESCRIPTION = new Description(By.cssSelector("span[data-qtip='Вложения']"), "Кнопка 'Вложения'");

        private static final @NotNull Description VALUE_DESCRIPTION = new Description(By.cssSelector("div[class*='x-grid-cell-inner']"), "Значение");

        private final @NotNull Component attachmentButton;

        private final @NotNull Component showOnMapButton;

        private final @NotNull Component openCardButton;

        public Item() {
            final var containerForButtons = find(ViewPanel::new)
                    .findInside(() -> new WebComponent(CONTAINER_FOR_BUTTONS_DESCRIPTION.copy(getIndex())));

            attachmentButton = containerForButtons.findInside(() -> new WebComponent(ATTACHMENT_BUTTON_DESCRIPTION));
            showOnMapButton = containerForButtons.findInside(() -> new WebComponent(SHOW_ON_MAP_BUTTON_DESCRIPTION));
            openCardButton = containerForButtons.findInside(() -> new WebComponent(OPEN_CARD_BUTTON_DESCRIPTION));
        }

        @Override
        protected @NotNull Description initialize() {
            return DEFAULT_DESCRIPTION;
        }

        public boolean isSelected() {
            return toAction().getCssClass().contains("selected");
        }

        public @NotNull String getValue(@NotNull String columnName) {
            final var column = find(ViewPanel::new).findInside(Header::new, Header.Requirements.Equals.byName(columnName));
            Component.should(column, Requirement.isAvailable(true), column.getTimeout());
            return findInside(() -> new WebComponent(VALUE_DESCRIPTION.copy(column.getIndex() - 1))).toAction().getText();
        }

        public void show(@NotNull Option option) {
            switch (option){
                case MAP: showOnMapButton.toAction().click(); break;
                case ATTACHMENT: attachmentButton.toAction().click(); break;
                case CARD: openCardButton.toAction().click(); break;
            }
        }

        public void select() {
            toAction().click();
        }
    }

    public static final @NotNull Description DEFAULT_DESCRIPTION = new Description(By.cssSelector("div[id^='pptab'][class='x-panel x-tabpanel-child x-panel-default x-closable x-panel-closable x-panel-default-closable']:not([style*=display])"), "Панель представления");

    private static final @NotNull Spinner spinner = Component.find(Spinner::new);

    private static final @NotNull Alert alert = ((Alert) Component.find(Alert::new, Window.Requirements.isActive(true)));

    @Override
    protected @NotNull Description initialize() {
        return DEFAULT_DESCRIPTION;
    }

    public void deleteSelectedObjects() {
        findInside(Button::new, Button.Requirements.Equals.byText("Еще")).clickAsDropdown().findInside(Menu.Item::new, Menu.Item.Requirements.Equals.byText("Удалить объект")).click();
        alert.findInside(Button::new, Button.Requirements.Equals.byText("Удалить")).click();
        spinner.wait(Duration.ofSeconds(60));
    }

    public void refresh() {
        findInside(Button::new, Button.Requirements.Equals.byTip("Обновить таблицу")).click();
        spinner.wait(Duration.ofSeconds(60));
    }

    public Card createNewObject() {
        findInside(Button::new, Button.Requirements.Equals.byTip("Создать новый объект")).click();
        return ((Card) Component.find(Card::new, Window.Requirements.isActive(true)));
    }

    public SearchField getSearchField() {
        return ((SearchField) findInside(SearchField::new, Field.Requirements.Equals.byPlaceholder("Поиск в таблице")));
    }

    public Item getItemBy(BaseRequirement<Item> requirement) {
        return findInside(ViewPanel.Item::new, requirement);
    }

    public Card createNewObjectBy(@NotNull BaseRequirement<Menu.Item> requirement) {
        findInside(Button::new, Button.Requirements.Equals.byTip("Создать новый объект")).clickAsDropdown().findInside(Menu.Item::new, requirement);
        return ((Card) Component.find(Card::new, Window.Requirements.isActive(true)));
    }
}
