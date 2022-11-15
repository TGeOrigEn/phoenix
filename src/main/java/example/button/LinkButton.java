package example.button;

import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.By;
import org.untitled.phoenix.component.Description;

public class LinkButton extends Button {

    public static final @NotNull Description DEFAULT_DESCRIPTION = new Description(By.cssSelector("div[class^='x-component']:not([style*='display']) a[style*='cursor: pointer'])"), "Кнопка 'Ссылка'");

    @Override
    protected @NotNull Description initialize() {
        return DEFAULT_DESCRIPTION;
    }
}
