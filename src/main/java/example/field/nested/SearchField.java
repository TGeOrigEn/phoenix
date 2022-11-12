package example.field.nested;

import example.field.Field;
import org.gems.WebComponent;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.By;
import org.untitled.phoenix.component.Component;
import org.untitled.phoenix.component.Description;

public class SearchField extends Field {

    private static final @NotNull Description CLEAR_BUTTON_DESCRIPTION = new Description(By.cssSelector("div[class*='x-form-clear-trigger']"), "Кнопка 'Отчистить'");

    private static final @NotNull Description SEARCH_BUTTON_DESCRIPTION = new Description(By.cssSelector("div[class*='x-form-search-trigger']"), "Кнопка 'Поиск'");

    public static final @NotNull Description INPUT_DESCRIPTION = new Description(By.tagName("input"), "Ввод");

    private final @NotNull Component searchButton;

    private final @NotNull Component clearButton;

    private final @NotNull Component input;

    public SearchField() {
        searchButton = findInside(() -> new WebComponent(SEARCH_BUTTON_DESCRIPTION));
        clearButton = findInside(() -> new WebComponent(CLEAR_BUTTON_DESCRIPTION));
        input = findInside(() -> new WebComponent(INPUT_DESCRIPTION));
    }

    @Override
    protected @NotNull Component getInput() {
        return input;
    }

    @Override
    public void setValue(@NotNull String value) {
        if (clearButton.isAvailable())
            clearButton.toAction().click();

        input.toAction().sendKeys(value);
        searchButton.toAction().click();
    }
}
