package example;

import example.drivers.Chrome;
import io.qameta.allure.Allure;
import io.qameta.allure.model.Status;
import io.qameta.allure.model.StepResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.chrome.ChromeOptions;
import org.other.Report;
import org.untitled.phoenix.configuration.Configuration;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.UUID;

public abstract class BaseTest {

    private final static @Nullable String REMOTE_ADDRESS = "http://10.5.1.167:4444/wd/hub";

    protected @NotNull Duration getTimeout() {
        return Duration.ofSeconds(120);
    }

    protected abstract @NotNull String getAddress();

    protected @NotNull MutableCapabilities getCapabilities() {
        return Chrome.getDefaultOptions(getTimeout());
    }

    @BeforeEach
    public void webDriverInitialization() throws MalformedURLException {
        URL pathToWebDriver;

        if (System.getProperty("os.name").contains("windows")) pathToWebDriver = this.getClass().getClassLoader().getResource("drivers/chrome/chromedriver.exe");
        else pathToWebDriver = this.getClass().getClassLoader().getResource("drivers/chrome/chromedriver");

        final var downloadDirectory = Paths.get("build/downloads/").toFile();

        if (pathToWebDriver == null) throw new RuntimeException("Веб-драйвера не существует.");
        else System.setProperty("webdriver.chrome.driver", pathToWebDriver.getPath());

        Configuration.setChromeDriver(REMOTE_ADDRESS, downloadDirectory.getAbsolutePath(), (ChromeOptions) getCapabilities());
        Configuration.getWebDriver().manage().window().setSize(new Dimension(1280, 720));
    }

    @BeforeEach
    public void openAddress() {
        Configuration.getWebDriver().get(getAddress());
        Report.setStartTime(System.currentTimeMillis());
    }

    @BeforeEach
    public void reportInitialization() {
        Report.clear();
    }

    @AfterEach
    public void closeWebDriver() throws IOException {
        Report.perform();
        Configuration.getWebDriver().quit();
        if (Report.isFailed()) Assertions.fail("Зафиксированы ошибки");
    }
}
