package example.window;

import example.Map;
import org.gems.WebComponent;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.By;
import org.openqa.selenium.Point;
import org.untitled.phoenix.component.Action;
import org.untitled.phoenix.component.Component;
import org.untitled.phoenix.component.Description;

public class MapWindow extends Window {

    public static final @NotNull Description DEFAULT_DESCRIPTION = new Description(By.cssSelector("div[id^='ext-comp'].x-panel.x-layer:not([style*='display']):not([class*='hidden'])"), "Окно с картой");

    private static final @NotNull Description SAVE_BUTTON_DESCRIPTION = new Description(By.cssSelector("img[class*='x-tool-save']"), "Кнопка 'Сохранить'");

    private final @NotNull Component saveButton;

    public MapWindow() {
        saveButton = findInside(() -> new WebComponent(SAVE_BUTTON_DESCRIPTION));
    }

    @Override
    public boolean isActive() {
        return true;
    }

    @Override
    protected @NotNull Description initialize() {
        return DEFAULT_DESCRIPTION;
    }

    public void save() {
        saveButton.toAction().click();
    }
}
