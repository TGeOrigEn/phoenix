package geometa.viewPanel.filter;

import common.nested.TAG;
import example.*;

import example.field.Field;
import example.field.nested.TextField;
import example.table.Header;
import example.table.ViewPanel;


import geometa.BaseGeometaTest;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.*;
import org.untitled.phoenix.component.Component;
import org.untitled.phoenix.component.requirement.generic.Requirement;

import java.time.Duration;

@Tag(TAG.GEOMETA.VIEW_PANEL)
@DisplayName("Фильтрация по числовому полю")
public class ViewPanelNumberFilterTest extends BaseGeometaTest {

    @Override
    protected @NotNull String[] initializeItems() {
        return new String[]{"Приморский край", "autotests", "presentationPanel"};
    }

    @BeforeEach
    public void beforeEach() {
        Component.find(NavigationPanel.Item::new, NavigationPanel.Item.Requirements.Equals.byName("Качество среды - параметры улиц")).open(NavigationPanel.Item.Option.TABLE);

        removeAllItem();
        createObject("0");
        createObject("10");
    }

    @Test
    @DisplayName("Фильтровать по числовому полю (МЕНЬШЕ)")
    public void numberFilterByLess() {
        var sort = openSort();

        sort.findInside(TextField::new, Field.DEFAULT_DESCRIPTION.copy(0), Field.Requirements.Equals.byPlaceholder("Введите число ...")).toAction().click();
        sort.findInside(TextField::new, Field.DEFAULT_DESCRIPTION.copy(0), Field.Requirements.Equals.byPlaceholder("Введите число ...")).setValue("10");

        spinner.wait(Duration.ofSeconds(10));

        Component.should(viewPanel.getItemBy(ViewPanel.Item.Requirements.Equals.byValue("Радиус поиска объектов торговли, м.", "0")), Requirement.isAvailable(true), Duration.ofSeconds(60));
        Component.should(viewPanel.getItemBy(ViewPanel.Item.Requirements.Equals.byValue("Радиус поиска объектов торговли, м.", "10")), Requirement.isAvailable(false), Duration.ofSeconds(60));

        checkItems();
    }

    @Test
    @DisplayName("Фильтровать по числовому полю (БОЛЬШЕ)")
    public void numberFilterByMore() {
        var sort = openSort();

        sort.findInside(TextField::new, Field.DEFAULT_DESCRIPTION.copy(1), Field.Requirements.Equals.byPlaceholder("Введите число ...")).toAction().click();
        sort.findInside(TextField::new, Field.DEFAULT_DESCRIPTION.copy(1), Field.Requirements.Equals.byPlaceholder("Введите число ...")).setValue("0");

        spinner.wait(Duration.ofSeconds(10));

        Component.should(viewPanel.getItemBy(ViewPanel.Item.Requirements.Equals.byValue("Радиус поиска объектов торговли, м.", "0")), Requirement.isAvailable(false), Duration.ofSeconds(60));
        Component.should(viewPanel.getItemBy(ViewPanel.Item.Requirements.Equals.byValue("Радиус поиска объектов торговли, м.", "10")), Requirement.isAvailable(true), Duration.ofSeconds(60));

        checkItems();
    }

    @Test
    @DisplayName("Фильтровать по числовому полю (РАВНО)")
    public void numberFilterByEquals() {
        var sort = openSort();

        sort.findInside(TextField::new, Field.DEFAULT_DESCRIPTION.copy(2), Field.Requirements.Equals.byPlaceholder("Введите число ...")).toAction().click();
        sort.findInside(TextField::new, Field.DEFAULT_DESCRIPTION.copy(2), Field.Requirements.Equals.byPlaceholder("Введите число ...")).setValue("0");

        spinner.wait(Duration.ofSeconds(10));

        Component.should(viewPanel.getItemBy(ViewPanel.Item.Requirements.Equals.byValue("Радиус поиска объектов торговли, м.", "0")), Requirement.isAvailable(true), Duration.ofSeconds(60));
        Component.should(viewPanel.getItemBy(ViewPanel.Item.Requirements.Equals.byValue("Радиус поиска объектов торговли, м.", "10")), Requirement.isAvailable(false), Duration.ofSeconds(60));

        checkItems();
    }

    @AfterEach
    public void afterEach() {
        removeAllItem();
    }

    private static void createObject(@NotNull String value) {
        viewPanel.createNewObject();

        card.findInside(TextField::new, Field.Requirements.Equals.byTitle("Радиус поиска объектов торговли, м.:")).setValue(value);
        card.save();
        card.close();

        viewPanel.refreshTable();

        Component.should(viewPanel.getItemBy(ViewPanel.Item.Requirements.Equals.byValue("Радиус поиска объектов торговли, м.", value)), Requirement.isAvailable(true), Duration.ofSeconds(60));
    }

    private static @NotNull Menu openSort() {
        var sort = viewPanel.findInside(Header::new, Header.Requirements.Equals.byName("Радиус поиска объектов торговли, м.")).openMenu();
        sort.findInside(Menu.Item::new, Menu.Item.Requirements.Equals.byText("Фильтр")).hover();
        return sort;
    }

    private static void checkItems() {
        viewPanel.resetAllFilters();

        Component.should(viewPanel.getItemBy(ViewPanel.Item.Requirements.Equals.byValue("Радиус поиска объектов торговли, м.", "0")), Requirement.isAvailable(true), Duration.ofSeconds(60));
        Component.should(viewPanel.getItemBy(ViewPanel.Item.Requirements.Equals.byValue("Радиус поиска объектов торговли, м.", "10")), Requirement.isAvailable(true), Duration.ofSeconds(60));
    }

    private static void removeAllItem() {
        while (Component.has(viewPanel.findInside(ViewPanel.Item::new),  Requirement.isAvailable(true), Duration.ofSeconds(1))) {
            viewPanel.findInside(ViewPanel.Item::new).select();
            viewPanel.deleteSelectedObjects();
            viewPanel.refreshTable();
        }
    }
}
