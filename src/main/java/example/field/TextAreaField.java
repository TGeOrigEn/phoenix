package example.field;

import org.gems.WebComponent;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.By;
import org.untitled.phoenix.component.Component;
import org.untitled.phoenix.component.Description;

public class TextAreaField extends Field {

    private static final @NotNull Description TEXT_AREA_DESCRIPTION = new Description(By.tagName("textarea"), "Текстовая область");

    private final @NotNull Component textArea;

    public TextAreaField() {
        textArea = findInside(() -> new WebComponent(TEXT_AREA_DESCRIPTION));
    }

    @Override
    protected @NotNull Component getInput() {
        return textArea;
    }
}
