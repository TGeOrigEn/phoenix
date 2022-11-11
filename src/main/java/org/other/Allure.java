package org.other;

import io.qameta.allure.Attachment;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.untitled.phoenix.configuration.Configuration;

import java.util.regex.Pattern;

public final class Allure {

    private final static @NotNull String REGEX_HTTP = "http://.*:\\d*/";

    private Allure() {
    }

    @Attachment(value = "Видео", type = "text/html", fileExtension = ".html")
    public static @NotNull String attachVideo() {
        final  var s = getVideoAddress();
        return "<html><body><video width='100%' height='100%' controls autoplay><source src='"
                + s
                + "' type='video/mp4'></video></body></html>";
    }

    @Attachment(value = "{screenshotName}", type = "image/png")
    public static byte @NotNull [] attachScreenshot(@NotNull String screenshotName) {
        return ((TakesScreenshot) Configuration.getWebDriver()).getScreenshotAs(OutputType.BYTES);
    }

    private static @NotNull String getVideoAddress() {
        final var remoteAddress = Configuration.getRemoteAddress();
        final var pattern = Pattern.compile(REGEX_HTTP);

        if (remoteAddress == null)
            throw new RuntimeException();

        final var matcher = pattern.matcher(remoteAddress.toString());

        if (!matcher.find())
            throw new RuntimeException();

        final var s = matcher.group();

        final var f = ((RemoteWebDriver) Configuration.getWebDriver()).getSessionId();

        return String.format("%svideo/%s.mp4", s, f);
    }
}
