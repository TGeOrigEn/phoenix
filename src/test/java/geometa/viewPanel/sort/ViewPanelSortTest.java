package geometa.viewPanel.sort;

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
@DisplayName("Сортировать по полю даты")
public class ViewPanelSortTest extends BaseGeometaTest {

    private static final @NotNull String DATE_BEFORE = Date.getDate(LocalDateTime.now().minusDays(1), Date.Format.DD_MM_YYYY_DOT);
    private static final @NotNull String DATE_AFTER = Date.getDate(LocalDateTime.now().plusDays(1), Date.Format.DD_MM_YYYY_DOT);
    private static final @NotNull String DATE_NOW = Date.getDate(LocalDateTime.now(), Date.Format.DD_MM_YYYY_DOT);

    private final @NotNull ViewPanel.Item item_0 = viewPanel.findInside(ViewPanel.Item::new, ViewPanel.Item.DEFAULT_DESCRIPTION.copy(0));

    private final @NotNull ViewPanel.Item item_1 = viewPanel.findInside(ViewPanel.Item::new, ViewPanel.Item.DEFAULT_DESCRIPTION.copy(1));

    private final @NotNull ViewPanel.Item item_2 = viewPanel.findInside(ViewPanel.Item::new, ViewPanel.Item.DEFAULT_DESCRIPTION.copy(2));

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
    @DisplayName("Сортировать по полю даты, нажав на заголовок (БОЛЬШЕ)")
    public void sortHeaderByMore() {
        viewPanel.findInside(Header::new, Header.Requirements.Equals.byName("Дата")).toAction().click();

        spinner.wait(Duration.ofSeconds(10));

        Component.should(item_0, ViewPanel.Item.Requirements.Equals.byValue("Дата", DATE_BEFORE), Duration.ofSeconds(5));
        Component.should(item_1, ViewPanel.Item.Requirements.Equals.byValue("Дата", DATE_NOW), Duration.ofSeconds(5));
        Component.should(item_2, ViewPanel.Item.Requirements.Equals.byValue("Дата", DATE_AFTER), Duration.ofSeconds(5));
    }

    @Test
    @DisplayName("Сортировать по полю даты, нажав на заголовок (МЕНЬШЕ)")
    public void sortHeaderByLess() {
        final var header = viewPanel.findInside(Header::new, Header.Requirements.Equals.byName("Дата"));

        header.toAction().click();

        spinner.wait(Duration.ofSeconds(10));

        header.toAction().click();

        spinner.wait(Duration.ofSeconds(10));

        Component.should(item_0, ViewPanel.Item.Requirements.Equals.byValue("Дата", DATE_AFTER), Duration.ofSeconds(5));
        Component.should(item_1, ViewPanel.Item.Requirements.Equals.byValue("Дата", DATE_NOW), Duration.ofSeconds(5));
        Component.should(item_2, ViewPanel.Item.Requirements.Equals.byValue("Дата", DATE_BEFORE), Duration.ofSeconds(5));
    }

    @Test
    @DisplayName("Сортировать по полю даты, нажав в меню вариантов (МЕНЬШЕ)")
    public void sortMenuByLess() {
        viewPanel.findInside(Header::new, Header.Requirements.Equals.byName("Дата")).openMenu().findInside(Menu.Item::new, Menu.Item.Requirements.Equals.byText("Сортировать по убыванию")).click();

        spinner.wait(Duration.ofSeconds(10));

        Component.should(item_0, ViewPanel.Item.Requirements.Equals.byValue("Дата", DATE_AFTER), Duration.ofSeconds(5));
        Component.should(item_1, ViewPanel.Item.Requirements.Equals.byValue("Дата", DATE_NOW), Duration.ofSeconds(5));
        Component.should(item_2, ViewPanel.Item.Requirements.Equals.byValue("Дата", DATE_BEFORE), Duration.ofSeconds(5));
    }

    @Test
    @DisplayName("Сортировать по полю даты, нажав в меню вариантов (БОЛЬШЕ)")
    public void sortMenuByMore() {
        viewPanel.findInside(Header::new, Header.Requirements.Equals.byName("Дата")).openMenu().findInside(Menu.Item::new, Menu.Item.Requirements.Equals.byText("Сортировать по возрастанию")).click();

        Component.should(item_0, ViewPanel.Item.Requirements.Equals.byValue("Дата", DATE_BEFORE), Duration.ofSeconds(5));
        Component.should(item_1, ViewPanel.Item.Requirements.Equals.byValue("Дата", DATE_NOW), Duration.ofSeconds(5));
        Component.should(item_2, ViewPanel.Item.Requirements.Equals.byValue("Дата", DATE_AFTER), Duration.ofSeconds(5));
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

    private static void removeAllItem() {
        while (Component.has(viewPanel.findInside(ViewPanel.Item::new),  Requirement.isAvailable(true), Duration.ofSeconds(1))) {
            viewPanel.findInside(ViewPanel.Item::new).select();
            viewPanel.deleteSelectedObjects();
            viewPanel.refreshTable();
        }
    }
}
