package org.untitled.phoenix.component;

import org.jetbrains.annotations.Unmodifiable;
import org.jetbrains.annotations.UnmodifiableView;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;

import org.openqa.selenium.remote.RemoteWebDriver;
import org.untitled.phoenix.exception.UnavailableComponentException;
import org.untitled.phoenix.exception.ComponentActionException;
import org.untitled.phoenix.configuration.Configuration;

import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.time.Duration;
import java.util.regex.Pattern;

import static java.lang.System.currentTimeMillis;

public class Action {

    private final static @NotNull String REGEX_HTTP = "http://.*:\\d*/";

    private final static @NotNull String REGEX_HREF = "(?<=href=\").*(?=\")";

    private final @NotNull Component component;

    public Action(@NotNull Component component) {
        this.component = component;
    }

    public @NotNull String getAttribute(@NotNull String attributeName) {
        return invoke(webElement -> { final var attribute = webElement.getAttribute(attributeName); return attribute == null ? "" : attribute; }, String.format("Не удалось получить значение атрибута '%s' компонента", attributeName), component.getTimeout());
    }

    public @NotNull String getCssValue(@NotNull String propertyName) {
        return invoke(webElement -> { final var attribute = webElement.getCssValue(propertyName); return attribute == null ? "" : attribute; }, String.format("Не удалось получить значение '%s' компонента", propertyName), component.getTimeout());
    }

    public @NotNull String getCssClass() {
        return invoke(webElement -> { final var attribute = webElement.getAttribute("class"); return attribute == null ? "" : attribute; }, "Не удалось получить значение класса", component.getTimeout());
    }

    public @NotNull String getValue() {
        return invoke(webElement -> { final var attribute = webElement.getAttribute("value"); return attribute == null ? "" : attribute; }, "Не удалось получить значение компонента", component.getTimeout());
    }

    public @NotNull String getText() {
        return invoke(webElement -> { final var text = webElement.getText(); return text == null ? "" : text; }, "Не удалось получить текст компонента", component.getTimeout());
    }

    public void sendKeys(@NotNull CharSequence... keys) {
        invoke((Consumer<WebElement>) webElement -> webElement.sendKeys(keys), String.format("Не удалось отправить нажатие клавиш '%s' компоненту", Arrays.toString(keys)), component.getTimeout());
    }

    public void setValue(String value) {
        invoke(webElement -> { webElement.clear(); webElement.sendKeys(value); }, String.format("Не удалось задать значение '%s' компоненту", value), component.getTimeout());
    }

    /**
     * <p>Наводится мышкой на компонент, если это возможно.</p>
     */
    public void hover() {
        invoke((Consumer<WebElement>) webElement -> new Actions(Configuration.getWebDriver()).moveToElement(component.toWebElement()).build().perform(), "Не удалось навести курсор мыши на компонент", component.getTimeout());
    }

    /**
     * <p>Нажимает левой кнопкой мыши на компонент, если это возможно.</p>
     */
    public void click() {
        invoke(WebElement::click, "Не удалось нажать левой кнопкой мыши на компонент", component.getTimeout());
    }

    /**
     * Если компонент является элементом ввода, то этот метод отчистит его значение.
     */
    public void clear() {
        invoke(WebElement::clear, "Не удалось очистить компонент", component.getTimeout());
    }

