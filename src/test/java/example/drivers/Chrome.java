package example.drivers;

import org.openqa.selenium.chrome.ChromeDriver;
import org.untitled.phoenix.configuration.Configuration;

import org.jetbrains.annotations.NotNull;

import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.nio.file.Path;
import java.time.Duration;
import java.io.File;
import java.net.URL;

public final class Chrome {

    private static final @NotNull File CRYPTOGRAPHY_EXTENSION_FILE = new File("/opt/cades_plugin.crx");

    private static final @NotNull String PATH_TO_WEB_DRIVER;

    static {
        final var address = Chrome.class.getClassLoader().getResource("drivers/chrome/chromedriver.exe");
        if (address == null) throw new NullPointerException("Не был найден хром-драйвер.");
        PATH_TO_WEB_DRIVER = address.getPath();
    }

    public static void setDefault(@NotNull Path pathToDownload, @NotNull Duration timeout) {
        Configuration.configure(new ChromeDriver(getOptionsDefault(timeout)), pathToDownload);
    }

    public static void setDefault(@NotNull Path pathToDownload, @NotNull URL remoteAddress, @NotNull Duration timeout) {
        Configuration.configure(new RemoteWebDriver(remoteAddress, getOptionsDefault(timeout)), pathToDownload, remoteAddress);
    }

    public static void setWithCryptography(@NotNull Path pathToDownload, @NotNull Duration timeout) {
        Configuration.configure(new ChromeDriver(getOptionsWithCryptography(timeout)), pathToDownload);
    }

    public static void setWithCryptography(@NotNull Path pathToDownload, @NotNull URL remoteAddress, @NotNull Duration timeout) {
        Configuration.configure(new RemoteWebDriver(remoteAddress, getOptionsWithCryptography(timeout)), pathToDownload, remoteAddress);
    }

    private static @NotNull ChromeOptions getOptionsWithCryptography(@NotNull Duration timeout) {
        final var options = getOptionsDefault(timeout);
        options.addExtensions(CRYPTOGRAPHY_EXTENSION_FILE);
        return options;
    }

    private static @NotNull ChromeOptions getOptionsDefault(@NotNull Duration timeout) {
        final var options = new ChromeOptions();

        System.setProperty("webdriver.chrome.driver", PATH_TO_WEB_DRIVER);

        options.setCapability("sessionTimeout", String.format("%dms", timeout.toMillis()));
        options.addArguments("--window-size=1920,1080");
        options.setCapability("browserName", "chrome");
        options.setCapability("enableVideo", true);
        options.setCapability("version", "106.0");
        options.setCapability("enableVNC", true);

        return options;
    }
}
