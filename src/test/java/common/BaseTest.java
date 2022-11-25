package common;

import org.untitled.phoenix.configuration.Configuration;
import common.drivers.*;
import org.other.Report;

import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;

import org.junit.jupiter.api.Assertions;

import java.net.MalformedURLException;
import java.io.IOException;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public abstract class BaseTest {

    protected static final class Date {

        private static final @NotNull DateTimeFormatter MM_DD_YYYY_SLASH = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        private static final @NotNull DateTimeFormatter YYYY_MM_DD_SLASH = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        private static final @NotNull DateTimeFormatter DD_MM_YYYY_DOT = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        public enum Format { MM_DD_YYYY_SLASH, YYYY_MM_DD_SLASH, DD_MM_YYYY_DOT }

        public static @NotNull String getDate(@NotNull LocalDateTime dateTime, @NotNull Format format) {
            switch (format){
                case DD_MM_YYYY_DOT: return dateTime.format(DD_MM_YYYY_DOT);
                case MM_DD_YYYY_SLASH: return dateTime.format(MM_DD_YYYY_SLASH);
                case YYYY_MM_DD_SLASH: return dateTime.format(YYYY_MM_DD_SLASH);
                default: throw new RuntimeException();
            }
        }
    }

    protected static final class Selenoid {

        public static final @NotNull String SERGEY = "http://10.5.1.167:4444/wd/hub";

        public static final @NotNull String ARTEM = "http://10.5.1.170:4444/wd/hub";

        public static final @Nullable String NONE = null;
    }

    protected static final class Application {

        public static final @NotNull String GEOMETA = "https://autotests.gemsdev.ru";
        public static final @NotNull String GEOMETA_CONFIGURATOR = "https://autotests.gemsdev.ru/system";

        public static final @NotNull String AGATE = "https://agate-autotests.gemsdev.ru";
        public static final @NotNull String AGATE_CONFIGURATOR = "https://agate-conf-autotests.gemsdev.ru";

        public static final @NotNull String WEB_IMPORTER = "https://importer-autotests.gemsdev.ru";

        public static final @NotNull String DIAMOND = "https://gisogd-autotests.gemsdev.ru";
    }

    protected static final class User {

        public static final @NotNull User GEOMETA_ADMIN = new User("gemsAdmin", "gemsAdmin123$");

        public static final @NotNull User GEOMETA_USER = new User("autotests1", "autotests1Admin123$");

        private final @NotNull String login;

        private final @NotNull String password;

        public User(@NotNull String login, @NotNull String password) {
            this.password = password;
            this.login = login;
        }

        public @NotNull String getLogin() {
            return login;
        }

        public @NotNull String getPassword() {
            return password;
        }
    }

    protected enum Driver {CHROME_DEFAULT, CHROME_CRYPTOGRAPHY}

    protected final @NotNull String randomUUID = UUID.randomUUID().toString();

    private final @NotNull Path PATH_TO_DOWNLOADS = Paths.get(String.format("build/downloads/%s", randomUUID)).toAbsolutePath();

    protected abstract @NotNull String initializeApplication();

    protected @NotNull Duration initializeTimeout() {
        return Duration.ofSeconds(30);
    }

    protected @NotNull Driver initializeDriver() {
        return Driver.CHROME_DEFAULT;
    }

    protected @Nullable String initializeSelenoid() {
        return Selenoid.NONE;
    }

    @BeforeEach
    public final void initialization() throws MalformedURLException {
        final var selenoid = initializeSelenoid();

        switch (initializeDriver()) {
            case CHROME_CRYPTOGRAPHY: {
                if (selenoid == null) Chrome.setWithCryptography(PATH_TO_DOWNLOADS, initializeTimeout());
                else Chrome.setWithCryptography(selenoid, initializeTimeout());
            }
            case CHROME_DEFAULT: {
                if (selenoid == null) Chrome.setDefault(PATH_TO_DOWNLOADS, initializeTimeout());
                else Chrome.setDefault(selenoid, initializeTimeout());
            }
        }

        Configuration.getWebDriver().navigate().to(initializeApplication());
        Report.setStartTime(System.currentTimeMillis());
        Report.clear();
    }

    @AfterEach
    public final void finalization() throws IOException {
        Report.perform();
        Configuration.getWebDriver().quit();
        if (Report.isFailed()) Assertions.fail("Зафиксированы ошибки! Пожалуйста, проверьте отчёт.");
    }
}
