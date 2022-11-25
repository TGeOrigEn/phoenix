package org.untitled.phoenix.configuration;

import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;

import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.nio.file.Path;

public final class Configuration {

    private static @Nullable String remoteAddress;

    private static @Nullable Path pathToDownload;

    private static @Nullable WebDriver webDriver;

    public static boolean isRemote() {
        return getWebDriver().getClass().equals(RemoteWebDriver.class);
    }

    public static void configure(@NotNull RemoteWebDriver webDriver, @NotNull String remoteAddress, @NotNull Path pathToDownload) {
        Configuration.pathToDownload = pathToDownload;
        Configuration.remoteAddress = remoteAddress;
        Configuration.webDriver = webDriver;

        webDriver.setFileDetector(new LocalFileDetector());

        createDownloadFolder(pathToDownload.toFile());
    }

    public static void configure(@NotNull WebDriver webDriver, @NotNull Path pathToDownload) {
        Configuration.pathToDownload = pathToDownload;
        Configuration.webDriver = webDriver;
        Configuration.remoteAddress = null;

        createDownloadFolder(pathToDownload.toFile());
    }

    public static @NotNull String getRemoteAddress() {
        if (remoteAddress == null)
            throw new NullPointerException("Путь не был инициализирован.");

        return remoteAddress;
    }

    public static @NotNull Path getPathToDownload() {
        if (pathToDownload == null)
            throw new NullPointerException("Путь не был инициализирован.");

        return pathToDownload;
    }

    public static @NotNull WebDriver getWebDriver() {
        if (webDriver == null)
            throw new NullPointerException("Веб-драйвер не был инициализирован.");

        return webDriver;
    }

    private static void createDownloadFolder(@NotNull File folder) {
        if (folder.isFile()) throw new RuntimeException("Папка загрузок не может являться файлом.");
        if (!folder.exists() && !folder.mkdirs()) throw new RuntimeException("Не удалось создать папку для загрузок.");
    }
}
