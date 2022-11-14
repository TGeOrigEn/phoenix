package example.drivers;

import org.untitled.phoenix.configuration.Configuration;

import org.jetbrains.annotations.NotNull;

import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.chrome.ChromeDriver;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.time.Duration;
import java.io.File;
import java.net.URL;

public final class Chrome {

    static {
        final var pathToChromeDriver = Chrome.class.getClassLoader().getResource("drivers/chrome/chromedriver.exe");
        if (pathToChromeDriver == null) throw new NullPointerException("Хром-драйвера не существует.");
        System.setProperty("webdriver.chrome.driver", pathToChromeDriver.getPath());
    }

    public static void setDefault(@NotNull Path pathToDownload, @NotNull Duration timeout) {
        Configuration.configure(new ChromeDriver(getOptionsDefault(timeout)), pathToDownload);
    }

    public static void setDefault(@NotNull Path pathToDownload, @NotNull String remoteAddress, @NotNull Duration timeout) throws MalformedURLException {
        Configuration.configure(new RemoteWebDriver(new URL(remoteAddress), getOptionsDefault(timeout)), pathToDownload, remoteAddress);
    }

    public static void setWithCryptography(@NotNull Path pathToDownload, @NotNull Duration timeout) {
        Configuration.configure(new ChromeDriver(getOptionsWithCryptography(timeout)), pathToDownload);
    }

    public static void setWithCryptography(@NotNull Path pathToDownload, @NotNull String remoteAddress, @NotNull Duration timeout) throws MalformedURLException {
        Configuration.configure(new RemoteWebDriver(new URL(remoteAddress), getOptionsWithCryptography(timeout)), pathToDownload, remoteAddress);
    }

    private static @NotNull ChromeOptions getOptionsWithCryptography(@NotNull Duration timeout) {
        final var options = getOptionsDefault(timeout);
        options.addExtensions(new File("/opt/cades_plugin.crx"));
        return options;
    }

    private static @NotNull ChromeOptions getOptionsDefault(@NotNull Duration timeout) {
        final var options = new ChromeOptions();

        options.setCapability("sessionTimeout", String.format("%dms", timeout.toMillis()));
        options.addArguments("--window-size=1920,1080");
        options.setCapability("browserName", "chrome");
        options.setCapability("enableVideo", true);
        options.setCapability("version", "106.0");
        options.setCapability("enableVNC", true);

        return options;
    }
}
