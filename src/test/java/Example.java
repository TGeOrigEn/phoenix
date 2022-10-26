import geoMeta.*;
import org.untitled.phoenix.component.Component;
import org.untitled.phoenix.component.requirement.generic.Requirement;
import org.untitled.phoenix.configuration.Configuration;
import org.untitled.phoenix.component.Description;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.chrome.ChromeDriver;

import java.time.Duration;

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

    private void open() {
        final var requirementB = Item.Requirements.Equals.byName("autotests")
                .or(Item.Requirements.Equals.byName("AutoTestCatalog")).toNegative();

        final var requirementC = Item.Requirements.byExpand(false).and(Item.Requirements.byExpendable(true));
        final var requirement = requirementB.and(requirementC);

        final var component = Component.find(Item::new, requirement);
        while (true)
            component.expand();
    }

    private void close() {
        final var requirementB = Item.Requirements.Equals.byName("Шкотовский МР")
                .or(Item.Requirements.Equals.byName("Владивостокский ГО"))
                .or(Item.Requirements.Equals.byName("Приморский край"));

        final var requirementC = Item.Requirements.byExpand(false).toNegative().and(Item.Requirements.byExpendable(false));
        final var requirement = requirementB.and(requirementC);

        final var component = Component.find(Item::new, requirement);
        while (Component.has(component, Requirement.byAvailable(true), Duration.ofSeconds(1)))
            component.expand();
    }

    @Test
    public void test() {
        Component.find(AuthorizationForm::new).logIn("gemsAdmin", "gemsAdmin123$");

        while (true) {
            open();
            //close();
        }
    }
}
