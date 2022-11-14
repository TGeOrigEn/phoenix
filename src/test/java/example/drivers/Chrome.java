package example.drivers;

import org.openqa.selenium.chrome.ChromeDriver;
import org.untitled.phoenix.configuration.Configuration;

import org.jetbrains.annotations.NotNull;

import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.nio.file.Paths;
import java.nio.file.Path;
import java.time.Duration;
import java.io.File;
import java.net.URL;

public final class Chrome {

    private static final @NotNull Path PATH_TO_WEB_DRIVER = Paths.get("drivers/chrome/chromedriver.exe").toAbsolutePath();

    private static final @NotNull File CRYPTOGRAPHY_EXTENSION = new File("/opt/cades_plugin.crx");

    public static void setDefault(@NotNull Path pathToDownload, @NotNull Duration timeout) {
        Configuration.configure(PATH_TO_WEB_DRIVER, new ChromeDriver(getOptionsDefault(timeout)), pathToDownload);
    }

    public static void setDefault(@NotNull Path pathToDownload, @NotNull URL remoteAddress, @NotNull Duration timeout) {
        Configuration.configure(PATH_TO_WEB_DRIVER, new RemoteWebDriver(getOptionsDefault(timeout)), pathToDownload, remoteAddress);
    }

    public static void setWithCryptography(@NotNull Path pathToDownload, @NotNull Duration timeout) {
        Configuration.configure(PATH_TO_WEB_DRIVER, new ChromeDriver(getOptionsWithCryptography(timeout)), pathToDownload);
    }

    public static void setWithCryptography(@NotNull Path pathToDownload, @NotNull URL remoteAddress, @NotNull Duration timeout) {
        Configuration.configure(PATH_TO_WEB_DRIVER, new RemoteWebDriver(getOptionsWithCryptography(timeout)), pathToDownload, remoteAddress);
    }

    private static @NotNull ChromeOptions getOptionsWithCryptography(@NotNull Duration timeout) {
        final var options = getOptionsDefault(timeout);
        options.addExtensions(CRYPTOGRAPHY_EXTENSION);
        return options;
    }

    private static @NotNull ChromeOptions getOptionsDefault(@NotNull Duration timeout) {
        final var options = new ChromeOptions();

        options.setCapability("sessionTimeout", String.format("%dms", timeout.toMillis()));
        options.setCapability("screenResolution", "1920x1080x24");
        options.setCapability("browserName", "chrome");
        options.setCapability("enableVideo", true);
        options.setCapability("version", "106.0");
        options.setCapability("enableVNC", true);

        return options;
    }
}
