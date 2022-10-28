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
        final var startTime = System.currentTimeMillis();

        while (true) {
            try {
                action.accept(component.toWebElement());
                return;
            } catch (InvalidElementStateException | StaleElementReferenceException ignore) {
                if (System.currentTimeMillis() - startTime >= timeout.toMillis())
                    throw new ComponentActionException(component, message, timeout);
            } catch (UnavailableComponentException | WebDriverException exception) {
                if (System.currentTimeMillis() - startTime >= timeout.toMillis())
                    throw exception;
            }
        }
    }

    private <TValue> TValue invoke(@NotNull Function<@NotNull WebElement, TValue> action, @NotNull String message, @NotNull Duration timeout) {
        final var startTime = System.currentTimeMillis();

        while (true) {
            try {
                return action.apply(component.toWebElement());
            } catch (InvalidElementStateException | StaleElementReferenceException ignore) {
                if (System.currentTimeMillis() - startTime >= timeout.toMillis())
                    throw new ComponentActionException(component, message, timeout);
            } catch (UnavailableComponentException | WebDriverException exception) {
                if (System.currentTimeMillis() - startTime >= timeout.toMillis())
                    throw exception;
            }
        }
    }

    public static @NotNull String getPageContent(@NotNull URL url) {
        try {
            final var openConnection = url.openConnection();

            final var bufferedReader = new BufferedReader(new InputStreamReader(openConnection.getInputStream()));
            final var stringBuffer = new StringBuilder();

            String inputLine;
            while ((inputLine = bufferedReader.readLine()) != null)
                stringBuffer.append(inputLine);

            bufferedReader.close();

            return stringBuffer.toString();
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    public @NotNull File[] download(Duration timeout) {
        final var startTime = System.currentTimeMillis();
        final var filesBefore = getCurrentFiles();
        final var files = new ArrayList<File>();

        click();

        while (true)
            if (downloadCompleted(getCurrentFiles())) {
                if (System.currentTimeMillis() - startTime >= timeout.toMillis())
                    break;
            } else
                break;

        while (true)
            if (downloadCompleted(getCurrentFiles()))
                break;

        final var filesAfter = getCurrentFiles();

        for (var i = 0; i < filesAfter.length; i++) {
            final var index = i;
            if (Arrays.stream(filesBefore).noneMatch(file -> file.equals(filesAfter[index])))
                files.add(filesAfter[index]);
        }

        return files.toArray(File[]::new);
    }

    private static boolean downloadCompleted(@NotNull File[] filesBefore) {
        final var lastChangesBefore = Arrays.stream(filesBefore).map(File::lastModified).mapToLong(value -> value).toArray();
        final var lastChangesAfter = Arrays.stream(getCurrentFiles()).map(File::lastModified).mapToLong(value -> value).toArray();

        if (lastChangesBefore.length < lastChangesAfter.length)
            return false;

        for (var i = 0; i < lastChangesAfter.length; i++) {
            final var index = i;
            if (Arrays.stream(lastChangesBefore).noneMatch(value -> value == lastChangesAfter[index]))
                return false;
        }

        return true;
    }

    private static File @NotNull [] getCurrentFiles() {
        final var files = new File(Configuration.getDownloadDirectory()).listFiles();
        return files == null ? new File[0] : files;
    }
}