    /**
     * <p>Нажимает левой кнопкой мыши на компонент, после чего ожидает, что начнётся загрузка, и будут скачено указанное количество файлов за указанное время ожидания.</p><br>
     * <p><b>При использовании локального {@link WebDriver} скачивание осуществляется динамически</b>: если файлы загрузятся раньше указанного времени ожидания, то оставшееся время будет проигнорировано.</p>
     * @param timeout время ожидания загрузки
     * @param countFiles ожидаемое количество загруженных файлов
     * @return список загруженных файлов
     */
    public @NotNull @Unmodifiable List<File> download(@NotNull Duration timeout, int countFiles) {
        if (Configuration.isRemote()) {
            try {
                return remoteDownload(timeout, countFiles);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        else return localDownload(timeout, countFiles, true);
    }

    /**
     * <nobr>Определяет, виден ли компонент или нет.</nobr>
     * @return
     * <p><b>true</b> - если компонент виден</p>
     * <p><b>false</b> - если компонент невиден</p>
     */
    public boolean isDisplayed() {
        return invoke(WebElement::isDisplayed, "Не удалось установить отображается ли компонент", component.getTimeout());
    }

    /**
     * <nobr>Определяет, являет ли компонент только для чтения или нет.</nobr>
     * @return
     * <p><b>true</b> - если компонент выделен</p>
     * <p><b>false</b> - если компонент не выделен</p>
     */
    public boolean isReadonly() {
        return invoke(webElement -> !Objects.equals(webElement.getAttribute("readonly"), null), "Не удалось установить является ли компонент только для чтения", component.getTimeout());
    }

    /**
     * <p>Определяет, выделен ли компонент или нет.</p><br>
     * <p>Этот метод поддерживается только в том случае, если компонент представляет собой один из следующих веб-элементов: <b>input</b>, <b>checkbox</b>, <b>options</b> в <b>select</b> и <b>radiobutton</b>.</p>
     * @return
     * <p><b>true</b> - если компонент выделен</p>
     * <p><b>false</b> - если компонент не выделен</p>
     */
    public boolean isSelected() {
        return invoke(WebElement::isSelected, "Не удалось установить выделен ли компонент", component.getTimeout());
    }

    /**
     * <nobr>Определяет, включен компонент или выключен.</nobr>
     * @return
     * <p><b>true</b> - если компонент включён</p>
     * <p><b>false</b> - если компонент выключен</p>
     */
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

    private @NotNull @Unmodifiable List<File> remoteDownload(@NotNull Duration timeout, int countFiles) throws IOException {

        invoke(WebElement::click, "Не удалось нажать левой кнопкой мыши на компонент", component.getTimeout());

        final var startTime = currentTimeMillis();

        while (true) {

            if (currentTimeMillis() - startTime <= timeout.toMillis())
                continue;

            final var pageContent = getPageContent(new URL(getDownloadUrl())).replace("</a>", "</a>\n");
            final var patter = Pattern.compile(REGEX_HREF);
            final var matcher = patter.matcher(pageContent);
            final var downloadedFiles = new ArrayList<File>();
            final var namesDownloadedFiles = new ArrayList<String>();

            while (matcher.find())
                namesDownloadedFiles.add(matcher.group());

            if (namesDownloadedFiles.size() < countFiles)
                throw new RuntimeException();

            for (var name : namesDownloadedFiles) {
                InputStream in = new URL(String.format("%s/%s", getDownloadUrl(), name)).openStream();
                Files.copy(in, Paths.get(Configuration.getDownloadDirectory(), name), StandardCopyOption.REPLACE_EXISTING);
                downloadedFiles.add(Paths.get(Configuration.getDownloadDirectory(), name).toFile());
            }

            for (var name : namesDownloadedFiles) {
                HttpURLConnection httpCon = (HttpURLConnection) new URL(String.format("%s/%s", getDownloadUrl(), name)).openConnection();
                httpCon.setRequestMethod("DELETE");
                httpCon.getResponseCode();
            }

            if (downloadedFiles.size() != countFiles)
                throw new RuntimeException();

            return Collections.unmodifiableList(downloadedFiles);
        }
    }

    private static boolean downloadCompleted(@NotNull File[] filesBefore) {
        final var lastChangesBefore = Arrays.stream(filesBefore).map(File::lastModified).mapToLong(value -> value).toArray();
        final var lastChangesAfter = Arrays.stream(getCurrentFiles()).map(File::lastModified).mapToLong(value -> value).toArray();

        for (final long currentValue : lastChangesAfter)
            if (Arrays.stream(lastChangesBefore).noneMatch(value -> value == currentValue))
                return false;

        return true;
    }

    private static File @NotNull [] getCurrentFiles() {
        final var path = Configuration.getDownloadDirectory();
        final var files = new File(Configuration.getDownloadDirectory()).listFiles();
        return files == null ? new File[0] : files;
    }

    private @NotNull @UnmodifiableView List<File> localDownload(@NotNull Duration timeout, int countFiles, boolean isAction) {
        final var downloadedFiles = new ArrayList<File>();
        final var filesBefore = getCurrentFiles();

        if (isAction)
            invoke(WebElement::click, "Не удалось нажать левой кнопкой мыши на компонент", component.getTimeout());

        final var startTime = currentTimeMillis();

        if (timeout.toMillis() <= 0)
            throw new RuntimeException("GG");

        while (true)
            if (downloadCompleted(getCurrentFiles()))
                if (currentTimeMillis() - startTime >= 1_000)
                    break;

        for (var fileAfter : getCurrentFiles())
            if (Arrays.stream(filesBefore).noneMatch(fileBefore -> fileBefore.equals(fileAfter)))
                downloadedFiles.add(fileAfter);

        if (downloadedFiles.size() < countFiles)
            downloadedFiles.addAll(localDownload(Duration.ofMillis(timeout.toMillis() - (currentTimeMillis() - startTime)), countFiles - downloadedFiles.size(), false));

        if (downloadedFiles.size() != countFiles)
            throw new RuntimeException();

        return Collections.unmodifiableList(downloadedFiles);
    }

    private static @NotNull String getDownloadUrl() {

        if (Configuration.isRemote()) {
            final var patter = Pattern.compile(REGEX_HTTP);
            final var remoteAddress = Configuration.getRemoteAddress();

            if (remoteAddress == null)
                throw new RuntimeException();

            final var matcher = patter.matcher(remoteAddress.toString());

            if (!matcher.find())
                throw new RuntimeException();

            return String.format("%s/download/%s", matcher.group(),((RemoteWebDriver) Configuration.getWebDriver()).getSessionId());
        }

        throw new RuntimeException();
    }
}
