package org.untitled.phoenix.component;

import org.untitled.phoenix.configuration.Configuration;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.untitled.phoenix.exception.ComponentActionException;
import org.untitled.phoenix.exception.UnavailableComponentException;

import java.util.function.Consumer;
import java.util.function.Function;
import java.time.Duration;
import java.util.Objects;
import java.util.Arrays;

public final class Action {

    private final @NotNull Component component;

    public Action(@NotNull Component component) {
        this.component = component;
    }

    public @Nullable String getAttribute(@NotNull String attributeName) {
        return invoke((Function<WebElement, String>) webElement -> webElement.getAttribute(attributeName), String.format("Не удалось получить значение атрибута '%s' компонента", attributeName), component.getTimeout());
    }

    public @Nullable String getCssId() {
        return invoke((Function<WebElement, String>) webElement -> webElement.getAttribute("id"), "Не удалось получить значение идентификатора", component.getTimeout());
    }

    public @Nullable String getCssClass() {
        return invoke((Function<WebElement, String>) webElement -> webElement.getAttribute("class"), "Не удалось получить значение класса", component.getTimeout());
    }

    public @Nullable String getValue() {
        return invoke((Function<WebElement, String>) webElement -> webElement.getAttribute("value"), "Не удалось значение компонента", component.getTimeout());
    }

    public void hover() {
        final var action = new Actions(Configuration.getWebDriver());
        final var startTime = System.currentTimeMillis();

        while (true) {
            try {
                action.moveToElement(component.toWebElement()).build().perform();
                return;
            } catch (Exception exception) {
                if (System.currentTimeMillis() - startTime >= component.getTimeout().toMillis())
                    throw new RuntimeException(String.format("%s :: Не удалось навести курсор мыши на компонент на протяжении %d миллисекунд.", component, component.getTimeout().toMillis()));
            }
        }
    }

    public void click() {
        invoke(WebElement::click, "Не удалось нажать левой кнопкой мыши на компонент", component.getTimeout());
    }

    public @Nullable String getText() {
        return invoke(WebElement::getText, "Не удалось получить текст компонента", component.getTimeout());
    }

    public void setValue(String value) {
        final var startTime = System.currentTimeMillis();

        while (true) {
            try {
                final var element = component.toWebElement();
                element.clear();
                element.sendKeys(value);
                return;
            } catch (Exception exception) {
                if (System.currentTimeMillis() - startTime >= component.getTimeout().toMillis())
                    throw new RuntimeException(String.format("%s :: Не удалось задать значение '%s' компоненту на протяжении %d миллисекунд.", component, value, component.getTimeout().toMillis()));
            }
        }
    }

    public void sendKeys(CharSequence @NotNull ... keys) {
        invoke((Consumer<WebElement>) webElement -> webElement.sendKeys(keys), String.format("Не удалось отправить нажатие клавиш '%s' компоненту", Arrays.toString(keys)), component.getTimeout());
    }

    public void clear() {
        invoke(WebElement::clear, "Не удалось отчистить компонент", component.getTimeout());
    }

    public boolean isDisplayed() {
        return invoke(WebElement::isDisplayed, "Не удалось установить отображается ли компонент", component.getTimeout());
    }

    public boolean isSelected() {
        return invoke(WebElement::isSelected, "Не удалось установить выделен ли компонент", component.getTimeout());
    }

    public boolean isEnabled() {
        return invoke(WebElement::isEnabled, "Не удалось установить включен ли компонент", component.getTimeout());
    }

    public boolean isReadonly() {
        return invoke(webElement -> !Objects.equals(webElement.getAttribute("readonly"), null), "Не удалось установить является ли компонент только для чтения", component.getTimeout());
    }

    private void invoke(@NotNull Consumer<@NotNull WebElement> action, @NotNull String message, @NotNull Duration timeout) {
        final var startTime = System.currentTimeMillis();

        while (true) {
            try {
                action.accept(component.toWebElement());
                return;
            } catch (UnavailableComponentException exception) {
                if (System.currentTimeMillis() - startTime >= timeout.toMillis())
                    throw exception;
            } catch (Exception ignore) {
                if (System.currentTimeMillis() - startTime >= timeout.toMillis())
                    throw new ComponentActionException(component, message, timeout);
            }
        }
    }

    private <TValue> TValue invoke(@NotNull Function<@NotNull WebElement, TValue> action, @NotNull String message, @NotNull Duration timeout) {
        final var startTime = System.currentTimeMillis();

        while (true) {
            try {
                return action.apply(component.toWebElement());
            } catch (UnavailableComponentException exception) {
                if (System.currentTimeMillis() - startTime >= timeout.toMillis())
                    throw exception;
            } catch (Exception ignore) {
                if (System.currentTimeMillis() - startTime >= timeout.toMillis())
                    throw new ComponentActionException(component, message, timeout);
            }
        }
    }
}
