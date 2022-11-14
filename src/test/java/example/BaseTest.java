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
import java.net.URL;

public abstract class BaseTest {

    public enum Driver {CHROME_DEFAULT, CHROME_CRYPTOGRAPHY}

    private static final @NotNull Path PATH_TO_DOWNLOADS = Paths.get("build/downloads/").toAbsolutePath();

    protected abstract @NotNull URL addressInitialization() throws MalformedURLException;

    protected @Nullable URL remoteAddressInitialization() throws MalformedURLException {
        return null;
    }

    protected @NotNull Duration timeoutInitialization() {
        return Duration.ofSeconds(120);
    }

    protected @NotNull Driver driverInitialization() {
        return Driver.CHROME_DEFAULT;
    }

    @BeforeEach
    public final void testInitialization() throws MalformedURLException {
        final var remoteAddress = remoteAddressInitialization();
        final var timeout = timeoutInitialization();

        switch (driverInitialization()) {
            case CHROME_CRYPTOGRAPHY: {
                if (remoteAddress == null) Chrome.setWithCryptography(PATH_TO_DOWNLOADS, timeout);
                else Chrome.setWithCryptography(PATH_TO_DOWNLOADS, remoteAddress, timeout);
            }
            case CHROME_DEFAULT: {
                if (remoteAddress == null) Chrome.setDefault(PATH_TO_DOWNLOADS, timeout);
                else Chrome.setDefault(PATH_TO_DOWNLOADS, remoteAddress, timeout);
            }
        }
        Configuration.getWebDriver().navigate().to(addressInitialization());
        Report.setStartTime(System.currentTimeMillis());
        Report.clear();
    }

    @AfterEach
    public final void testFinalization() throws IOException {
        Report.perform();
        Configuration.getWebDriver().quit();
        if (Report.isFailed()) Assertions.fail("Зафиксированы ошибки! Пожалуйста, проверьте отчёт.");
    }
}
