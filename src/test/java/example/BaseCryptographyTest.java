package example;

import org.gems.WebComponent;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.By;
import org.untitled.phoenix.component.Component;
import org.untitled.phoenix.component.Description;

public abstract class BaseCryptographyTest extends BaseTest {

    private static final @NotNull Description INPUT_DESCRIPTION = new Description(By.cssSelector("input[placeholder='Добавить новый']"), "Ввод 'Добавить новый'");

    private static final @NotNull Description ADD_BUTTON_DESCRIPTION = new Description(By.cssSelector("button[id='add_button']"), "Кнопка 'Добавить'");

    private static final @NotNull Description SAVE_BUTTON_DESCRIPTION = new Description(By.cssSelector("button[name='save']"), "Кнопка 'Сохранить'");

    @Override
    protected @NotNull Driver getDriver() {
        return Driver.CHROME_CRYPTOGRAPHY;
    }

    @Override
    protected @NotNull String getAddress() {
        return "file:///etc/opt/cprocsp/trusted_sites.html";
    }

    protected abstract @NotNull String[] getAcceptedAddresses();

    @BeforeEach
    public void cryptoProInitialization() {
        for (var address : getAcceptedAddresses()) {
            Component.find(() -> new WebComponent(INPUT_DESCRIPTION)).toAction().setValue(address);
            Component.find(() -> new WebComponent(ADD_BUTTON_DESCRIPTION)).toAction().click();
        }
        Component.find(() -> new WebComponent(SAVE_BUTTON_DESCRIPTION)).toAction().click();
    }
}
