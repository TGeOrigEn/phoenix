package example.field.nested;

import example.field.Field;
import org.gems.WebComponent;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.By;
import org.untitled.phoenix.component.Component;
import org.untitled.phoenix.component.Description;

public class CheckboxField extends Field {

    private static final @NotNull Description INPUT_DESCRIPTION = new Description(By.tagName("input"), "Ввод");

    private final @NotNull Component input;

    public CheckboxField() {
        input = findInside(() -> new WebComponent(INPUT_DESCRIPTION));
    }

    @Override
    protected @NotNull Component getInput() {
        return input;
    }

    public boolean isChecked() {
        return toAction().getCssClass().contains("checked");
    }

    public void check() {
        input.toAction().click();
    }
}
