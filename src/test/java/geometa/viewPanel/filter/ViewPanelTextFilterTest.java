package geometa.viewPanel.filter;

import common.nested.TAG;
import example.*;
import example.field.nested.CheckboxField;
import example.field.Field;
import example.field.nested.TextField;
import example.field.nested.DropdownField;
import example.table.Header;
import example.table.ViewPanel;
import geometa.BaseGeometaTest;
import io.qameta.allure.Step;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.*;
import org.untitled.phoenix.component.Component;
import org.untitled.phoenix.component.requirement.generic.Requirement;
import java.time.Duration;

@Tag(TAG.GEOMETA.VIEW_PANEL)
@DisplayName("Фильтрация по текстовому полю")
public class ViewPanelTextFilterTest extends BaseGeometaTest {

    @Override
    protected @NotNull String[] initializeItems() {
        return new String[]{"Приморский край", "autotests", "presentationPanel"};
    }

    @BeforeEach
    public void beforeEach() {
        Component.find(NavigationPanel.Item::new, NavigationPanel.Item.Requirements.Equals.byName("Растительность")).open(NavigationPanel.Item.Option.TABLE);

        removeAllItem();
        createObject("Бугор", "Не пусто");
        createObject("Яма", "");
    }

    @Test
    @Step("Фильтровать по текстовому полю (НЕ ПУСТО)")
    public void textFilterByNoEmpty() {
        var sort = openSort();

        ((CheckboxField) sort.findInside(CheckboxField::new, Field.Requirements.Equals.byTitle("Не пусто"))).check();

        spinner.wait(Duration.ofSeconds(10));

        Component.should(viewPanel.getItemBy(ViewPanel.Item.Requirements.Equals.byValue("Примечание", "Не пусто")), Requirement.isAvailable(true), Duration.ofSeconds(60));
        Component.should(viewPanel.getItemBy(ViewPanel.Item.Requirements.Equals.byValue("Примечание", " ")), Requirement.isAvailable(false), Duration.ofSeconds(60));

        checkItems();
    }

    @Test
    @Step("Фильтровать по текстовому полю (ПУСТО)")
    public void textFilterByEmpty() {
        var sort = openSort();

        ((CheckboxField) sort.findInside(CheckboxField::new, Field.Requirements.Equals.byTitle("Пусто"))).check();

        spinner.wait(Duration.ofSeconds(10));

        Component.should(viewPanel.getItemBy(ViewPanel.Item.Requirements.Equals.byValue("Примечание", "Не пусто")), Requirement.isAvailable(false), Duration.ofSeconds(60));
        Component.should(viewPanel.getItemBy(ViewPanel.Item.Requirements.Equals.byValue("Примечание", " ")), Requirement.isAvailable(true), Duration.ofSeconds(60));

        checkItems();
    }

    @Test
    @Step("Фильтровать по текстовому полю (НЕ СОДЕРЖИТ)")
    public void textFilterByNoContains() {
        var sort = openSort();

        sort.findInside(TextField::new, Field.Requirements.Equals.byTitle("Не содержит")).toAction().click();
        sort.findInside(TextField::new, Field.Requirements.Equals.byTitle("Не содержит")).setValue("Не");

        spinner.wait(Duration.ofSeconds(10));

        Component.should(viewPanel.getItemBy(ViewPanel.Item.Requirements.Equals.byValue("Примечание", "Не пусто")), Requirement.isAvailable(false), Duration.ofSeconds(60));
        Component.should(viewPanel.getItemBy(ViewPanel.Item.Requirements.Equals.byValue("Примечание", " ")), Requirement.isAvailable(true), Duration.ofSeconds(60));

        checkItems();
    }

