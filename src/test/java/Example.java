import geoMeta.*;
import org.openqa.selenium.chrome.ChromeOptions;
import org.untitled.phoenix.component.Component;
import org.untitled.phoenix.component.requirement.generic.Requirement;
import org.untitled.phoenix.configuration.Configuration;
import org.untitled.phoenix.component.Description;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;

import java.io.IOException;
import java.nio.file.Paths;
import java.time.Duration;

public class Example {

    private static final Description CREATE_NEW_OBJECT_BUTTON_DESCRIPTION = new Description(By.cssSelector("a[data-qtip='Создать новый объект']"), "Кнопка 'Создать новый объект'");

    private static final Description REFRESH_TABLE_BUTTON_DESCRIPTION = new Description(By.cssSelector("a[data-qtip='Обновить таблицу']"), "Кнопка 'Обновить таблицу'");

    private static final Description MORE_OPTIONS_BUTTON_DESCRIPTION = new Description(By.cssSelector("span[class*='fg-more']"), "Кнопка 'Ещё'");

    @BeforeEach
    public void beforeEach() throws IOException {
        var url = this.getClass().getClassLoader().getResource("drivers/chromedriver.exe");

        if (url == null) throw new RuntimeException();
        System.setProperty("webdriver.chrome.driver", url.getPath());

        final var options = new ChromeOptions();
        options.setCapability("enableVNC", true);
        options.setCapability("screenResolution", "1920x1080x24");
        options.setBrowserVersion("test");

        final var file = Paths.get("build/downloads/").toFile();

        //Configuration.setRemoteWebDriver(new URL("http://10.5.1.167:4444/wd/hub"), file.getAbsolutePath(), options);
        Configuration.setWebDriver(file.getAbsolutePath(), options);
        var dimension = new Dimension(1920, 1080);

        Configuration.getWebDriver().manage().window().setSize(dimension);
        Configuration.getWebDriver().get("https://autotests.gemsdev.ru/");

/*        final var sessionId = ((RemoteWebDriver) Configuration.getWebDriver()).getSessionId();
        final var pageContent = Action.getPageContent(new URL(String.format("http://10.5.1.167:4444/download/%s/", sessionId))).replace("</a>", "</a>\n");
        final var patter = Pattern.compile("(?<=href=\").*(?=\")");
        final var matcher = patter.matcher(pageContent);
        final var list = new ArrayList<String>();

        while (matcher.find()) {
            list.add(matcher.group());
        }

        for (var name: list) {
            InputStream in = new URL(String.format("http://10.5.1.167:4444/download/%s/%s", sessionId, name)).openStream();
            Files.copy(in, Paths.get(name), StandardCopyOption.REPLACE_EXISTING);
        }

        for (var name: list) {
            HttpURLConnection httpCon = (HttpURLConnection) new URL(String.format("http://10.5.1.167:4444/download/%s/%s", sessionId, name)).openConnection();
            httpCon.setRequestMethod("DELETE");
            httpCon.getResponseCode();
        }*/
    }

    private void example() {
        final var requirementB = Item.Requirements.Equals.byName("autotests").toNegative()
                .and(Item.Requirements.Equals.byName("AutoTestCatalog").toNegative());

        final var requirementC = Item.Requirements.byExpand(false).and(Item.Requirements.byExpendable(true));
        final var requirement = requirementB.and(requirementC);

        final var component = Component.find(Item::new, requirement);
        while (Component.has(component, Requirement.byAvailable(true), Duration.ZERO))
            component.expand();
    }

    private void open() {

        final var requirementA = Item.Requirements.Equals.byName("Шкотовский МР")
                .or(Item.Requirements.Equals.byName("Владивостокский ГО"))
                .or(Item.Requirements.Equals.byName("Приморский край"));

        final var requirementB = Item.Requirements.byExpand(false)
                .and(Item.Requirements.byExpendable(true));

        final var item = Component.find(Item::new, requirementA.and(requirementB));

        for (int i = 0; i < 3; i++)
            item.expand();
    }

    private void close() {

        final var requirementA = Item.Requirements.Equals.byName("Шкотовский МР")
                .or(Item.Requirements.Equals.byName("Владивостокский ГО"))
                .or(Item.Requirements.Equals.byName("Приморский край"));

        final var requirementB = Item.Requirements.byExpand(false).toNegative()
                .and(Item.Requirements.byExpendable(true));

        final var item = Component.find(Item::new, requirementA.and(requirementB));

        for (int i = 0; i < 3; i++)
            item.expand();
    }

    @Test
    public void test() {
        Component.find(AuthorizationForm::new).logIn("gemsAdmin", "gemsAdmin123$");

        while (true) {
            open();
            close();
        }

        /*Component.find(TextButton::new, TextButton.byQuickTipText("Графический отчет")).click();
        Component.find(TextButton::new, TextButton.byText("Далее")).click();
        Component.find(TextButton::new, TextButton.byText("Сформировать")).getAction().click();

        Component.find(TextButton::new, TextButton.byQuickTipText("Графический отчет")).click();
        Component.find(TextButton::new, TextButton.byText("Далее")).click();
        final var s = Component.find(TextButton::new, TextButton.byText("Сформировать")).getAction().download(Duration.ofSeconds(3600), 2);
        final var text = 0;*/
    }
}
