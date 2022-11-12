package example;

import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.By;
import org.untitled.phoenix.component.Component;
import org.untitled.phoenix.component.Description;

public class QuickTip extends Component {

    public static final @NotNull Description DEFAULT_DESCRIPTION = new Description(By.cssSelector("div[id='ext-quicktips-tip']:not([style*='display'])"), "Быстрая подсказка");

    @Override
    protected @NotNull Description initialize() {
        return DEFAULT_DESCRIPTION;
    }
}
