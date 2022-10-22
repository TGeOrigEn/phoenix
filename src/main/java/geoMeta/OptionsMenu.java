package geoMeta;

import org.untitled.phoenix.component.Component;
import org.untitled.phoenix.component.Description;
import org.gems.WebComponent;
import org.untitled.phoenix.component.Requirement;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.By;

public class OptionsMenu extends Component {

    private static final Description OPTION_DESCRIPTION = new Description(By.cssSelector("span[class^='x-menu-item-text']"), "Опция");

    @Override
    protected @NotNull Description initialize() {
        return new Description(By.cssSelector("div[id^='menu'][class*='x-layer']:not([style*=visibility])"), "Меню опций");
    }

    public void clickOnOption(String optionName) {
        findInside(() -> new WebComponent(OPTION_DESCRIPTION), Requirement.byText(optionName)).toAction().click();
    }
}
