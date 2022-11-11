package example;

import example.drivers.Chrome;
import io.qameta.allure.Step;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.chrome.ChromeOptions;
import org.other.Allure;
import org.untitled.phoenix.configuration.Configuration;

import java.net.MalformedURLException;
import java.nio.file.Paths;
import java.time.Duration;

public abstract class BaseTest {

    private final static @Nullable String REMOTE_ADDRESS = null;

    protected @NotNull Duration getTimeout() {
        return Duration.ofSeconds(30);
    }

    protected abstract @NotNull String getAddress();

    protected @NotNull MutableCapabilities getCapabilities() {
        return Chrome.getDefaultOptions(getTimeout());
    }

    @BeforeEach
    @DisplayName("Инициализировать веб-драйвер")
    public void webDriverInitialization() throws MalformedURLException {
        final var pathToWebDriver = this.getClass().getClassLoader().getResource("drivers/chromedriver.exe");
        final var downloadDirectory = Paths.get("build/downloads/").toFile();

        if (pathToWebDriver == null) throw new RuntimeException("Веб-драйвера не существует.");
        else System.setProperty("webdriver.chrome.driver", pathToWebDriver.getPath());

        Configuration.setChromeDriver(REMOTE_ADDRESS, downloadDirectory.getAbsolutePath(), (ChromeOptions) getCapabilities());
        Configuration.getWebDriver().manage().window().setSize(new Dimension(1920, 1080));
    }

    @BeforeEach
    @DisplayName("Перейти на стартовую страницу")
    public void openAddress() {
        Configuration.getWebDriver().get(getAddress());
    }

    @AfterEach
    public void closeWebDriver() {

        Allure.attachScreenshot("Последний снимок");

        if (Configuration.isRemote())
            Allure.attachVideo();

        Configuration.getWebDriver().quit();
    }
}
