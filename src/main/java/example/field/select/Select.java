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

    private static final @NotNull Description ARROW_BUTTON_DESCRIPTION = new Description(By.cssSelector("div[class*='x-form-arrow-trigger']"), "Кнопка 'Стрелка'");

    private static final @NotNull Description PARENT_DIV_DESCRIPTION = new Description(By.xpath("./../.."), "Контейнер-родитель");

    private static final @NotNull Description INPUT_DESCRIPTION = new Description(By.tagName("input"), "Ввод");

    private final @NotNull Component arrowButton;

    private final @NotNull Button addButton;

    private final @NotNull Component input;

    public Select() {
        arrowButton = findInside(() -> new WebComponent(ARROW_BUTTON_DESCRIPTION));
        input = findInside(() -> new WebComponent(INPUT_DESCRIPTION));

        addButton = findInside(() -> new WebComponent(PARENT_DIV_DESCRIPTION))
                .findInside(Button::new, Button.Requirements.Equals.byTip("Добавить новый объект"));
    }

    @Override
    protected @NotNull Component getInput() {
        return input;
    }

    public @NotNull Card clickOnAddButton() {
        toAction().hover();
        addButton.click();
        return Card.getActiveCard();
    }

    public @NotNull Menu clickOnArrowButton() {
        arrowButton.toAction().click();
        return find(Menu::new);
    }
}
