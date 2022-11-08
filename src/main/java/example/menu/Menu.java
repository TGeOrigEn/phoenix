package example.menu;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;
import org.openqa.selenium.By;
import org.untitled.phoenix.component.Component;
import org.untitled.phoenix.component.Description;
import org.untitled.phoenix.component.requirement.generic.Requirement;

import java.util.List;

public class Menu extends Component {

    public static final @NotNull Description DEFAULT_DESCRIPTION = new Description(By.cssSelector("div[class*='x-menu x-layer']:not([style*=visibility])"), "Выпадающий список");

    @Override
    protected @NotNull Description initialize() {
        return DEFAULT_DESCRIPTION;
    }

    public @NotNull @Unmodifiable List<Option> getOptions() {
        return findInsideEveryone(Option::new);
    }

    public <TValue> @NotNull @Unmodifiable List<Option> getOptionsBy(Requirement<Option, TValue> requirement) {
        return findInsideEveryone(Option::new, requirement);
    }
}
