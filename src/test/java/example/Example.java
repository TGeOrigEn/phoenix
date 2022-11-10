package example;

import example.window.Card;
import example.window.Window;
import example.field.Field;
import example.field.TextField;
import example.field.TextAreaField;
import example.field.dropdown.DropdownField;
import example.field.dropdown.Dropdown;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.chrome.ChromeOptions;
import org.untitled.phoenix.component.Component;
import org.untitled.phoenix.configuration.Configuration;

import java.nio.file.Paths;
import java.time.Duration;
import java.util.UUID;

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
    public void test_0() {
        Component.find(AuthorizationForm::new).logIn("gemsAdmin", "gemsAdmin123$");

        Component.find(NavigationPanel.Item::new, NavigationPanel.Item.Requirements.Equals.byName("Приморский край")).expand();
        Component.find(NavigationPanel.Item::new, NavigationPanel.Item.Requirements.Equals.byName("Базовая карта")).expand();

        Component.find(NavigationPanel.Item::new, NavigationPanel.Item.Requirements.Equals.byName("Городское поселение (СТП ОП)")).open(NavigationPanel.Item.Option.TABLE);

        Component.find(Spinner::new).wait(Duration.ofSeconds(60));

        Component.find(Button::new, Button.Requirements.Equals.byTip("Создать новый объект")).click();

        final var card = Component.find(Card::new, Window.Requirements.isActive(true));

        card.findInside(TextField::new, Field.Requirements.Equals.byTitle("Площадь, кв. м.:")).setValue("1");

        ((DropdownField) card.findInside(DropdownField::new,  Field.Requirements.Equals.byTitle("Субъект градостроительных отношений:")))
                .openDropdown().findInside(Dropdown.Option::new, Dropdown.Option.Requirements.Equals.byText("Российская Федерация")).click();

        card.findInside(TextAreaField::new, Field.Requirements.Equals.byTitle("Код ОКАТО:")).setValue("Код ОКАТО");

        card.findInside(TextField::new, Field.Requirements.Equals.byTitle("Идентификатор:")).setValue("Идентификатор");

        card.findInside(TextField::new, Field.Requirements.Equals.byTitle("Численность населения, тыс. чел:")).setValue("3");

        ((DropdownField) card.findInside(DropdownField::new,  Field.Requirements.Equals.byTitle("Вид объекта:")))
                .openDropdown().findInside(Dropdown.Option::new,Dropdown.Option.Requirements.Equals.byText("Городское поселение")).click();

        card.findInside(TextAreaField::new, Field.Requirements.Equals.byTitle("Собственное название:")).setValue("Собственное название");

        ((DropdownField) card.findInside(DropdownField::new,  Field.Requirements.Equals.byTitle("Статус объекта административно-территориального деления:"))).addNewObject();
        card.findInside(TextField::new, Field.Requirements.Equals.byTitle("Наименование:")).setValue(UUID.randomUUID().toString());
        card.findInside(Button::new, Button.Requirements.Equals.byTip("Сохранить")).click();
        card.close();

        card.findInside(TextAreaField::new, Field.Requirements.Equals.byTitle("Код ОКТМО:")).setValue("Код ОКТМО");

        card.findInside(TextAreaField::new, Field.Requirements.Equals.byTitle("Примечание:")).setValue("Примечание");

        ((DropdownField) Component.find(Card::new).findInside(DropdownField::new,  Field.Requirements.Equals.byTitle("Тип муниципального образования:"))).addNewObject();
        card.findInside(TextField::new, Field.Requirements.Equals.byTitle("Наименование:")).setValue(UUID.randomUUID().toString());
        card.findInside(Button::new, Button.Requirements.Equals.byTip("Сохранить")).click();
        card.close();

        card.findInside(TextAreaField::new, Field.Requirements.Equals.byTitle("Источник данных:")).setValue("Источник данных");
    }

    @Test
    public void test() {
        Component.find(AuthorizationForm::new).logIn("gemsAdmin", "gemsAdmin123$");

        Component.find(Button::new, Button.Requirements.Equals.byTip("Графический отчет")).click();

        Component.find(DropdownField::new, DropdownField.DEFAULT_DESCRIPTION.copy(1))
                .openDropdown()
                .findInside(Dropdown.Option::new,Dropdown.Option.Requirements.Equals.byText("А4, Альбомный (PDF)"))
                .click();

        Component.find(Button::new, Button.Requirements.Equals.byText("Далее")).click();

        Component.find(DropdownField::new, DropdownField.DEFAULT_DESCRIPTION.copy(2))
                .openDropdown()
                .findInside(Dropdown.Option::new,Dropdown.Option.Requirements.Equals.byText("~1:1600"))
                .click();

        final var file = Component.find(Button::new, Button.Requirements.Equals.byText("Сформировать")).toAction().download(Duration.ofSeconds(600), 1);

        final var s = file.get(0).exists();
    }
}
