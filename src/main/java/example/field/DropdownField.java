package example.field;

import example.Button;
import org.gems.WebComponent;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.By;
import org.untitled.phoenix.component.Component;
import org.untitled.phoenix.component.Description;
import org.untitled.phoenix.component.requirement.BaseRequirement;
import org.untitled.phoenix.component.requirement.generic.Requirement;

public class DropdownField extends Field {

    public static class Dropdown extends Component {

        public static class Item extends Component {

            public static class Requirements {

                public static class Equals {

                    public static @NotNull BaseRequirement<Item> byText(@NotNull String text) {
                        return new Requirement<>(Item::getTextDiv, text, "Имеет текст");
                    }
                }

                public static class Contains {

                    public static @NotNull BaseRequirement<Item> byText(@NotNull String text) {
                        return new Requirement<>(Item::getTextDiv, text, "Содержит текст", String::contains);
                    }
                }
            }

            public static final @NotNull Description DEFAULT_DESCRIPTION = new Description(By.cssSelector("[class*='x-boundlist-item']"), "Вариант");

            private static final @NotNull Description SPAN_DESCRIPTION = new Description(By.cssSelector("span:not([class='chkBoundListItem'])"), "Текст");

            private static final @NotNull Description DIV_DESCRIPTION = new Description(By.tagName("div"), "Текст");

            private final @NotNull Component textSpan;

            private final @NotNull Component textDiv;

            public Item() {
                textSpan = findInside(() -> new WebComponent(SPAN_DESCRIPTION));
                textDiv = findInside(() -> new WebComponent(DIV_DESCRIPTION));
            }

            @Override
            protected @NotNull Description initialize() {
                return DEFAULT_DESCRIPTION;
            }

            public @NotNull String getTextDiv() {
                return textDiv.isAvailable() ? textSpan.isAvailable()
                        ? textSpan.toAction().getText() : textDiv.toAction().getText()
                        : toAction().getText();
            }

            public void click() {
                toAction().click();
            }
        }

        public static final @NotNull Description DEFAULT_DESCRIPTION = new Description(By.cssSelector("div[id^='boundlist'][class='x-boundlist x-boundlist-floating x-layer x-boundlist-default x-border-box']:not([style*=display])"), "Список вариантов");

        @Override
        protected @NotNull Description initialize() {
            return DEFAULT_DESCRIPTION;
        }
    }

    private static final @NotNull Description ARROW_BUTTON_DESCRIPTION = new Description(By.cssSelector("div[class*='x-form-arrow-trigger']"), "Кнопка 'Стрелка'");

    private static final @NotNull Description PARENT_DIV_DESCRIPTION = new Description(By.xpath("./../.."), "Контейнер-родитель");

    private static final @NotNull Description VALUE_DESCRIPTION = new Description(By.cssSelector("div[class='x-tagfield-item-text']"), "Значение");

    private static final @NotNull Description INPUT_DESCRIPTION = new Description(By.tagName("input"), "Ввод");

    private final @NotNull Component arrowButton;

    private final @NotNull Button addButton;

    private final @NotNull Component input;

    public DropdownField() {
        arrowButton = findInside(() -> new WebComponent(ARROW_BUTTON_DESCRIPTION));
        input = findInside(() -> new WebComponent(INPUT_DESCRIPTION));

        addButton = findInside(() -> new WebComponent(PARENT_DIV_DESCRIPTION))
                .findInside(Button::new, Button.Requirements.Equals.byTip("Добавить новый объект"));
    }

    @Override
    protected @NotNull Component getInput() {
        return input;
    }

    @Override
    public void setValue(@NotNull String name) {
        arrowButton.toAction().click();
        find(Dropdown::new).findInside(Dropdown.Item::new, Dropdown.Item.Requirements.Equals.byText(name)).click();
    }

    @Override
    public @NotNull String getValue() {
       return String.join("; ", findInsideEveryone(() -> new WebComponent(VALUE_DESCRIPTION)).stream().map(value -> value.toAction().getText()).toArray(String[]::new));
    }

    public void addNewObject() {
        toAction().hover();
        addButton.click();
    }

    public @NotNull Dropdown openDropdown() {
        arrowButton.toAction().click();
        return find(Dropdown::new);
    }
}
