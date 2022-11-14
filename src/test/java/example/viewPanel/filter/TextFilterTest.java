package example.viewPanel.filter;

import example.*;
import example.button.Button;
import example.field.nested.CheckboxField;
import example.field.Field;
import example.field.nested.TextAreaField;
import example.field.nested.TextField;
import example.field.nested.DropdownField;
import example.window.Alert;
import example.window.Card;
import example.window.Window;
import io.qameta.allure.Step;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.untitled.phoenix.component.Component;
import org.untitled.phoenix.component.requirement.generic.Requirement;
import java.time.Duration;

@DisplayName("Фильтрация по текстовому полю")
public class TextFilterTest extends BaseTest {

    private static final Card card = (Card) Component.find(Card::new, Window.Requirements.isActive(true));
    private static final ViewPanel viewPanel = Component.find(ViewPanel::new);
    private static final Alert alert = Component.find(Alert::new);

    @Override
    protected @NotNull String getAddress() {
        return "https://autotests.gemsdev.ru/";
    }

    @BeforeEach
    @DisplayName("Подготовка тестовых данных")
    public void beforeEach() {
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
    @Step("Фильтровать по текстовому полю (Не пусто)")
    public void textFilterByNoEmpty() {
        var sort = viewPanel.findInside(ViewPanel.Header::new, ViewPanel.Header.Requirements.Equals.byName("Примечание")).openSort();
        sort.findInside(Menu.Item::new, Menu.Item.Requirements.Equals.byText("Фильтр")).hover();
        ((CheckboxField) sort.findInside(CheckboxField::new, Field.Requirements.Equals.byTitle("Не пусто"))).check();

        refreshTable();

        Component.should(viewPanel.findInside(ViewPanel.Item::new, ViewPanel.Item.Requirements.Equals.byValue("Примечание", "Не пусто")), Requirement.byAvailable(true), Duration.ofSeconds(60));
        Component.should(viewPanel.findInside(ViewPanel.Item::new, ViewPanel.Item.Requirements.Equals.byValue("Примечание", " ")), Requirement.byAvailable(false), Duration.ofSeconds(60));

        checkItems();
    }

    @Test
    @Step("Фильтровать по текстовому полю (Пусто)")
    public void textFilterByEmpty() {
        var sort = viewPanel.findInside(ViewPanel.Header::new, ViewPanel.Header.Requirements.Equals.byName("Примечание")).openSort();
        sort.findInside(Menu.Item::new, Menu.Item.Requirements.Equals.byText("Фильтр")).hover();
        ((CheckboxField) sort.findInside(CheckboxField::new, Field.Requirements.Equals.byTitle("Пусто"))).check();

        refreshTable();

        Component.should(viewPanel.findInside(ViewPanel.Item::new, ViewPanel.Item.Requirements.Equals.byValue("Примечание", "Не пусто")), Requirement.byAvailable(false), Duration.ofSeconds(60));
        Component.should(viewPanel.findInside(ViewPanel.Item::new, ViewPanel.Item.Requirements.Equals.byValue("Примечание", " ")), Requirement.byAvailable(true), Duration.ofSeconds(60));

        checkItems();
    }

    @Test
    @Step("Фильтровать по текстовому полю (Не содержит)")
    public void textFilterByNoContains() {
        var sort = viewPanel.findInside(ViewPanel.Header::new, ViewPanel.Header.Requirements.Equals.byName("Примечание")).openSort();
        sort.findInside(Menu.Item::new, Menu.Item.Requirements.Equals.byText("Фильтр")).hover();
        sort.findInside(TextField::new, Field.Requirements.Equals.byTitle("Не содержит")).toAction().click();
        sort.findInside(TextField::new, Field.Requirements.Equals.byTitle("Не содержит")).setValue("Не");

        refreshTable();

        Component.should(viewPanel.findInside(ViewPanel.Item::new, ViewPanel.Item.Requirements.Equals.byValue("Примечание", "Не пусто")), Requirement.byAvailable(false), Duration.ofSeconds(60));
        Component.should(viewPanel.findInside(ViewPanel.Item::new, ViewPanel.Item.Requirements.Equals.byValue("Примечание", " ")), Requirement.byAvailable(true), Duration.ofSeconds(60));

        checkItems();
    }

    @Test
    @Step("Фильтровать по текстовому полю (Содержит)")
    public void textFilterByContains() {
        var sort = viewPanel.findInside(ViewPanel.Header::new, ViewPanel.Header.Requirements.Equals.byName("Примечание")).openSort();
        sort.findInside(Menu.Item::new, Menu.Item.Requirements.Equals.byText("Фильтр")).hover();
        sort.findInside(TextField::new, Field.Requirements.Equals.byTitle("Содержит")).toAction().click();
        sort.findInside(TextField::new, Field.Requirements.Equals.byTitle("Содержит")).setValue("Не");

        refreshTable();

        Component.should(viewPanel.findInside(ViewPanel.Item::new, ViewPanel.Item.Requirements.Equals.byValue("Примечание", "Не пусто")), Requirement.byAvailable(true), Duration.ofSeconds(60));
        Component.should(viewPanel.findInside(ViewPanel.Item::new, ViewPanel.Item.Requirements.Equals.byValue("Примечание", " ")), Requirement.byAvailable(false), Duration.ofSeconds(60));

        checkItems();
    }

    @Test
    @Step("Фильтровать по текстовому полю (Не равно)")
    public void textFilterByNoEquals() {
        var sort = viewPanel.findInside(ViewPanel.Header::new, ViewPanel.Header.Requirements.Equals.byName("Примечание")).openSort();
        sort.findInside(Menu.Item::new, Menu.Item.Requirements.Equals.byText("Фильтр")).hover();
        sort.findInside(TextField::new, Field.Requirements.Equals.byTitle("Не равно")).toAction().click();
        sort.findInside(TextField::new, Field.Requirements.Equals.byTitle("Не равно")).setValue("Не пусто");

        refreshTable();

        Component.should(viewPanel.findInside(ViewPanel.Item::new, ViewPanel.Item.Requirements.Equals.byValue("Примечание", "Не пусто")), Requirement.byAvailable(false), Duration.ofSeconds(60));
        Component.should(viewPanel.findInside(ViewPanel.Item::new, ViewPanel.Item.Requirements.Equals.byValue("Примечание", " ")), Requirement.byAvailable(true), Duration.ofSeconds(60));

        checkItems();
    }

    @Test
    @Step("Фильтровать по текстовому полю (Равно)")
    public void textFilterByEquals() {
        var sort = viewPanel.findInside(ViewPanel.Header::new, ViewPanel.Header.Requirements.Equals.byName("Примечание")).openSort();
        sort.findInside(Menu.Item::new, Menu.Item.Requirements.Equals.byText("Фильтр")).hover();
        sort.findInside(TextField::new, Field.Requirements.Equals.byTitle("Равно")).toAction().click();
        sort.findInside(TextField::new, Field.Requirements.Equals.byTitle("Равно")).setValue("Не пусто");

        refreshTable();

        Component.should(viewPanel.findInside(ViewPanel.Item::new, ViewPanel.Item.Requirements.Equals.byValue("Примечание", "Не пусто")), Requirement.byAvailable(true), Duration.ofSeconds(60));
        Component.should(viewPanel.findInside(ViewPanel.Item::new, ViewPanel.Item.Requirements.Equals.byValue("Примечание", " ")), Requirement.byAvailable(false), Duration.ofSeconds(60));

        checkItems();
    }

    @AfterEach
    @DisplayName("Удаление тестовых данных")
    public void afterEach() {
        removeAllItem();
    }

    private static void refreshTable() {
        viewPanel.findInside(Button::new, Button.Requirements.Equals.byTip("Обновить таблицу")).click();
        Component.find(Spinner::new).wait(Duration.ofSeconds(60));
    }

    private static void checkItems() {
        viewPanel.findInside(Button::new, Button.Requirements.Equals.byTip("Фильтры")).showMenu().findInside(Menu.Item::new, Menu.Item.Requirements.Equals.byText("Сбросить все фильтры")).click();

        refreshTable();

        Component.should(viewPanel.findInside(ViewPanel.Item::new, ViewPanel.Item.Requirements.Equals.byValue("Примечание", "Не пусто")), Requirement.byAvailable(true), Duration.ofSeconds(60));
        Component.should(viewPanel.findInside(ViewPanel.Item::new, ViewPanel.Item.Requirements.Equals.byValue("Примечание", " ")), Requirement.byAvailable(true), Duration.ofSeconds(60));
    }

    private static void removeAllItem() {
        final var item = viewPanel.findInside(ViewPanel.Item::new);

        while (Component.has(item,  Requirement.byAvailable(true), Duration.ofSeconds(1))) {
            item.show(ViewPanel.Item.Option.CARD);

            card.findInside(Button::new, Button.Requirements.Equals.byText("Еще")).showMenu().findInside(Menu.Item::new, Menu.Item.Requirements.Equals.byText("Удалить объект")).click();
            alert.findInside(Button::new, Button.Requirements.Equals.byText("Удалить")).click();

            refreshTable();
        }
    }
}
