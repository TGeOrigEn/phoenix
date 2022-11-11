package org.other;

import io.qameta.allure.Attachment;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.untitled.phoenix.configuration.Configuration;

import java.util.regex.Pattern;

public final class Allure {

    private final static @NotNull String REGEX_HTTP = "http://.*:\\d*/";

    private Allure() {
    }

    @Attachment(value = "Видео", type = "text/html", fileExtension = ".html")
    public static @NotNull String attachVideo() {
        return "<html><body><video width='100%' height='100%' controls autoplay><source src='"
                + getVideoAddress()
                + "' type='video/mp4'></video></body></html>";
    }

    private static @NotNull String getVideoAddress() {
        final var remoteAddress = Configuration.getRemoteAddress();
        final var pattern = Pattern.compile(REGEX_HTTP);

        if (remoteAddress == null)
            throw new RuntimeException();

        final var matcher = pattern.matcher(remoteAddress.toString());

        if (!matcher.find())
            throw new RuntimeException();

        return String.format("%s/video/%s.mp4", matcher.group(),((RemoteWebDriver) Configuration.getWebDriver()).getSessionId());
    }
}
