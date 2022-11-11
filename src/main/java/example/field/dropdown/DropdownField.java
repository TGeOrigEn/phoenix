package example.field.dropdown;

import example.Button;
import example.field.Field;
import org.gems.WebComponent;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.By;
import org.untitled.phoenix.component.Component;
import org.untitled.phoenix.component.Description;

public class DropdownField extends Field {

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
