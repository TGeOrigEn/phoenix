package org.other;

import io.qameta.allure.Attachment;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.untitled.phoenix.component.Component;
import org.untitled.phoenix.configuration.Configuration;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.*;
import java.util.regex.Pattern;

public final class Allure {

    private final static @NotNull String REGEX_HTTP = "";

    private Allure() {

    }

    @Attachment(value = "{screenshotName}", type = "image/png")
    public static byte @NotNull [] attachScreenshot(@NotNull String screenshotName) {
        return ((TakesScreenshot) Configuration.getWebDriver()).getScreenshotAs(OutputType.BYTES);
    }

    public static void attachFile(@NotNull File file, @NotNull String fileName) throws FileNotFoundException {
        io.qameta.allure.Allure.attachment(fileName, new FileInputStream(file));
    }
}
