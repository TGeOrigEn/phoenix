import geoMeta.*;
import org.untitled.phoenix.component.Component;
import org.untitled.phoenix.configuration.Configuration;
import org.untitled.phoenix.component.Description;
import org.gems.WebComponent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.chrome.ChromeDriver;

public class Example {

    private static final Description CREATE_NEW_OBJECT_BUTTON_DESCRIPTION = new Description(By.cssSelector("a[data-qtip='Создать новый объект']"), "Кнопка 'Создать новый объект'");

    private static final Description REFRESH_TABLE_BUTTON_DESCRIPTION = new Description(By.cssSelector("a[data-qtip='Обновить таблицу']"), "Кнопка 'Обновить таблицу'");

    private static final Description MORE_OPTIONS_BUTTON_DESCRIPTION = new Description(By.cssSelector("span[class*='fg-more']"), "Кнопка 'Ещё'");

    @BeforeEach
    public void beforeEach() {
        var url = this.getClass().getClassLoader().getResource("drivers/chromedriver.exe");

        if (url == null) throw new RuntimeException();
        System.setProperty("webdriver.chrome.driver", url.getPath());

        Configuration.setWebDriver(new ChromeDriver());
        var dimension = new Dimension(1920, 1080);
        Configuration.getWebDriver().manage().window().setSize(dimension);

        Configuration.getWebDriver().get("https://autotests.gemsdev.ru/");
    }

    @Test
    public void test() {
        Component.find(AuthorizationForm::new).logIn("gemsAdmin", "gemsAdmin123$");

        Component.find(Item::new, Item.byName("Приморский край")).expand();
        Component.find(Item::new, Item.byName("Владивостокский ГО")).expand();
        Component.find(Item::new, Item.byName("Шкотовский МР")).expand();

        Component.find(Item::new, Item.byName("Результаты запросов (Sapphire)")).expand();
    }

    @Test
    public void test_1() {

        Component.find(AuthorizationForm::new).logIn("gemsAdmin", "gemsAdmin123$");

        final var item_0 = Component.find(Item::new, Item.byName("Приморский край"));
        item_0.toWebElement();
        item_0.getCondition().setEnabled(false);
        item_0.expand();

        final var item_1 = Component.find(Item::new, Item.byName("autotests"));
        item_1.toWebElement();
        item_1.getCondition().setEnabled(false);
        item_1.expand();

        final var item_2 = Component.find(Item::new, Item.byName("presentationPanel"));
        item_2.toWebElement();
        item_2.getCondition().setEnabled(false);
        item_2.expand();

        final var item_3 = Component.find(Item::new, Item.byName("Растительность"));
        item_3.toWebElement();
        item_3.getCondition().setEnabled(false);
        item_3.openTable();

        Component.find(() -> new WebComponent(CREATE_NEW_OBJECT_BUTTON_DESCRIPTION)).toAction().click();

        final var objectCard = Component.find(ObjectCard::new);
        objectCard.save();
        objectCard.close();

        Component.find(() -> new WebComponent(REFRESH_TABLE_BUTTON_DESCRIPTION)).toAction().click();

        Component.find(() -> new WebComponent(MORE_OPTIONS_BUTTON_DESCRIPTION)).toAction().click();

        Component.find(OptionsMenu::new).clickOnOption("Удалить объект");

        Component.find(NotificationWindow::new).clickOnButton("Удалить");

    }
}
