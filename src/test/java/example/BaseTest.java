package example;

import org.untitled.phoenix.configuration.Configuration;
import example.drivers.Chrome;
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

public abstract class BaseTest {

    protected static final class Selenoid {
        public static final @NotNull String SERGEY = "http://10.5.1.167:4444/wd/hub";
        public static final @NotNull String ARTEM = "http://10.5.1.170:4444/wd/hub";
    }

    protected static final class Application {
        public static final @NotNull String GEOMETA = "https://autotests.gemsdev.ru";
        public static final @NotNull String GEOMETA_CONFIGURATOR = "https://autotests.gemsdev.ru/system";

        public static final @NotNull String AGATE = "https://agate-autotests.gemsdev.ru";
        public static final @NotNull String AGATE_CONFIGURATOR = "https://agate-conf-autotests.gemsdev.ru";

        public static final @NotNull String WEB_IMPORTER = "https://importer-autotests.gemsdev.ru";

        public static final @NotNull String DIAMOND = "https://gisogd-autotests.gemsdev.ru";
    }

    protected enum Driver {CHROME_DEFAULT, CHROME_CRYPTOGRAPHY}

    private static final @NotNull Path PATH_TO_DOWNLOADS = Paths.get("build/downloads/").toAbsolutePath();

    protected abstract @NotNull String initializeApplication();

    protected @NotNull Duration initializeTimeout() {
        return Duration.ofSeconds(120);
    }

    protected @NotNull Driver initializeDriver() {
        return Driver.CHROME_DEFAULT;
    }

    protected @Nullable String initializeSelenoid() {
        return Selenoid.SERGEY;
    }

    @BeforeEach
    public final void initialization() throws MalformedURLException {
        final var selenoid = initializeSelenoid();

        switch (initializeDriver()) {
            case CHROME_CRYPTOGRAPHY: {
                if (selenoid == null) Chrome.setWithCryptography(PATH_TO_DOWNLOADS, initializeTimeout());
                else Chrome.setWithCryptography(PATH_TO_DOWNLOADS, selenoid, initializeTimeout());
            }
            case CHROME_DEFAULT: {
                if (selenoid == null) Chrome.setDefault(PATH_TO_DOWNLOADS, initializeTimeout());
                else Chrome.setDefault(PATH_TO_DOWNLOADS, selenoid, initializeTimeout());
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
