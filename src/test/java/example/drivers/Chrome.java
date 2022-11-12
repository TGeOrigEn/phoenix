package example.drivers;

import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.File;
import java.time.Duration;

public final class Chrome {

    public static @NotNull ChromeOptions getDefaultOptions(@NotNull Duration timeout) {
        final var chromeOptions = new ChromeOptions();

        chromeOptions.setCapability("browser.download.folderList", 2);
        chromeOptions.setCapability("browser.download.manager.showWhenStarting", false);
        chromeOptions.setCapability("browser.helperApps.alwaysAsk.force", false);
        chromeOptions.setCapability("browser.helperApps.neverAsk.saveToDisk", "*.*");
        chromeOptions.setCapability("browser.download.manager.focusWhenStarting",false);
        chromeOptions.setCapability("browser.download.manager.useWindow", false);
        chromeOptions.setCapability("browser.download.manager.showAlertOnComplete", false);
        chromeOptions.setCapability("pdfjs.disabled", true);



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

        chromeOptions.addArguments("--browser.download.folderList=1");
        chromeOptions.addArguments("--browser.download.manager.showWhenStarting=false");
        chromeOptions.addArguments("--browser.download.manager.focusWhenStarting=false");
        chromeOptions.addArguments("--browser.download.useDownloadDir=true");
        chromeOptions.addArguments("--browser.helperApps.alwaysAsk.force=false");
        chromeOptions.addArguments("--browser.download.manager.alertOnEXEOpen=false");
        chromeOptions.addArguments("--browser.download.manager.closeWhenDone=true");
        chromeOptions.addArguments("--browser.download.manager.showAlertOnComplete=false");
        chromeOptions.addArguments("--browser.download.manager.useWindow=false");
        // You will need to find the content-type of your app and set it here.
        chromeOptions.addArguments("--browser.helperApps.neverAsk.saveToDisk=application/octet-stream");

        chromeOptions.setCapability("sessionTimeout", String.format("%dms", timeout.toMillis()));
        chromeOptions.setCapability("screenResolution", "1920x1080x24");
        chromeOptions.setCapability("version", "cryptochrome");
        chromeOptions.setCapability("browserName", "chrome");
        chromeOptions.setCapability("enableVideo", true);
        chromeOptions.setCapability("enableVNC", true);

        return chromeOptions;
    }
}
