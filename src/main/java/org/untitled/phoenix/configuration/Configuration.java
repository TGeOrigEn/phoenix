package org.untitled.phoenix.configuration;

import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;

import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.WebDriver;

import java.net.MalformedURLException;
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

    public static void setChromeDriver(@Nullable String remoteAddress, @NotNull String downloadDirectory, @NotNull ChromeOptions options) throws MalformedURLException {
        Configuration.downloadDirectory = String.format("%s\\%s", downloadDirectory, UUID.randomUUID());

        final var prefs = new HashMap<String, Object>();
        prefs.put("download.default_directory", Configuration.downloadDirectory);
        prefs.put("download.prompt_for_download", false);
        prefs.put("download.directory_upgrade", true);
        prefs.put("profile.default_content_setting_values.automatic_downloads", 2);
        prefs.put("profile.content_settings.exceptions.automatic_downloads.*.setting", 1 );
        options.setExperimentalOption("prefs", prefs);

        if (remoteAddress == null) {
            Configuration.webDriver = new ChromeDriver(options);
            Configuration.remoteAddress = null;
        } else {
            final var address = new URL(remoteAddress);
            final var remote = new RemoteWebDriver(address, options);
            remote.setFileDetector(new LocalFileDetector());
            Configuration.remoteAddress = address;
            Configuration.webDriver = remote;
        }

        final var directory = new File(getDownloadDirectory());

        if (!directory.mkdirs() || !directory.exists())
            throw new RuntimeException("Не удалось создать директорию для загруженных файлов");
    }
}
