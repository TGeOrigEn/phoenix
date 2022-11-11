package example.drivers;

import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.File;
import java.time.Duration;

public final class Chrome {

    public static @NotNull ChromeOptions getDefaultOptions(@NotNull Duration timeout) {
        final var chromeOptions = new ChromeOptions();

        chromeOptions.setCapability("sessionTimeout", String.format("%dms", timeout.toMillis()));
        chromeOptions.setCapability("screenResolution", "1920x1080x24");
        chromeOptions.setCapability("browserName", "chrome");
        chromeOptions.setCapability("enableVideo", true);
        chromeOptions.setCapability("enableVNC", true);
        chromeOptions.setCapability("version", "98.0");

        return chromeOptions;
    }

    public static @NotNull ChromeOptions getOptionsWithCryptography(@NotNull Duration timeout) {
        final var chromeOptions = new ChromeOptions();

        chromeOptions.addExtensions(new File("/opt/cades_plugin.crx"));

        chromeOptions.setCapability("sessionTimeout", String.format("%dms", timeout.toMillis()));
        chromeOptions.setCapability("screenResolution", "1920x1080x24");
        chromeOptions.setCapability("browserName", "chrome");
        chromeOptions.setCapability("enableVideo", true);
        chromeOptions.setCapability("enableVNC", true);
        chromeOptions.setCapability("version", "cryptochrome");

        return chromeOptions;
    }
}
