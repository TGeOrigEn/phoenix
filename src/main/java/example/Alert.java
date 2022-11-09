package example;

import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.By;
import org.untitled.phoenix.component.Component;
import org.untitled.phoenix.component.Description;

public class Alert extends Component {

    public static final @NotNull Description DEFAULT_DESCRIPTION = new Description(By.cssSelector("div[class^='x-window x-message-box x-layer']:not([class*=x-hidden-offsets])"), "Предупреждение");

    @Override
    protected @NotNull Description initialize() {
        return DEFAULT_DESCRIPTION;
    }
}
