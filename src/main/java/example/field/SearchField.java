package example.field;

import org.gems.WebComponent;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.By;
import org.untitled.phoenix.component.Component;
import org.untitled.phoenix.component.Description;

public class SearchField extends Field {

    public static final @NotNull Description INPUT_DESCRIPTION = new Description(By.tagName("input"), "Ввод");

    private static final @NotNull Description ARROW_BUTTON_DESCRIPTION = new Description(By.cssSelector("div[class*='x-form-arrow-trigger']"), "Кнопка 'Стрелка'");

    private final @NotNull Component input;

    public SearchField() {
        input = findInside(() -> new WebComponent(INPUT_DESCRIPTION));
    }

    @Override
    protected @NotNull Component getInput() {
        return input;
    }
}
