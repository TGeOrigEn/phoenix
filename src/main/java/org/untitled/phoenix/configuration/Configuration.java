package org.untitled.phoenix.configuration;

import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;

import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.WebDriver;

import java.nio.file.Path;
import java.net.URL;

public final class Configuration {

    private static @Nullable Path pathToDownload;

    private static @Nullable WebDriver webDriver;

    private static @Nullable String remoteAddress;


    public static void configure(@NotNull RemoteWebDriver webDriver, @NotNull Path pathToDownload, @NotNull String remoteAddress) {
        Configuration.pathToDownload = pathToDownload;
        Configuration.remoteAddress = remoteAddress;
        Configuration.webDriver = webDriver;
    }

    public static void configure(@NotNull WebDriver webDriver, @NotNull Path pathToDownload) {
        Configuration.pathToDownload = pathToDownload;
        Configuration.webDriver = webDriver;
    }

    public static boolean isRemote() {
        return RemoteWebDriver.class.isAssignableFrom(getWebDriver().getClass());
    }

    public static @NotNull Path getPathToDownload() {
        if (pathToDownload == null)
            throw new NullPointerException("Путь не был инициализирован.");

        return pathToDownload;
    }

    public static @NotNull String getRemoteAddress() {
        if (remoteAddress == null)
            throw new NullPointerException("Путь не был инициализирован.");

        return remoteAddress;
    }

    public static @NotNull WebDriver getWebDriver() {
        if (webDriver == null)
            throw new NullPointerException("Веб-драйвер не был инициализирован.");

        return webDriver;
    }
}
