package geometa.viewPanel.filter;

import common.nested.TAG;
import example.Menu;
import example.NavigationPanel;
import example.button.Button;
import example.field.Field;
import example.field.nested.DropdownField;
import example.field.nested.TextField;
import example.table.Header;
import example.table.ViewPanel;
import geometa.BaseGeometaTest;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.*;
import org.untitled.phoenix.component.Component;
import org.untitled.phoenix.component.requirement.generic.Requirement;

import java.time.Duration;
import java.time.LocalDateTime;

@Tag(TAG.GEOMETA.VIEW_PANEL)
@DisplayName("Фильтрация по полю даты")
public class ViewPanelDateFilterTest extends BaseGeometaTest {

    private static final @NotNull String DATE_BEFORE = Date.getDate(LocalDateTime.now().minusDays(1), Date.Format.DD_MM_YYYY_DOT);
    private static final @NotNull String DATE_AFTER = Date.getDate(LocalDateTime.now().plusDays(1), Date.Format.DD_MM_YYYY_DOT);
    private static final @NotNull String DATE_NOW = Date.getDate(LocalDateTime.now(), Date.Format.DD_MM_YYYY_DOT);

    @Override
    protected @NotNull String[] initializeItems() {
        return new String[]{"Приморский край", "autotests", "presentationPanel"};
    }

    @BeforeEach
    public void beforeEach() {
        Component.find(NavigationPanel.Item::new, NavigationPanel.Item.Requirements.Equals.byName("Акт приемки ОКС")).open(NavigationPanel.Item.Option.TABLE);

        removeAllItem();
        createObject(DATE_NOW);
        createObject(DATE_AFTER);
        createObject(DATE_BEFORE);
    }

    @AfterEach
    public void afterEach() {
        removeAllItem();
    }

    @Test
    public void dateFilterByEquals() {
        var sort = openSort();

        sort.findInside(Menu.Item::new, Menu.Item.Requirements.Equals.byText("На дату")).hover();
        Component.find(Button::new, Button.Requirements.Equals.byText("Сегодня")).click();

        spinner.wait(Duration.ofSeconds(10));

        Component.should(viewPanel.getItemBy(ViewPanel.Item.Requirements.Equals.byValue("Дата", DATE_NOW)), Requirement.isAvailable(true), Duration.ofSeconds(60));
        Component.should(viewPanel.getItemBy(ViewPanel.Item.Requirements.Equals.byValue("Дата", DATE_AFTER)), Requirement.isAvailable(false), Duration.ofSeconds(60));
        Component.should(viewPanel.getItemBy(ViewPanel.Item.Requirements.Equals.byValue("Дата", DATE_BEFORE)), Requirement.isAvailable(false), Duration.ofSeconds(60));


        checkItems();
    }

    @Test
    public void dateFilterByMore() {
        var sort = openSort();

        sort.findInside(Menu.Item::new, Menu.Item.Requirements.Equals.byText("Не ранее")).hover();
        Component.find(Button::new, Button.Requirements.Equals.byText("Сегодня")).click();

        spinner.wait(Duration.ofSeconds(10));

        Component.should(viewPanel.getItemBy(ViewPanel.Item.Requirements.Equals.byValue("Дата", DATE_NOW)), Requirement.isAvailable(true), Duration.ofSeconds(60));
        Component.should(viewPanel.getItemBy(ViewPanel.Item.Requirements.Equals.byValue("Дата", DATE_AFTER)), Requirement.isAvailable(true), Duration.ofSeconds(60));
        Component.should(viewPanel.getItemBy(ViewPanel.Item.Requirements.Equals.byValue("Дата", DATE_BEFORE)), Requirement.isAvailable(false), Duration.ofSeconds(60));

        checkItems();
    }

    @Test
    public void dateFilterByLess() {
        var sort = openSort();

        sort.findInside(Menu.Item::new, Menu.Item.Requirements.Equals.byText("Не позднее")).hover();
        Component.find(Button::new, Button.Requirements.Equals.byText("Сегодня")).click();

        spinner.wait(Duration.ofSeconds(10));

        Component.should(viewPanel.getItemBy(ViewPanel.Item.Requirements.Equals.byValue("Дата", DATE_NOW)), Requirement.isAvailable(true), Duration.ofSeconds(60));
        Component.should(viewPanel.getItemBy(ViewPanel.Item.Requirements.Equals.byValue("Дата", DATE_AFTER)), Requirement.isAvailable(false), Duration.ofSeconds(60));
        Component.should(viewPanel.getItemBy(ViewPanel.Item.Requirements.Equals.byValue("Дата", DATE_BEFORE)), Requirement.isAvailable(true), Duration.ofSeconds(60));

        checkItems();
    }

    private static void createObject(@NotNull String value) {
        viewPanel.createNewObject();

        card.findInside(TextField::new, Field.Requirements.Equals.byTitle("Дата:")).setValue(value);
        card.findInside(DropdownField::new, Field.Requirements.Equals.byTitle("Земельный участок:")).setValue("Земельный участок №55555 ()");

        card.save();
        card.close();

        viewPanel.refreshTable();

        Component.should(viewPanel.getItemBy(ViewPanel.Item.Requirements.Equals.byValue("Дата", value)), Requirement.isAvailable(true), Duration.ofSeconds(60));
    }

    private static @NotNull Menu openSort() {
        var sort = viewPanel.findInside(Header::new, Header.Requirements.Equals.byName("Дата")).openMenu();
        sort.findInside(Menu.Item::new, Menu.Item.Requirements.Equals.byText("Фильтр")).hover();
        return sort;
    }

    private static void checkItems() {
        viewPanel.resetAllFilters();

        Component.should(viewPanel.getItemBy(ViewPanel.Item.Requirements.Equals.byValue("Дата", DATE_NOW)), Requirement.isAvailable(true), Duration.ofSeconds(60));
        Component.should(viewPanel.getItemBy(ViewPanel.Item.Requirements.Equals.byValue("Дата", DATE_AFTER)), Requirement.isAvailable(true), Duration.ofSeconds(60));
        Component.should(viewPanel.getItemBy(ViewPanel.Item.Requirements.Equals.byValue("Дата", DATE_BEFORE)), Requirement.isAvailable(true), Duration.ofSeconds(60));
    }

    private static void removeAllItem() {
        while (Component.has(viewPanel.findInside(ViewPanel.Item::new),  Requirement.isAvailable(true), Duration.ofSeconds(1))) {
            viewPanel.findInside(ViewPanel.Item::new).select();
            viewPanel.deleteSelectedObjects();
            viewPanel.refreshTable();
        }
    }
}
