package org.untitled.phoenix.configuration;

import org.jetbrains.annotations.NotNull;

import org.jetbrains.annotations.Nullable;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.WebDriver;

import java.nio.file.Paths;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.UUID;
import java.net.URL;

public final class Configuration {

    private static @NotNull Path pathToDownload;

    private static @NotNull WebDriver webDriver;

    private static @Nullable URL remoteAddress;

    static {
        final var toDownload = Paths.get(String.format("build/downloads/%s", UUID.randomUUID())).toAbsolutePath();
        final var toWebDriver = Paths.get("drivers/chrome/chromedriver.exe").toAbsolutePath();

        final var prefs = new HashMap<String, Object>();
        final var options = new ChromeOptions();

        System.setProperty("webdriver.chrome.driver", toWebDriver.toString());
        prefs.put("download.default_directory", Configuration.pathToDownload);
        options.setExperimentalOption("prefs", prefs);

        webDriver = new ChromeDriver(options);
        pathToDownload = toDownload;
        remoteAddress = null;
    }

    public static void configure(@NotNull Path pathToWebDriver, @NotNull RemoteWebDriver webDriver, @NotNull Path pathToDownload, @NotNull URL remoteAddress) {
        System.setProperty("webdriver.chrome.driver", pathToWebDriver.toString());

        Configuration.pathToDownload = pathToDownload;
        Configuration.remoteAddress = remoteAddress;
        Configuration.webDriver = webDriver;
    }

    public static void configure(@NotNull Path pathToWebDriver, @NotNull WebDriver webDriver, @NotNull Path pathToDownload) {
        System.setProperty("webdriver.chrome.driver", pathToWebDriver.toString());

        Configuration.pathToDownload = pathToDownload;
        Configuration.webDriver = webDriver;
    }

    public static boolean isRemote() {
        return RemoteWebDriver.class.isAssignableFrom(webDriver.getClass());
    }

    public static @NotNull Path getPathToDownload() {
        return pathToDownload;
    }

    public static @Nullable URL getRemoteAddress() {
        return remoteAddress;
    }

    public static @NotNull WebDriver getWebDriver() {
        return webDriver;
    }
}
