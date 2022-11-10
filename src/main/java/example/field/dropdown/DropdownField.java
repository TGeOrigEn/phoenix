package example.field.dropdown;

import example.Button;
import example.window.Card;
import example.window.Window;
import example.field.Field;
import org.gems.WebComponent;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.By;
import org.untitled.phoenix.component.Component;
import org.untitled.phoenix.component.Description;

public class DropdownField extends Field {

    private static final @NotNull Description ARROW_BUTTON_DESCRIPTION = new Description(By.cssSelector("div[class*='x-form-arrow-trigger']"), "Кнопка 'Стрелка'");

    private static final @NotNull Description PARENT_DIV_DESCRIPTION = new Description(By.xpath("./../.."), "Контейнер-родитель");

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

    public @NotNull Card addNewObject() {
        toAction().hover();
        addButton.click();
        return (Card) Component.find(Card::new, Window.Requirements.isActive(true));
    }

    public @NotNull Dropdown openDropdown() {
        arrowButton.toAction().click();
        return find(Dropdown::new);
    }
}
