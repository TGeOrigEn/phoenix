package org.untitled.phoenix.configuration;

import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;

import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.WebDriver;

import java.util.HashMap;
import java.util.UUID;

import java.io.File;
import java.net.URL;

public final class Configuration {

    private static @Nullable WebDriver webDriver;

    private static @Nullable String downloadDirectory;

    private static @Nullable URL remoteAddress = null;

    public static @NotNull String getDownloadDirectory() {
        if (downloadDirectory == null)
            throw new RuntimeException();
        return downloadDirectory;
    }

    public static @NotNull WebDriver getWebDriver() {
        if (webDriver == null) throw new RuntimeException();
        return webDriver;
    }

    public static boolean isRemote() {
        return getWebDriver().getClass().equals(RemoteWebDriver.class);
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
        new File(getDownloadDirectory()).mkdirs();
    }

    public static void setWebDriver(@NotNull String downloadDirectory, @NotNull ChromeOptions options) {
        Configuration.downloadDirectory = String.format("%s\\%s", downloadDirectory, UUID.randomUUID());

        final var prefs = new HashMap<String, Object>();
        prefs.put("download.default_directory", Configuration.downloadDirectory);

        options.setExperimentalOption("prefs", prefs);
        Configuration.webDriver = new ChromeDriver(options);

        new File(getDownloadDirectory()).mkdirs();
    }
}
