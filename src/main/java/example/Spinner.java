package example;

import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.By;
import org.untitled.phoenix.component.Component;
import org.untitled.phoenix.component.Description;
import org.untitled.phoenix.component.requirement.generic.Requirement;

import java.time.Duration;

public class Spinner extends Component {

    public static final @NotNull Description DEFAULT_DESCRIPTION = new Description(By.cssSelector("div[id^='ext-element'][class*='x-mask']:not([style*=display])"), "Индикатор загрузки");

    @Override
    protected @NotNull Description initialize() {
        return DEFAULT_DESCRIPTION;
    }

    public void wait(@NotNull Duration timeout) {
        if (Component.has(this, Requirement.byAvailable(true), Duration.ofSeconds(1))) {
            Component.should(this, Requirement.byAvailable(false), timeout);
            wait(timeout);
        }
    }
}
