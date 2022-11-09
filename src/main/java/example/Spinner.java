package example;

import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.By;
import org.untitled.phoenix.component.Component;
import org.untitled.phoenix.component.Description;

public class Spinner extends Component {

    public static final @NotNull Description DEFAULT_DESCRIPTION = new Description(By.cssSelector("div[id^='ext-element'][class*='x-mask']:not([style*=display])"), "Индикатор загрузки");

    @Override
    protected @NotNull Description initialize() {
        return DEFAULT_DESCRIPTION;
    }
}
