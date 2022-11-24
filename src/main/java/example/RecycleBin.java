package example;

import example.window.Window;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.By;
import org.untitled.phoenix.component.Component;
import org.untitled.phoenix.component.Description;

public class RecycleBin extends Window {

    public static class Item extends Component {

        @Override
        protected @NotNull Description initialize() {
            return null;
        }
    }

    public static final @NotNull Description DEFAULT_DESCRIPTION = new Description(By.cssSelector("div[id^=RecycleBin][class*='x-window x-layer']"), "Корзина");

    @Override
    protected @NotNull Description initialize() {
        return DEFAULT_DESCRIPTION;
    }

    @Override
    public boolean isActive() {
        return true;
    }
}
