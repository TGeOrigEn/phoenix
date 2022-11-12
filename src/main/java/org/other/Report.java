package org.other;

import io.qameta.allure.Attachment;
import io.qameta.allure.Step;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.untitled.phoenix.component.Component;
import org.untitled.phoenix.configuration.Configuration;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class Report {

    public static class ErrorScreenshot {

        private final @Nullable Point location;

        private final @Nullable Dimension size;

        private final byte @NotNull [] bytes;

        private final @NotNull String name;

        public ErrorScreenshot(@NotNull String name, @NotNull Component component) {
            final var element = component.toWebElement();

            this.bytes = (((TakesScreenshot) Configuration.getWebDriver()).getScreenshotAs(OutputType.BYTES));
            this.location = element.getLocation();
            this.size = element.getSize();
            this.name = name;
        }

        public ErrorScreenshot(@NotNull String name) {
            this.bytes = (((TakesScreenshot) Configuration.getWebDriver()).getScreenshotAs(OutputType.BYTES));
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

        public ComponentScreenshot(@NotNull String name, @NotNull Component component) {
            final var element = component.toWebElement();

            this.bytes = (((TakesScreenshot) Configuration.getWebDriver()).getScreenshotAs(OutputType.BYTES));
            this.location = element.getLocation();
            this.size = element.getSize();
            this.name = name;
        }
    }

    private static final @NotNull List<ComponentScreenshot> components = new ArrayList<>();

    private static final @NotNull List<ErrorScreenshot> errors = new ArrayList<>();

    public static void addStep(@NotNull ComponentScreenshot screenshot) {
        components.add(screenshot);
    }

    public static void addError(@NotNull ErrorScreenshot screenshot) {
        errors.add(screenshot);
    }

    public static void clear() {
        components.clear();
        errors.clear();
    }

    @Step("Отчёт")
    public static void perform() throws IOException {
        if (!errors.isEmpty()) computeErrors();
        if (!components.isEmpty()) computeComponents();
        if (Configuration.isRemote()) Allure.attachVideo();
    }

    @Step("Ошибки")
    private static void computeErrors() throws IOException {
        for (var error : errors)
            attachErrorScreenshot(error);
    }

    @Step("Шаги")
    private static void computeComponents() throws IOException {
        for (var component : components)
            attachComponentScreenshot(component);
    }

    @Attachment(value = "{error.name}", type = "image/png")
    private static byte @NotNull [] attachErrorScreenshot(@NotNull ErrorScreenshot error) throws IOException {
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

    @Attachment(value = "{component.name}", type = "image/png")
    private static byte @NotNull [] attachComponentScreenshot(@NotNull ComponentScreenshot component) throws IOException {
        final var screenshot = ImageIO.read(new ByteArrayInputStream(component.bytes));
        final var graphics = screenshot.createGraphics();

        graphics.setColor(Color.RED);
        graphics.setStroke(new BasicStroke(2));
        graphics.drawRect(component.location.x, component.location.y, component.size.width, component.size.height);

        final var outputStream = new ByteArrayOutputStream();
        ImageIO.write(screenshot, "png", outputStream);

        return outputStream.toByteArray();
    }
}
