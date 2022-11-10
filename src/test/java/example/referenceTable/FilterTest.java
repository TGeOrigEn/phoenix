package example.referenceTable;

import example.*;
import example.field.Checkbox;
import example.field.Field;
import example.field.TextAreaField;
import example.field.TextField;
import example.field.dropdown.DropdownField;
import example.window.Alert;
import example.window.Card;
import example.window.Window;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.untitled.phoenix.component.Component;
import org.untitled.phoenix.component.requirement.generic.Requirement;

import java.time.Duration;

public class FilterTest extends BaseTest {

    @BeforeEach
    public void init() {
        Component.find(AuthorizationForm::new).logIn("gemsAdmin", "gemsAdmin123$");

        Component.find(NavigationPanel.Item::new, NavigationPanel.Item.Requirements.Equals.byName("Приморский край")).expand();
        Component.find(NavigationPanel.Item::new, NavigationPanel.Item.Requirements.Equals.byName("autotests")).expand();
        Component.find(NavigationPanel.Item::new, NavigationPanel.Item.Requirements.Equals.byName("presentationPanel")).expand();
    }

    @Test
    public void test_0() {
        final var card = Component.find(Card::new, Window.Requirements.isActive(true));
        final var viewPanel = Component.find(ViewPanel::new);
        final var alert = Component.find(Alert::new);

        Component.find(NavigationPanel.Item::new, NavigationPanel.Item.Requirements.Equals.byName("Растительность")).open(NavigationPanel.Item.Option.TABLE);

        viewPanel.findInside(Button::new, Button.Requirements.Equals.byTip("Создать новый объект")).click();

        card.findInside(DropdownField::new, Field.Requirements.Equals.byTitle("Вид объекта:")).setValue("Бугор");
        card.findInside(TextAreaField::new, Field.Requirements.Equals.byTitle("Наименование:")).setValue("Бугор");
        card.findInside(TextField::new, Field.Requirements.Equals.byTitle("Примечание:")).setValue("Не пусто");
        card.findInside(Button::new, Button.Requirements.Equals.byTip("Сохранить")).click();
        card.close();

        viewPanel.findInside(Button::new, Button.Requirements.Equals.byTip("Создать новый объект")).click();

        card.findInside(DropdownField::new, Field.Requirements.Equals.byTitle("Вид объекта:")).setValue("Яма");
        card.findInside(TextAreaField::new, Field.Requirements.Equals.byTitle("Наименование:")).setValue("Яма");
        card.findInside(Button::new, Button.Requirements.Equals.byTip("Сохранить")).click();
        card.close();

        viewPanel.findInside(Button::new, Button.Requirements.Equals.byTip("Обновить таблицу")).click();

        var sort = viewPanel.findInside(ViewPanel.Header::new, ViewPanel.Header.Requirements.Equals.byName("Примечание")).openSort();
        sort.findInside(Menu.Option::new, Menu.Option.Requirements.Equals.byText("Фильтр")).hover();
        ((Checkbox) sort.findInside(Checkbox::new, Field.Requirements.Equals.byTitle("Не пусто"))).check();

        viewPanel.findInside(Button::new, Button.Requirements.Equals.byTip("Обновить таблицу")).click();

        viewPanel.findInside(ViewPanel.Item::new, ViewPanel.Item.Requirements.Equals.byValue("Примечание", "Не пусто")).show(ViewPanel.Item.Option.CARD);
        card.findInside(Button::new, Button.Requirements.Equals.byText("Еще")).showMenu().findInside(Menu.Option::new, Menu.Option.Requirements.Equals.byText("Удалить объект")).click();

        alert.findInside(Button::new, Button.Requirements.Equals.byText("Удалить")).click();

        viewPanel.findInside(Button::new, Button.Requirements.Equals.byTip("Фильтры")).showMenu().findInside(Menu.Option::new, Menu.Option.Requirements.Equals.byText("Сбросить все фильтры")).click();

        viewPanel.findInside(Button::new, Button.Requirements.Equals.byTip("Обновить таблицу")).click();

        viewPanel.findInside(ViewPanel.Item::new, ViewPanel.Item.Requirements.Equals.byValue("Примечание", "")).show(ViewPanel.Item.Option.CARD);
        card.findInside(Button::new, Button.Requirements.Equals.byText("Еще")).showMenu().findInside(Menu.Option::new, Menu.Option.Requirements.Equals.byText("Удалить объект")).click();
        alert.findInside(Button::new, Button.Requirements.Equals.byText("Удалить")).click();

        viewPanel.findInside(Button::new, Button.Requirements.Equals.byTip("Обновить таблицу")).click();

        Component.should(viewPanel.findInside(ViewPanel.Item::new), Requirement.byAvailable(false), Duration.ofSeconds(5));
    }

