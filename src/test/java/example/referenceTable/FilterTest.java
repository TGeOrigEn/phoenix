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
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.untitled.phoenix.component.Component;
import org.untitled.phoenix.component.requirement.generic.Requirement;
import org.untitled.phoenix.configuration.Configuration;

import java.time.Duration;

public class FilterTest extends BaseTest {

    private static final Card card = (Card) Component.find(Card::new, Window.Requirements.isActive(true));
    private static final ViewPanel viewPanel = Component.find(ViewPanel::new);
    private static final Alert alert = Component.find(Alert::new);

    @Override
    protected @NotNull String getAddress() {
        return "https://autotests.gemsdev.ru/";
    }

    @BeforeEach
    public void beforeEach() {
        Configuration.getWebDriver().navigate().to("https://autotests.gemsdev.ru/");

        Component.find(AuthorizationForm::new).logIn("gemsAdmin", "gemsAdmin123$");

        Component.find(NavigationPanel.Item::new, NavigationPanel.Item.Requirements.Equals.byName("Приморский край")).expand();
        Component.find(NavigationPanel.Item::new, NavigationPanel.Item.Requirements.Equals.byName("autotests")).expand();
        Component.find(NavigationPanel.Item::new, NavigationPanel.Item.Requirements.Equals.byName("presentationPanel")).expand();

        Component.find(NavigationPanel.Item::new, NavigationPanel.Item.Requirements.Equals.byName("Растительность")).open(NavigationPanel.Item.Option.TABLE);

        removeAllItem();

        viewPanel.findInside(Button::new, Button.Requirements.Equals.byTip("Создать новый объект")).click();

        card.findInside(DropdownField::new, Field.Requirements.Equals.byTitle("Вид объекта:")).setValue("Бугор");
        card.findInside(TextAreaField::new, Field.Requirements.Equals.byTitle("Наименование:")).setValue("Бугор");
        card.findInside(TextField::new, Field.Requirements.Equals.byTitle("Примечание:")).setValue("Не пусто");
        card.findInside(Button::new, Button.Requirements.Equals.byTip("Сохранить")).click();
        card.close();

        viewPanel.findInside(Button::new, Button.Requirements.Equals.byTip("Обновить таблицу")).click();

        Component.should(viewPanel.findInside(ViewPanel.Item::new, ViewPanel.Item.Requirements.Equals.byValue("Наименование", "Бугор")), Requirement.byAvailable(true), Duration.ofSeconds(60));

        viewPanel.findInside(Button::new, Button.Requirements.Equals.byTip("Создать новый объект")).click();

        card.findInside(DropdownField::new, Field.Requirements.Equals.byTitle("Вид объекта:")).setValue("Яма");
        card.findInside(TextAreaField::new, Field.Requirements.Equals.byTitle("Наименование:")).setValue("Яма");
        card.findInside(Button::new, Button.Requirements.Equals.byTip("Сохранить")).click();
        card.close();

        refreshTable();

        Component.should(viewPanel.findInside(ViewPanel.Item::new, ViewPanel.Item.Requirements.Equals.byValue("Наименование", "Яма")), Requirement.byAvailable(true), Duration.ofSeconds(60));
    }

    @Test
    public void sortTexNoEmpty() {
        var sort = viewPanel.findInside(ViewPanel.Header::new, ViewPanel.Header.Requirements.Equals.byName("Примечание")).openSort();
        sort.findInside(Menu.Option::new, Menu.Option.Requirements.Equals.byText("Фильтр")).hover();
        ((Checkbox) sort.findInside(Checkbox::new, Field.Requirements.Equals.byTitle("Не пусто"))).check();

        refreshTable();

        Component.should(viewPanel.findInside(ViewPanel.Item::new, ViewPanel.Item.Requirements.Equals.byValue("Примечание", "Не пусто")), Requirement.byAvailable(true), Duration.ofSeconds(60));
        Component.should(viewPanel.findInside(ViewPanel.Item::new, ViewPanel.Item.Requirements.Equals.byValue("Примечание", " ")), Requirement.byAvailable(false), Duration.ofSeconds(60));

        checkItems();
    }

    @Test
    public void sortTexEmpty() {
        var sort = viewPanel.findInside(ViewPanel.Header::new, ViewPanel.Header.Requirements.Equals.byName("Примечание")).openSort();
        sort.findInside(Menu.Option::new, Menu.Option.Requirements.Equals.byText("Фильтр")).hover();
        ((Checkbox) sort.findInside(Checkbox::new, Field.Requirements.Equals.byTitle("Пусто"))).check();

        refreshTable();

        Component.should(viewPanel.findInside(ViewPanel.Item::new, ViewPanel.Item.Requirements.Equals.byValue("Примечание", "Не пусто")), Requirement.byAvailable(false), Duration.ofSeconds(60));
        Component.should(viewPanel.findInside(ViewPanel.Item::new, ViewPanel.Item.Requirements.Equals.byValue("Примечание", " ")), Requirement.byAvailable(true), Duration.ofSeconds(60));

        checkItems();
    }

