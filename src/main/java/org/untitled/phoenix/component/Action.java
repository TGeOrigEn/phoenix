package org.untitled.phoenix.component;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;

import org.untitled.phoenix.exception.UnavailableComponentException;
import org.untitled.phoenix.exception.ComponentActionException;
import org.untitled.phoenix.configuration.Configuration;

import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.function.Function;
import java.time.Duration;
import java.util.Objects;
import java.util.Arrays;

import static java.lang.System.currentTimeMillis;
import static java.lang.Thread.sleep;

public class Action {

    private final @NotNull Component component;

    public Action(@NotNull Component component) {
        this.component = component;
    }

    public @Nullable String getAttribute(@NotNull String attributeName) {
        return invoke((Function<WebElement, String>) webElement -> webElement.getAttribute(attributeName), String.format("Не удалось получить значение атрибута '%s' компонента", attributeName), component.getTimeout());
    }

    public @Nullable String getValue() {
        return invoke((Function<WebElement, String>) webElement -> webElement.getAttribute("value"), "Не удалось значение компонента", component.getTimeout());
    }

    public @Nullable String getText() {
        return invoke(WebElement::getText, "Не удалось получить текст компонента", component.getTimeout());
    }

    public void sendKeys(@NotNull CharSequence... keys) {
        invoke((Consumer<WebElement>) webElement -> webElement.sendKeys(keys), String.format("Не удалось отправить нажатие клавиш '%s' компоненту", Arrays.toString(keys)), component.getTimeout());
    }

    public void setValue(String value) {
        invoke(webElement -> { webElement.clear(); webElement.sendKeys(value); }, String.format("Не удалось задать значение '%s' компоненту", value), component.getTimeout());
    }

    public void hover() {
        invoke((Consumer<WebElement>) webElement -> new Actions(Configuration.getWebDriver()).moveToElement(component.toWebElement()).build().perform(), "Не удалось навести курсор мыши на компонент", component.getTimeout());
    }

    public void click() {
        invoke(WebElement::click, "Не удалось нажать левой кнопкой мыши на компонент", component.getTimeout());
    }

    public void clear() {
        invoke(WebElement::clear, "Не удалось отчистить компонент", component.getTimeout());
    }

    public boolean isDisplayed() {
        return invoke(WebElement::isDisplayed, "Не удалось установить отображается ли компонент", component.getTimeout());
    }

    public boolean isReadonly() {
        return invoke(webElement -> !Objects.equals(webElement.getAttribute("readonly"), null), "Не удалось установить является ли компонент только для чтения", component.getTimeout());
    }

    public boolean isSelected() {
        return invoke(WebElement::isSelected, "Не удалось установить выделен ли компонент", component.getTimeout());
    }

    public boolean isEnabled() {
        return invoke(WebElement::isEnabled, "Не удалось установить включен ли компонент", component.getTimeout());
    }

    private void invoke(@NotNull Consumer<@NotNull WebElement> action, @NotNull String message, @NotNull Duration timeout) {
        final var startTime = currentTimeMillis();

        while (true) {
            try {
                action.accept(component.toWebElement());
                return;
            } catch (InvalidElementStateException | StaleElementReferenceException ignore) {
                if (currentTimeMillis() - startTime >= timeout.toMillis())
                    throw new ComponentActionException(component, message, timeout);
            } catch (UnavailableComponentException | WebDriverException exception) {
                if (currentTimeMillis() - startTime >= timeout.toMillis())
                    throw exception;
            }
        }
    }

    private <TValue> TValue invoke(@NotNull Function<@NotNull WebElement, TValue> action, @NotNull String message, @NotNull Duration timeout) {
        final var startTime = currentTimeMillis();

        while (true) {
            try {
                return action.apply(component.toWebElement());
            } catch (InvalidElementStateException | StaleElementReferenceException ignore) {
                if (currentTimeMillis() - startTime >= timeout.toMillis())
                    throw new ComponentActionException(component, message, timeout);
            } catch (UnavailableComponentException | WebDriverException exception) {
                if (currentTimeMillis() - startTime >= timeout.toMillis())
                    throw exception;
            }
        }
    }

    public static @NotNull String getPageContent(@NotNull URL address) throws IOException {
        final var bufferedReader = new BufferedReader(new InputStreamReader(address.openConnection().getInputStream()));
        final var stringBuilder = new StringBuilder();

        while (true) {
            final var readLine = bufferedReader.readLine();

            if (readLine == null) {
                bufferedReader.close();
                break;
            }

            stringBuilder.append(readLine);
        }

        return stringBuilder.toString();
    }

    public @NotNull File[] download(Duration timeout) {
        final var downloadedFiles = new ArrayList<File>();
        final var filesBefore = getCurrentFiles();

        invoke(WebElement::click, "Не удалось нажать левой кнопкой мыши на компонент", component.getTimeout());

        final var startTime = currentTimeMillis();

        while (true)
            if (downloadCompleted(getCurrentFiles()))
                if (currentTimeMillis() - startTime >= timeout.toMillis())
                    break;

        for (var fileAfter : getCurrentFiles())
            if (Arrays.stream(filesBefore).noneMatch(fileBefore -> fileBefore.equals(fileAfter)))
                downloadedFiles.add(fileAfter);

        return downloadedFiles.toArray(File[]::new);
    }

    private static boolean downloadCompleted(@NotNull File[] filesBefore) {
        final var lastChangesBefore = Arrays.stream(filesBefore).map(File::lastModified).mapToLong(value -> value);
        final var lastChangesAfter = Arrays.stream(getCurrentFiles()).map(File::lastModified).mapToLong(value -> value).toArray();

        for (final long currentValue : lastChangesAfter)
            if (lastChangesBefore.noneMatch(value -> value == currentValue))
                return false;

        return true;
    }

    private static File @NotNull [] getCurrentFiles() {
        final var files = new File(Configuration.getDownloadDirectory()).listFiles();
        return files == null ? new File[0] : files;
    }
}
