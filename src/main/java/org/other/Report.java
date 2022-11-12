package org.other;

import io.qameta.allure.Attachment;
import io.qameta.allure.Step;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.untitled.phoenix.component.Component;
import org.untitled.phoenix.configuration.Configuration;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public final class Report {

    public static class ErrorScreenshot {

        private final @Nullable Point location;

        private final @Nullable Dimension size;

        private final byte @NotNull [] bytes;

        private final @NotNull String name;

        private final long milliseconds;

        public ErrorScreenshot(@NotNull String name, @NotNull Component component) {
            final var element = component.toWebElement();

            this.bytes = (((TakesScreenshot) Configuration.getWebDriver()).getScreenshotAs(OutputType.BYTES));
            milliseconds = System.currentTimeMillis();
            this.location = element.getLocation();
            this.size = element.getSize();
            this.name = name;
        }

        public ErrorScreenshot(@NotNull String name) {
            this.bytes = (((TakesScreenshot) Configuration.getWebDriver()).getScreenshotAs(OutputType.BYTES));
            milliseconds = System.currentTimeMillis();
            this.location = null;
            this.size = null;
            this.name = name;
        }
    }

    public static class ComponentScreenshot {

        private final @NotNull Point location;

        private final @NotNull Dimension size;

        private final byte @NotNull [] bytes;

        private final @NotNull String name;

        private final long milliseconds;

        public ComponentScreenshot(@NotNull String name, @NotNull Component component) {
            final var element = component.toWebElement();

            this.bytes = (((TakesScreenshot) Configuration.getWebDriver()).getScreenshotAs(OutputType.BYTES));
            milliseconds = System.currentTimeMillis();
            this.location = element.getLocation();
            this.size = element.getSize();
            this.name = name;
        }
    }

    private static final @NotNull List<ComponentScreenshot> components = new ArrayList<>();

    private static final @NotNull List<ErrorScreenshot> errors = new ArrayList<>();

    private static long milliseconds;

    public static void addStep(@NotNull ComponentScreenshot screenshot) {
        components.add(screenshot);
    }

    public static void addError(@NotNull ErrorScreenshot screenshot) {
        errors.add(screenshot);
    }

    public static void setStartTime(long milliseconds) {
        Report.milliseconds = milliseconds;
    }

    public static void clear() {
        components.clear();
        errors.clear();
    }

    @Step("Отчёт")
    public static void perform() throws IOException {
        if (!errors.isEmpty()) computeErrors();
        if (!components.isEmpty()) computeComponents();
        if (Configuration.isRemote()) attachVideo();
    }

    @Step("Ошибки")
    private static void computeErrors() throws IOException {
        for (var error : errors)
            attachErrorScreenshot(error, String.format("%s [%s]", error.name, getTime(error.milliseconds)));
    }

    @Step("Шаги")
    private static void computeComponents() throws IOException {
        for (var component : components)
            attachComponentScreenshot(component, String.format("%s [%s]", component.name, getTime(component.milliseconds)));
    }

    @Attachment(value = "{name}", type = "image/png")
    private static byte @NotNull [] attachErrorScreenshot(@NotNull ErrorScreenshot error, @NotNull String name) throws IOException {
        if (error.location != null && error.size != null) {
            final var screenshot = ImageIO.read(new ByteArrayInputStream(error.bytes));
            final var graphics = screenshot.createGraphics();

            graphics.setColor(Color.RED);
            graphics.setStroke(new BasicStroke(2));
            graphics.drawRect(error.location.x, error.location.y, error.size.width, error.size.height);

            final var outputStream = new ByteArrayOutputStream();
            ImageIO.write(screenshot, "png", outputStream);

            return outputStream.toByteArray();
        } else {
            return error.bytes;
        }
    }

    @Attachment(value = "{name}", type = "image/png")
    private static byte @NotNull [] attachComponentScreenshot(@NotNull ComponentScreenshot component, @NotNull String name) throws IOException {
        final var screenshot = ImageIO.read(new ByteArrayInputStream(component.bytes));
        final var graphics = screenshot.createGraphics();

        graphics.setColor(Color.RED);
        graphics.setStroke(new BasicStroke(2));
        graphics.drawRect(component.location.x, component.location.y, component.size.width, component.size.height);

        final var outputStream = new ByteArrayOutputStream();
        ImageIO.write(screenshot, "png", outputStream);

        return outputStream.toByteArray();
    }

    @Attachment(value = "Видео", type = "text/html", fileExtension = ".html")
    public static @NotNull String attachVideo() {
        final  var s = getVideoAddress();
        return "<html><body><video width='100%' height='100%' controls autoplay><source src='"
                + s
                + "' type='video/mp4'></video></body></html>";
    }

    private static @NotNull String getVideoAddress() {
        final var remoteAddress = Configuration.getRemoteAddress();
        final var pattern = Pattern.compile("http://.*:\\d*/");

        if (remoteAddress == null)
            throw new RuntimeException();

        final var matcher = pattern.matcher(remoteAddress.toString());

        if (!matcher.find())
            throw new RuntimeException();

        final var s = matcher.group();

        final var f = ((RemoteWebDriver) Configuration.getWebDriver()).getSessionId();

        return String.format("%svideo/%s.mp4", s, f);
    }

    public static @NotNull String getTime(long milliseconds) {
        final long seconds = milliseconds - Report.milliseconds;
        return String.format("%d:%02d:%02d", seconds / 3600, (seconds % 3600) / 60, seconds % 60);
    }
}
