package org.untitled.phoenix.configuration;

import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.UUID;

public final class Configuration {

    private static @NotNull WebDriver webDriver;

    private static @NotNull String downloadDirectory = Paths.get("target").toAbsolutePath().toString();

    public static @NotNull String getDownloadDirectory() {
        return downloadDirectory;
    }

    public static @NotNull WebDriver getWebDriver() {
        return webDriver;
    }

    public static void setWebDriver(@NotNull WebDriver webDriver) {
        Configuration.webDriver = webDriver;
        Configuration.webDriver.navigate().to("chrome://settings/downloads");
        final var s = webDriver.getPageSource();
        var g = 0;
    }
}
