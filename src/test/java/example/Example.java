package example;

import example.card.Card;
import example.field.Field;
import example.field.Text;
import example.field.TextArea;
import example.field.select.Select;
import example.field.select.menu.Option;
import geoMeta.AuthorizationForm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.chrome.ChromeOptions;
import org.untitled.phoenix.component.Component;
import org.untitled.phoenix.configuration.Configuration;

import java.nio.file.Paths;
import java.time.Duration;

public class Example {

    @BeforeEach
    public void beforeEach() {
        var url = this.getClass().getClassLoader().getResource("drivers/chromedriver.exe");

        if (url == null) throw new RuntimeException();
        System.setProperty("webdriver.chrome.driver", url.getPath());

        final var options = new ChromeOptions();
        options.setCapability("enableVNC", true);
        options.setCapability("screenResolution", "1920x1080x24");
        options.setBrowserVersion("test");

        final var file = Paths.get("build/downloads/").toFile();

        //Configuration.setRemoteWebDriver(new URL("http://10.5.1.167:4444/wd/hub"), file.getAbsolutePath(), options);
        Configuration.setWebDriver(file.getAbsolutePath(), options);

        var dimension = new Dimension(1920, 1080);

        Configuration.getWebDriver().manage().window().setSize(dimension);
        Configuration.getWebDriver().get("https://autotests.gemsdev.ru/");
    }

    @Test
    public void test_0() throws InterruptedException {
        Component.find(AuthorizationForm::new).logIn("gemsAdmin", "gemsAdmin123$");

        Component.find(Item::new, Item.Requirements.Equals.byName("Приморский край")).expand();
        Component.find(Item::new, Item.Requirements.Equals.byName("Базовая карта")).expand();
        Component.find(Item::new, Item.Requirements.Equals.byName("Городское поселение (СТП ОП)")).openTable();

        Thread.sleep(500);

        Component.find(Button::new, Button.Requirements.Equals.byTip("Создать новый объект")).click();

        Component.find(Card::new).findInside(Text::new, Field.Requirements.Equals.byTitle("Площадь, кв. м.:")).setValue("1");

        ((Select) Component.find(Card::new).findInside(Select::new,  Field.Requirements.Equals.byTitle("Субъект градостроительных отношений:")))
                .clickOnArrowButton().getOptionBy(Option.Requirements.Equals.byText("Российская Федерация")).click();

        Component.find(Card::new).findInside(TextArea::new, Field.Requirements.Equals.byTitle("Код ОКАТО:")).setValue("Код ОКАТО");

        Component.find(Card::new).findInside(Text::new, Field.Requirements.Equals.byTitle("Идентификатор:")).setValue("Идентификатор");

        Component.find(Card::new).findInside(Text::new, Field.Requirements.Equals.byTitle("Численность населения, тыс. чел:")).setValue("3");

        ((Select) Component.find(Card::new).findInside(Select::new,  Field.Requirements.Equals.byTitle("Вид объекта:")))
                .clickOnArrowButton().getOptionBy(Option.Requirements.Equals.byText("Городское поселение")).click();

        ((Select) Component.find(Card::new).findInside(Select::new,  Field.Requirements.Equals.byTitle("Статус объекта административно-территориального деления:")))
                .clickOnAddButton(Duration.ofSeconds(2)).close();

        Component.find(Card::new).findInside(TextArea::new, Field.Requirements.Equals.byTitle("Собственное название:")).setValue("Собственное название");

        Component.find(Card::new).findInside(TextArea::new, Field.Requirements.Equals.byTitle("Код ОКТМО:")).setValue("Код ОКТМО");

        Component.find(Card::new).findInside(TextArea::new, Field.Requirements.Equals.byTitle("Примечание:")).setValue("Примечание");

        Component.find(Card::new).findInside(TextArea::new, Field.Requirements.Equals.byTitle("Источник данных:")).setValue("Источник данных");
    }

    @Test
    public void test() {
        Component.find(AuthorizationForm::new).logIn("gemsAdmin", "gemsAdmin123$");

        Component.find(Button::new, Button.Requirements.Equals.byTip("Графический отчет")).click();

        Component.find(Select::new, Select.DEFAULT_DESCRIPTION.copy(1))
                .clickOnArrowButton()
                .getOptionBy(Option.Requirements.Equals.byText("А4, Альбомный (PDF)"))
                .click();

        Component.find(Button::new, Button.Requirements.Equals.byText("Далее")).click();

        Component.find(Select::new, Select.DEFAULT_DESCRIPTION.copy(2))
                .clickOnArrowButton()
                .getOptionBy(Option.Requirements.Equals.byText("~1:1600"))
                .click();

        final var file = Component.find(Button::new, Button.Requirements.Equals.byText("Сформировать")).toAction().download(Duration.ofSeconds(600), 1);

        final var s = file.get(0).exists();
    }
}
