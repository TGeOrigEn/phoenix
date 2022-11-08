package example.field.select.menu;

import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.By;
import org.untitled.phoenix.component.Component;
import org.untitled.phoenix.component.Description;
import org.untitled.phoenix.component.requirement.BaseRequirement;

public class Menu extends Component {

    public static final Description DEFAULT_DESCRIPTION = new Description(By.cssSelector("div[id^='boundlist'][class='x-boundlist x-boundlist-floating x-layer x-boundlist-default x-border-box']:not([style*=display])"), "Список вариантов");

    @Override
    protected @NotNull Description initialize() {
        return DEFAULT_DESCRIPTION;
    }

    public @NotNull Option getOptionBy(BaseRequirement<Option> requirement) {
        return findInside(Option::new, requirement);
    }
}