    @Test
    @Step("Фильтровать по текстовому полю (СОДЕРЖИТ)")
    public void textFilterByContains() {
        var sort = openSort();

        sort.findInside(TextField::new, Field.Requirements.Equals.byTitle("Содержит")).toAction().click();
        sort.findInside(TextField::new, Field.Requirements.Equals.byTitle("Содержит")).setValue("Не");

        spinner.wait(Duration.ofSeconds(10));

        Component.should(viewPanel.getItemBy(ViewPanel.Item.Requirements.Equals.byValue("Примечание", "Не пусто")), Requirement.isAvailable(true), Duration.ofSeconds(60));
        Component.should(viewPanel.getItemBy(ViewPanel.Item.Requirements.Equals.byValue("Примечание", " ")), Requirement.isAvailable(false), Duration.ofSeconds(60));

        checkItems();
    }

    @Test
    @Step("Фильтровать по текстовому полю (НЕ РАВНО)")
    public void textFilterByNoEquals() {
        var sort = openSort();

        sort.findInside(TextField::new, Field.Requirements.Equals.byTitle("Не равно")).toAction().click();
        sort.findInside(TextField::new, Field.Requirements.Equals.byTitle("Не равно")).setValue("Не пусто");

        spinner.wait(Duration.ofSeconds(10));

        Component.should(viewPanel.getItemBy(ViewPanel.Item.Requirements.Equals.byValue("Примечание", "Не пусто")), Requirement.isAvailable(false), Duration.ofSeconds(60));
        Component.should(viewPanel.getItemBy(ViewPanel.Item.Requirements.Equals.byValue("Примечание", " ")), Requirement.isAvailable(true), Duration.ofSeconds(60));

        checkItems();
    }

    @Test
    @Step("Фильтровать по текстовому полю (РАВНО)")
    public void textFilterByEquals() {
        var sort = openSort();

        sort.findInside(TextField::new, Field.Requirements.Equals.byTitle("Равно")).toAction().click();
        sort.findInside(TextField::new, Field.Requirements.Equals.byTitle("Равно")).setValue("Не пусто");

        spinner.wait(Duration.ofSeconds(10));

        Component.should(viewPanel.getItemBy(ViewPanel.Item.Requirements.Equals.byValue("Примечание", "Не пусто")), Requirement.isAvailable(true), Duration.ofSeconds(60));
        Component.should(viewPanel.getItemBy(ViewPanel.Item.Requirements.Equals.byValue("Примечание", " ")), Requirement.isAvailable(false), Duration.ofSeconds(60));

        checkItems();
    }

    @AfterEach
    public void afterEach() {
        removeAllItem();
    }

    private static void createObject(@NotNull String value, @NotNull String note) {
        viewPanel.createNewObject();

        card.findInside(DropdownField::new, Field.Requirements.Equals.byTitle("Вид объекта:")).setValue(value);
        card.findInside(TextField::new, Field.Requirements.Equals.byTitle("Примечание:")).setValue(note);

        card.save();
        card.close();

        viewPanel.refreshTable();

        Component.should(viewPanel.getItemBy(ViewPanel.Item.Requirements.Equals.byValue("Вид объекта", value)), Requirement.isAvailable(true), Duration.ofSeconds(60));
    }

    private static @NotNull Menu openSort() {
        var sort = viewPanel.findInside(Header::new, Header.Requirements.Equals.byName("Примечание")).openMenu();
        sort.findInside(Menu.Item::new, Menu.Item.Requirements.Equals.byText("Фильтр")).hover();
        return sort;
    }

    private static void checkItems() {
        viewPanel.resetAllFilters();

        Component.should(viewPanel.getItemBy(ViewPanel.Item.Requirements.Equals.byValue("Вид объекта", "Яма")), Requirement.isAvailable(true), Duration.ofSeconds(60));
        Component.should(viewPanel.getItemBy(ViewPanel.Item.Requirements.Equals.byValue("Вид объекта", "Бугор")), Requirement.isAvailable(true), Duration.ofSeconds(60));
    }

    private static void removeAllItem() {
        while (Component.has(viewPanel.findInside(ViewPanel.Item::new),  Requirement.isAvailable(true), Duration.ofSeconds(1))) {
            viewPanel.findInside(ViewPanel.Item::new).select();
            viewPanel.deleteSelectedObjects();
            viewPanel.refreshTable();
        }
    }
}
