package example;

import org.untitled.phoenix.configuration.Configuration;
import example.drivers.Chrome;
import org.other.Report;

import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;

import org.junit.jupiter.api.Assertions;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.time.Duration;

public abstract class BaseTest {

    public enum Driver {CHROME_DEFAULT, CHROME_CRYPTOGRAPHY}

    private static final @NotNull Path PATH_TO_DOWNLOADS = Paths.get("build/downloads/").toAbsolutePath();

    protected abstract @NotNull String getAddress();

    protected @NotNull Duration getTimeout() {
        return Duration.ofSeconds(120);
    }

    protected @NotNull Driver getDriver() {
        return Driver.CHROME_DEFAULT;
    }

    protected @Nullable String getRemoteAddress() {
        return null;
    }

    @BeforeEach
    public final void initialization() throws MalformedURLException {
        final var remoteAddress = getRemoteAddress();

        switch (getDriver()) {
            case CHROME_CRYPTOGRAPHY: {
                if (remoteAddress == null) Chrome.setWithCryptography(PATH_TO_DOWNLOADS, getTimeout());
                else Chrome.setWithCryptography(PATH_TO_DOWNLOADS, remoteAddress, getTimeout());
            }
            case CHROME_DEFAULT: {
                if (remoteAddress == null) Chrome.setDefault(PATH_TO_DOWNLOADS, getTimeout());
                else Chrome.setDefault(PATH_TO_DOWNLOADS, remoteAddress, getTimeout());
            }
        }

        Configuration.getWebDriver().navigate().to(getAddress());
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
