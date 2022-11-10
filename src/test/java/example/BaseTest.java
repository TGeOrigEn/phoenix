package example;

import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.chrome.ChromeOptions;
import org.untitled.phoenix.configuration.Configuration;

import java.nio.file.Paths;

public abstract class BaseTest {

    @BeforeEach
    public void beforeEach() {
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
    }
}