    @Test
    public void test_1() {
        final var card = Component.find(Card::new, Window.Requirements.isActive(true));
        final var viewPanel = Component.find(ViewPanel::new);
        final var alert = Component.find(Alert::new);

        Component.find(NavigationPanel.Item::new, NavigationPanel.Item.Requirements.Equals.byName("Растительность")).open(NavigationPanel.Item.Option.TABLE);

        viewPanel.findInside(Button::new, Button.Requirements.Equals.byTip("Создать новый объект")).click();

        card.findInside(DropdownField::new, Field.Requirements.Equals.byTitle("Вид объекта:")).setValue("Бугор");
        card.findInside(TextAreaField::new, Field.Requirements.Equals.byTitle("Наименование:")).setValue("Бугор");
        card.findInside(TextField::new, Field.Requirements.Equals.byTitle("Примечание:")).setValue("Не пусто");
        card.findInside(Button::new, Button.Requirements.Equals.byTip("Сохранить")).click();
        card.close();

        viewPanel.findInside(Button::new, Button.Requirements.Equals.byTip("Создать новый объект")).click();

        card.findInside(DropdownField::new, Field.Requirements.Equals.byTitle("Вид объекта:")).setValue("Яма");
        card.findInside(TextAreaField::new, Field.Requirements.Equals.byTitle("Наименование:")).setValue("Яма");
        card.findInside(Button::new, Button.Requirements.Equals.byTip("Сохранить")).click();
        card.close();

        viewPanel.findInside(Button::new, Button.Requirements.Equals.byTip("Обновить таблицу")).click();

        var sort = viewPanel.findInside(ViewPanel.Header::new, ViewPanel.Header.Requirements.Equals.byName("Примечание")).openSort();
        sort.findInside(Menu.Option::new, Menu.Option.Requirements.Equals.byText("Фильтр")).hover();
        ((Checkbox) sort.findInside(Checkbox::new, Field.Requirements.Equals.byTitle("Пусто"))).check();

        viewPanel.findInside(Button::new, Button.Requirements.Equals.byTip("Обновить таблицу")).click();

        viewPanel.findInside(ViewPanel.Item::new, ViewPanel.Item.Requirements.Equals.byValue("Примечание", "")).show(ViewPanel.Item.Option.CARD);
        card.findInside(Button::new, Button.Requirements.Equals.byText("Еще")).showMenu().findInside(Menu.Option::new, Menu.Option.Requirements.Equals.byText("Удалить объект")).click();

        alert.findInside(Button::new, Button.Requirements.Equals.byText("Удалить")).click();

        viewPanel.findInside(Button::new, Button.Requirements.Equals.byTip("Фильтры")).showMenu().findInside(Menu.Option::new, Menu.Option.Requirements.Equals.byText("Сбросить все фильтры")).click();

        viewPanel.findInside(Button::new, Button.Requirements.Equals.byTip("Обновить таблицу")).click();

        viewPanel.findInside(ViewPanel.Item::new, ViewPanel.Item.Requirements.Equals.byValue("Примечание", "Не пусто")).show(ViewPanel.Item.Option.CARD);
        card.findInside(Button::new, Button.Requirements.Equals.byText("Еще")).showMenu().findInside(Menu.Option::new, Menu.Option.Requirements.Equals.byText("Удалить объект")).click();
        alert.findInside(Button::new, Button.Requirements.Equals.byText("Удалить")).click();

        viewPanel.findInside(Button::new, Button.Requirements.Equals.byTip("Обновить таблицу")).click();

        Component.should(viewPanel.findInside(ViewPanel.Item::new), Requirement.byAvailable(false), Duration.ofSeconds(5));
    }
}
