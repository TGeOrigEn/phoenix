package example.field.select;

import example.Button;
import example.card.Card;
import example.field.Field;
import example.field.select.menu.Menu;
import org.gems.WebComponent;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.By;
import org.untitled.phoenix.component.Component;
import org.untitled.phoenix.component.Description;

import java.time.Duration;

public class Select extends Field {

    private static final Description INPUT_DESCRIPTION = new Description(By.tagName("input"), "Ввод");

    private static final Description ARROW_BUTTON_DESCRIPTION = new Description(By.cssSelector("div[class*='x-form-arrow-trigger']"), "Стрелка");

    private static final Description PARENT_DIV_DESCRIPTION = new Description(By.xpath("/parent::div/parent::div"), "")

    private final Component arrowButton;

    private final Button addButton;

    private final @NotNull Component div;

    private final Component input;

    public Select() {
        addButton = findInside(Button::new, Button.Requirements.Equals.byTip("Добавить новый объект"));
        arrowButton = findInside(() -> new WebComponent(ARROW_BUTTON_DESCRIPTION));
        input = findInside(() -> new WebComponent(INPUT_DESCRIPTION));
    }

    @Override
    protected @NotNull Component getInput() {
        return input;
    }

    public @NotNull Card clickOnAddButton(Duration timeout) {
        toAction().hover();
        addButton.click();
        return Card.getActiveCard(timeout);
    }

    public @NotNull Menu clickOnArrowButton() {
        arrowButton.toAction().click();
        return find(Menu::new);
    }
}
