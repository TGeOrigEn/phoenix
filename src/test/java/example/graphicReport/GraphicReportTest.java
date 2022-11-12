package example.graphicReport;

import example.*;
import example.button.Button;
import example.field.Field;
import example.field.nested.DropdownField;
import io.qameta.allure.Step;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.untitled.phoenix.component.Component;
import org.untitled.phoenix.component.requirement.generic.Requirement;

import java.time.Duration;

public class GraphicReportTest extends BaseTest {

    @Override
    protected @NotNull String getAddress() {
        return "https://autotests.gemsdev.ru/";
    }

    @BeforeEach
    @DisplayName("Подготовка тестовых данных")
    public void beforeEach() {
        Component.find(AuthorizationForm::new).logIn("gemsAdmin", "gemsAdmin123$");
        Component.find(Button::new, Button.Requirements.Equals.byTip("Подложки")).click();
        Component.find(Substrates::new).findInside(Substrates.Item::new, Substrates.Item.Requirements.Equals.byName("Нет")).select();
        Component.find(Button::new, Button.Requirements.Equals.byTip("Подложки")).click();
    }

    @Test
    @Tag("all")
    @Step("Графический отчёт (PDF)")
    public void graphicReport_PDF() {
        Component.find(Button::new, Button.Requirements.Equals.byTip("Графический отчет")).click();
        Component.find(DropdownField::new, Field.Requirements.Equals.byTitle("Выберите шаблон:")).setValue("А4, Альбомный (PDF)");
        Component.find(Button::new, Button.Requirements.Equals.byText("Далее")).click();
        Component.find(DropdownField::new, Field.Requirements.Equals.byTitle("Карта:")).setValue("~1:210000");
        Component.find(Button::new, Button.Requirements.Equals.byText("Сформировать")).download(Duration.ofSeconds(10), 1);
    }

    @Test
    @Tag("all")
    @Step("Графический отчёт с выделенной геометрией (PDF)")
    public void graphicReportWithGeometry_PDF() {

        Component.find(Map::new).toAction().click();

        Component.should(Component.find(Map::new).findInside(Map.Polygon::new), Requirement.byAvailable(true), Duration.ofSeconds(60));

        Component.find(Button::new, Button.Requirements.Equals.byTip("Графический отчет")).click();
        Component.find(DropdownField::new, Field.Requirements.Equals.byTitle("Выберите шаблон:")).setValue("А4, Альбомный (PDF)");
        Component.find(Button::new, Button.Requirements.Equals.byText("Далее")).click();
        Component.find(DropdownField::new, Field.Requirements.Equals.byTitle("Карта:")).setValue("~1:210000");
        Component.find(Button::new, Button.Requirements.Equals.byText("Сформировать")).download(Duration.ofSeconds(10), 1);
    }

    @Test
    @Step("Графический отчёт (ODG)")
    public void graphicReport_ODG() {
        Component.find(Button::new, Button.Requirements.Equals.byTip("Графический отчет")).click();
        Component.find(DropdownField::new, Field.Requirements.Equals.byTitle("Выберите шаблон:")).setValue("А4, Альбомный (ODG)");
        Component.find(Button::new, Button.Requirements.Equals.byText("Далее")).click();
        Component.find(DropdownField::new, Field.Requirements.Equals.byTitle("Карта:")).setValue("~1:210000");
        Component.find(Button::new, Button.Requirements.Equals.byText("Сформировать")).click();

        Component.find(Button::new, Button.Requirements.Equals.byTip("Графический отчет")).click();
        Component.find(DropdownField::new, Field.Requirements.Equals.byTitle("Выберите шаблон:")).setValue("А4, Альбомный (ODG)");
        Component.find(Button::new, Button.Requirements.Equals.byText("Далее")).click();
        Component.find(DropdownField::new, Field.Requirements.Equals.byTitle("Карта:")).setValue("~1:210000");
        Component.find(Button::new, Button.Requirements.Equals.byText("Сформировать")).download(Duration.ofSeconds(15), 2);
    }

    @Test
    @Step("Графический отчёт с выделенной геометрией (ODG)")
    public void graphicReportWithGeometry_ODG() {

        Component.find(Map::new).toAction().click();

        Component.should(Component.find(Map::new).findInside(Map.Polygon::new), Requirement.byAvailable(true), Duration.ofSeconds(60));

        Component.find(Button::new, Button.Requirements.Equals.byTip("Графический отчет")).click();
        Component.find(DropdownField::new, Field.Requirements.Equals.byTitle("Выберите шаблон:")).setValue("А4, Альбомный (ODG)");
        Component.find(Button::new, Button.Requirements.Equals.byText("Далее")).click();
        Component.find(DropdownField::new, Field.Requirements.Equals.byTitle("Карта:")).setValue("~1:210000");
        Component.find(Button::new, Button.Requirements.Equals.byText("Сформировать")).download(Duration.ofSeconds(10), 1);
    }
}
