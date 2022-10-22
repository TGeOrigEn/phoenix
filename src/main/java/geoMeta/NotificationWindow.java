package geoMeta;

import org.untitled.phoenix.component.Component;
import org.untitled.phoenix.component.Description;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.By;

public class NotificationWindow extends Component {

    @Override
    protected @NotNull Description initialize() {
        return new Description(By.cssSelector("div[class^='x-window x-message-box x-layer']:not([class*=x-hidden-offsets])"), "Окно уведомления");
    }

    public void clickOnButton(String buttonText) {
        findInside(TextButton::new, TextButton.byText(buttonText)).click();
    }
}
