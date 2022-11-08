package example.field.select;

import example.Button;
import example.field.Field;
import example.field.select.menu.Menu;
import org.gems.WebComponent;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.By;
import org.untitled.phoenix.component.Component;
import org.untitled.phoenix.component.Description;

public class Select extends Field {

    private static final Description INPUT_DESCRIPTION = new Description(By.tagName("input"), "Ввод");

    private static final Description ARROW_DESCRIPTION = new Description(By.cssSelector("div[class*='x-form-arrow-trigger']"), "Стрелка");

    private final Component input;

    private final Component arrow;

    private final Component add;

    public Select() {
        add = findInside(Button::new, Button.Requirements.Equals.byTip("Добавить новый объект"));
        input = findInside(() -> new WebComponent(INPUT_DESCRIPTION));
        arrow = findInside(() -> new WebComponent(ARROW_DESCRIPTION));
    }

    @Override
    protected @NotNull Component getInput() {
        return input;
    }

    public @NotNull Menu clickOnArrow() {
        arrow.toAction().click();
        return find(Menu::new);
    }
}
