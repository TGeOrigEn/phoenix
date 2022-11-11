package example.drivers;

import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.File;
import java.time.Duration;

public final class Chrome {

    public static @NotNull ChromeOptions getDefaultOptions(@NotNull Duration timeout) {
        final var chromeOptions = new ChromeOptions();

        chromeOptions.setCapability("browser.download.manager.showWhenStarting", false);
        chromeOptions.setCapability("browser.helperApps.neverAsk.openFile",
                "text/csv,application/x-msexcel,application/excel,application/x-excel,application/vnd.ms-excel,image/png,image/jpeg,text/html,text/plain,application/msword,application/xml");
        chromeOptions.setCapability("browser.helperApps.neverAsk.saveToDisk",
                "text/csv,application/x-msexcel,application/excel,application/x-excel,application/vnd.ms-excel,image/png,image/jpeg,text/html,text/plain,application/msword,application/xml");
        chromeOptions.setCapability("browser.helperApps.alwaysAsk.force", false);
        chromeOptions.setCapability("browser.download.manager.alertOnEXEOpen", false);
        chromeOptions.setCapability("browser.download.manager.focusWhenStarting", false);
        chromeOptions.setCapability("browser.download.manager.useWindow", false);
        chromeOptions.setCapability("browser.download.manager.showAlertOnComplete", false);
        chromeOptions.setCapability("browser.download.manager.closeWhenDone", false);

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

        chromeOptions.setCapability("browser.download.manager.showWhenStarting", false);
        chromeOptions.setCapability("browser.helperApps.neverAsk.openFile",
                "text/csv,application/x-msexcel,application/excel,application/x-excel,application/vnd.ms-excel,image/png,image/jpeg,text/html,text/plain,application/msword,application/xml");
        chromeOptions.setCapability("browser.helperApps.neverAsk.saveToDisk",
                "text/csv,application/x-msexcel,application/excel,application/x-excel,application/vnd.ms-excel,image/png,image/jpeg,text/html,text/plain,application/msword,application/xml");
        chromeOptions.setCapability("browser.helperApps.alwaysAsk.force", false);
        chromeOptions.setCapability("browser.download.manager.alertOnEXEOpen", false);
        chromeOptions.setCapability("browser.download.manager.focusWhenStarting", false);
        chromeOptions.setCapability("browser.download.manager.useWindow", false);
        chromeOptions.setCapability("browser.download.manager.showAlertOnComplete", false);
        chromeOptions.setCapability("browser.download.manager.closeWhenDone", false);

        chromeOptions.setCapability("sessionTimeout", String.format("%dms", timeout.toMillis()));
        chromeOptions.setCapability("screenResolution", "1920x1080x24");
        chromeOptions.setCapability("version", "cryptochrome");
        chromeOptions.setCapability("browserName", "chrome");
        chromeOptions.setCapability("enableVideo", true);
        chromeOptions.setCapability("enableVNC", true);

        return chromeOptions;
    }
}
