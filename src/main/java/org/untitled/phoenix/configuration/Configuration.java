package org.untitled.phoenix.configuration;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.UUID;

public final class Configuration {

    private static @NotNull WebDriver webDriver;

    private static @NotNull String downloadDirectory;

    private static @Nullable URL remoteAddress = null;

    public static @NotNull String getDownloadDirectory() {
        return downloadDirectory;
    }

    public static @NotNull WebDriver getWebDriver() {
        return webDriver;
    }

    public static boolean isRemote() {
        return webDriver.getClass().equals(RemoteWebDriver.class);
    }

    public static @Nullable URL getRemoteAddress() {
        return remoteAddress;
    }

    public static void setRemoteWebDriver(@NotNull URL remoteAddress, @NotNull String downloadDirectory, @NotNull ChromeOptions options) {
        Configuration.downloadDirectory = String.format("%s\\%s", downloadDirectory, UUID.randomUUID());

        final var prefs = new HashMap<String, Object>();
        prefs.put("download.default_directory", Configuration.downloadDirectory);

        options.setExperimentalOption("prefs", prefs);
        Configuration.webDriver = new RemoteWebDriver(remoteAddress, options);

        Configuration.remoteAddress = remoteAddress;
        new File(Configuration.downloadDirectory).mkdirs();
    }

    public static void setWebDriver(@NotNull String downloadDirectory, @NotNull ChromeOptions options) {
        Configuration.downloadDirectory = String.format("%s\\%s", downloadDirectory, UUID.randomUUID());

        final var prefs = new HashMap<String, Object>();
        prefs.put("download.default_directory", Configuration.downloadDirectory);

        options.setExperimentalOption("prefs", prefs);
        Configuration.webDriver = new ChromeDriver(options);

        new File(Configuration.downloadDirectory).mkdirs();
    }
}
