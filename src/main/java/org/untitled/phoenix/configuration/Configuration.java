package org.untitled.phoenix.configuration;

import org.openqa.selenium.WebDriver;

public final class Configuration {

    private static WebDriver webDriver;

    public static  WebDriver getWebDriver() {
        return webDriver;
    }

    public static void setWebDriver(WebDriver webDriver) {
        Configuration.webDriver = webDriver;
    }
}