    @Test
    public void sortTexNoContains() {
        var sort = viewPanel.findInside(ViewPanel.Header::new, ViewPanel.Header.Requirements.Equals.byName("Примечание")).openSort();
        sort.findInside(Menu.Option::new, Menu.Option.Requirements.Equals.byText("Фильтр")).hover();
        sort.findInside(TextField::new, Field.Requirements.Equals.byTitle("Не содержит")).toAction().click();
        sort.findInside(TextField::new, Field.Requirements.Equals.byTitle("Не содержит")).setValue("Не");

        refreshTable();

        Component.should(viewPanel.findInside(ViewPanel.Item::new, ViewPanel.Item.Requirements.Equals.byValue("Примечание", "Не пусто")), Requirement.byAvailable(false), Duration.ofSeconds(60));
        Component.should(viewPanel.findInside(ViewPanel.Item::new, ViewPanel.Item.Requirements.Equals.byValue("Примечание", " ")), Requirement.byAvailable(true), Duration.ofSeconds(60));

        checkItems();
    }

    @Test
    public void sortTexContains() {
        var sort = viewPanel.findInside(ViewPanel.Header::new, ViewPanel.Header.Requirements.Equals.byName("Примечание")).openSort();
        sort.findInside(Menu.Option::new, Menu.Option.Requirements.Equals.byText("Фильтр")).hover();
        sort.findInside(TextField::new, Field.Requirements.Equals.byTitle("Содержит")).toAction().click();
        sort.findInside(TextField::new, Field.Requirements.Equals.byTitle("Содержит")).setValue("Не");

        refreshTable();

        Component.should(viewPanel.findInside(ViewPanel.Item::new, ViewPanel.Item.Requirements.Equals.byValue("Примечание", "Не пусто")), Requirement.byAvailable(true), Duration.ofSeconds(60));
        Component.should(viewPanel.findInside(ViewPanel.Item::new, ViewPanel.Item.Requirements.Equals.byValue("Примечание", " ")), Requirement.byAvailable(false), Duration.ofSeconds(60));

        checkItems();
    }

    @Test
    public void sortTexNoEquals() {
        var sort = viewPanel.findInside(ViewPanel.Header::new, ViewPanel.Header.Requirements.Equals.byName("Примечание")).openSort();
        sort.findInside(Menu.Option::new, Menu.Option.Requirements.Equals.byText("Фильтр")).hover();
        sort.findInside(TextField::new, Field.Requirements.Equals.byTitle("Не равно")).toAction().click();
        sort.findInside(TextField::new, Field.Requirements.Equals.byTitle("Не равно")).setValue("Не пусто");

        refreshTable();

        Component.should(viewPanel.findInside(ViewPanel.Item::new, ViewPanel.Item.Requirements.Equals.byValue("Примечание", "Не пусто")), Requirement.byAvailable(false), Duration.ofSeconds(60));
        Component.should(viewPanel.findInside(ViewPanel.Item::new, ViewPanel.Item.Requirements.Equals.byValue("Примечание", " ")), Requirement.byAvailable(true), Duration.ofSeconds(60));

        checkItems();
    }

    @Test
    public void sortTexEquals() {
        var sort = viewPanel.findInside(ViewPanel.Header::new, ViewPanel.Header.Requirements.Equals.byName("Примечание")).openSort();
        sort.findInside(Menu.Option::new, Menu.Option.Requirements.Equals.byText("Фильтр")).hover();
        sort.findInside(TextField::new, Field.Requirements.Equals.byTitle("Равно")).toAction().click();
        sort.findInside(TextField::new, Field.Requirements.Equals.byTitle("Равно")).setValue("Не пусто");

        refreshTable();

        Component.should(viewPanel.findInside(ViewPanel.Item::new, ViewPanel.Item.Requirements.Equals.byValue("Примечание", "Не пусто")), Requirement.byAvailable(true), Duration.ofSeconds(60));
        Component.should(viewPanel.findInside(ViewPanel.Item::new, ViewPanel.Item.Requirements.Equals.byValue("Примечание", " ")), Requirement.byAvailable(false), Duration.ofSeconds(60));

        checkItems();
    }

    @AfterEach
    public void afterEach() {
        removeAllItem();
    }

    private static void refreshTable() {
        viewPanel.findInside(Button::new, Button.Requirements.Equals.byTip("Обновить таблицу")).click();
        Component.find(Spinner::new).wait(Duration.ofSeconds(60));
    }

    private static void checkItems() {
        viewPanel.findInside(Button::new, Button.Requirements.Equals.byTip("Фильтры")).showMenu().findInside(Menu.Option::new, Menu.Option.Requirements.Equals.byText("Сбросить все фильтры")).click();

        refreshTable();

        Component.should(viewPanel.findInside(ViewPanel.Item::new, ViewPanel.Item.Requirements.Equals.byValue("Примечание", "Не пусто")), Requirement.byAvailable(true), Duration.ofSeconds(60));
        Component.should(viewPanel.findInside(ViewPanel.Item::new, ViewPanel.Item.Requirements.Equals.byValue("Примечание", " ")), Requirement.byAvailable(true), Duration.ofSeconds(60));
    }

    private static void removeAllItem() {
        final var item = viewPanel.findInside(ViewPanel.Item::new);

        while (Component.has(item,  Requirement.byAvailable(true), Duration.ofSeconds(1))) {
            item.show(ViewPanel.Item.Option.CARD);

            card.findInside(Button::new, Button.Requirements.Equals.byText("Еще")).showMenu().findInside(Menu.Option::new, Menu.Option.Requirements.Equals.byText("Удалить объект")).click();
            alert.findInside(Button::new, Button.Requirements.Equals.byText("Удалить")).click();

            refreshTable();
        }
    }

}
