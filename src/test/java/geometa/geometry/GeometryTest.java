package geometa.geometry;

import common.BaseTest;
import common.nested.TAG;
import example.Map;

import example.NavigationPanel;
import example.button.Button;
import example.field.Field;
import example.field.nested.DropdownField;

import example.field.nested.TextField;
import example.table.CoordinateTable;
import example.table.ViewPanel;
import org.gems.WebComponent;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Point;
import org.untitled.phoenix.component.Component;
import org.untitled.phoenix.component.Description;
import org.untitled.phoenix.component.requirement.generic.Requirement;

import java.io.File;
import java.time.Duration;

@Tag(TAG.GEOMETA.OBJECT_GEOMETRY)
@DisplayName("Создание геометрии на карте")
public class GeometryTest extends BaseGeometryTest {

    private static final @NotNull CoordinateTable coordinateTable = Component.find(CoordinateTable::new);

    private static final @NotNull File polygon;

    static {
        final var pathToCoordinates = BaseTest.class.getClassLoader().getResource("geometa/card/geometry/polygon.csv");

        if (pathToCoordinates == null) throw new NullPointerException("Вложения 'polygon' не существует.");

        polygon = new File(pathToCoordinates.getPath());
    }

    //

    @Test
    @DisplayName("Создать на карте полигон и удалить через панель инструментов")
    public void createPolygonThenDeleteHim() {
        createPolygonOnMap();

        Component.find(Button::new, Button.Requirements.Equals.byTip("Удалить объект")).click();
        Component.find(Button::new, Button.Requirements.Equals.byText("Удалить")).click();

        Component.should(Component.find(Map.Polygon::new), Requirement.isAvailable(false), Duration.ofSeconds(60));
    }

    @Test
    @DisplayName("Создать на карте полигон и удалить через мини-карточку")
    public void createPolygonThenDeleteHimFromMiniCard() {
        createPolygonOnMap();

        Component.find(Button::new, Button.Requirements.Equals.byTip("Информация об объекте")).click();

        map.toAction().click();

        Component.find(Button::new, Button.Requirements.Equals.byText("Показать карточку")).click();

        card.delete();

        Component.should(Component.find(Map.Polygon::new), Requirement.isAvailable(false), Duration.ofSeconds(60));
    }

    @Test
    @DisplayName("Создать на карте полигон и удалить через панель представления")
    public void createPolygonThenDeleteHimFromViewPanel() {
        createPolygonOnMap();

        Component.find(ViewPanel.Item::new, ViewPanel.Item.Requirements.Equals.byValue("Идентификатор", randomUUID)).select();

        viewPanel.deleteSelectedObjects();

        Component.should(Component.find(Map.Polygon::new), Requirement.isAvailable(false), Duration.ofSeconds(60));
    }

    //

    @Test
    @DisplayName("Создать на карте полигон с помощью таблицы и удалить через панель инструментов")
    public void createPolygonFromTableThenDeleteHim() {
        createPolygonOnMapFromTable();

        Component.find(Button::new, Button.Requirements.Equals.byTip("Удалить объект")).click();
        Component.find(Button::new, Button.Requirements.Equals.byText("Удалить")).click();

        Component.should(Component.find(Map.Polygon::new), Requirement.isAvailable(false), Duration.ofSeconds(60));
    }

    @Test
    @DisplayName("Создать на карте полигон с помощью таблицы и удалить через мини-карточку")
    public void createPolygonFromTableThenDeleteHimFromMiniCard() {
        createPolygonOnMapFromTable();

        Component.find(Button::new, Button.Requirements.Equals.byTip("Информация об объекте")).click();

        map.toAction().click();

        Component.find(Button::new, Button.Requirements.Equals.byText("Показать карточку")).click();

        card.delete();

        Component.should(Component.find(Map.Polygon::new), Requirement.isAvailable(false), Duration.ofSeconds(60));
    }

    @Test
    @DisplayName("Создать на карте полигон с помощью таблицы и удалить через панель представления")
    public void createPolygonFromTableThenDeleteHimFromViewPanel() {
        createPolygonOnMapFromTable();

        Component.find(ViewPanel.Item::new, ViewPanel.Item.Requirements.Equals.byValue("Идентификатор", randomUUID)).select();

        viewPanel.deleteSelectedObjects();

        Component.should(Component.find(Map.Polygon::new), Requirement.isAvailable(false), Duration.ofSeconds(60));
    }

    //

    @Test
    @DisplayName("Создать на карте линию и удалить через панель инструментов")
    public void createLineThenDeleteHim() {
        createLineOnMap();

        Component.find(Button::new, Button.Requirements.Equals.byTip("Удалить объект")).click();
        Component.find(Button::new, Button.Requirements.Equals.byText("Удалить")).click();

        Component.should(Component.find(Map.Polygon::new), Requirement.isAvailable(false), Duration.ofSeconds(60));
    }

    @Test
    @DisplayName("Создать на карте линию и удалить через мини-карточку")
    public void createLineThenDeleteHimFromMiniCard() {
        createLineOnMap();

        Component.find(Button::new, Button.Requirements.Equals.byTip("Информация об объекте")).click();

        map.toAction().click();

        Component.find(Button::new, Button.Requirements.Equals.byText("Показать карточку")).click();

        card.delete();

        Component.should(Component.find(Map.Polygon::new), Requirement.isAvailable(false), Duration.ofSeconds(60));
    }

    @Test
    @DisplayName("Создать на карте линию и удалить через панель представления")
    public void createLineThenDeleteHimFromViewPanel() {
        createPolygonOnMapFromTable();

        Component.find(ViewPanel.Item::new, ViewPanel.Item.Requirements.Equals.byValue("Идентификатор", randomUUID)).select();

        viewPanel.deleteSelectedObjects();

        Component.should(Component.find(Map.Polygon::new), Requirement.isAvailable(false), Duration.ofSeconds(60));
    }

    //

    @Test
    @DisplayName("Создать на карте маркер и удалить через панель инструментов")
    public void createMarkerThenDeleteHim() {
        createMarkerOnMap();

        Component.find(Button::new, Button.Requirements.Equals.byTip("Удалить объект")).click();
        Component.find(Button::new, Button.Requirements.Equals.byText("Удалить")).click();

        Component.should(Component.find(Map.Polygon::new), Requirement.isAvailable(false), Duration.ofSeconds(60));
    }

    @Test
    @DisplayName("Создать на карте маркер и удалить через панель представления")
    public void createMarkerThenDeleteHimFromViewPanel() {
        createMarkerOnMap();

        Component.find(ViewPanel.Item::new, ViewPanel.Item.Requirements.Equals.byValue("Идентификатор", randomUUID)).select();

        viewPanel.deleteSelectedObjects();

        Component.should(Component.find(Map.Polygon::new), Requirement.isAvailable(false), Duration.ofSeconds(60));
    }

    //

    @Test
    @DisplayName("Импортировать полигон и удалить через панель инструментов")
    public void importPolygonThenDeleteHim() {
        importPolygon();

        Component.find(Button::new, Button.Requirements.Equals.byTip("Удалить объект")).click();
        Component.find(Button::new, Button.Requirements.Equals.byText("Удалить")).click();

        Component.should(Component.find(Map.Polygon::new), Requirement.isAvailable(false), Duration.ofSeconds(60));
    }

    @Test
    @DisplayName("Импортировать полигон и удалить через мини-карточку")
    public void importPolygonThenDeleteHimFromMiniCard() {
        importPolygon();

        Component.find(Button::new, Button.Requirements.Equals.byTip("Информация об объекте")).click();

        map.toAction().click();

        Component.find(Button::new, Button.Requirements.Equals.byText("Показать карточку")).click();

        card.delete();

        Component.should(Component.find(Map.Polygon::new), Requirement.isAvailable(false), Duration.ofSeconds(60));
    }

    @Test
    @DisplayName("Импортировать полигон и удалить через панель представления")
    public void importPolygonThenDeleteHimFromViewPanel() {
        importPolygon();

        Component.find(ViewPanel.Item::new, ViewPanel.Item.Requirements.Equals.byValue("Идентификатор", randomUUID)).select();

        viewPanel.deleteSelectedObjects();

        Component.should(Component.find(Map.Polygon::new), Requirement.isAvailable(false), Duration.ofSeconds(60));
    }

    //

    private void createPolygonOnMapFromTable() {
        setSettings();

        Component.find(Button::new, Button.Requirements.Equals.byTip("Полигон")).click();

        coordinateTable.addPart();

        coordinateTable.findInside(CoordinateTable.Item::new, CoordinateTable.Item.DEFAULT_DESCRIPTION.copy(0)).setX("14684470.3779");
        coordinateTable.findInside(CoordinateTable.Item::new, CoordinateTable.Item.DEFAULT_DESCRIPTION.copy(0)).setY("5360452.3566");

        coordinateTable.findInside(CoordinateTable.Item::new, CoordinateTable.Item.DEFAULT_DESCRIPTION.copy(0)).add();

        coordinateTable.findInside(CoordinateTable.Item::new, CoordinateTable.Item.DEFAULT_DESCRIPTION.copy(1)).setX("14688292.2293");
        coordinateTable.findInside(CoordinateTable.Item::new, CoordinateTable.Item.DEFAULT_DESCRIPTION.copy(1)).setY("5364274.2080");

        coordinateTable.findInside(CoordinateTable.Item::new, CoordinateTable.Item.DEFAULT_DESCRIPTION.copy(1)).add();

        alert.findInside(Button::new, Button.Requirements.Equals.byText("Да")).click();

        coordinateTable.findInside(CoordinateTable.Item::new, CoordinateTable.Item.DEFAULT_DESCRIPTION.copy(2)).setX("14692114.0808");
        coordinateTable.findInside(CoordinateTable.Item::new, CoordinateTable.Item.DEFAULT_DESCRIPTION.copy(2)).setY("5360452.3566");

        coordinateTable.findInside(CoordinateTable.Item::new, CoordinateTable.Item.DEFAULT_DESCRIPTION.copy(2)).add();

        coordinateTable.findInside(CoordinateTable.Item::new, CoordinateTable.Item.DEFAULT_DESCRIPTION.copy(3)).setX("14688292.2293");
        coordinateTable.findInside(CoordinateTable.Item::new, CoordinateTable.Item.DEFAULT_DESCRIPTION.copy(3)).setY("5356630.5052");

        coordinateTable.findInside(CoordinateTable.Item::new, CoordinateTable.Item.DEFAULT_DESCRIPTION.copy(3)).remove();

        createObject();

        map.toAction().click();

        Component.should(Component.find(Map.Polygon::new), Requirement.isAvailable(true), Duration.ofSeconds(60));

        Component.should(Component.find(Map.Point::new), Requirement.isAvailable(false), Duration.ofSeconds(1), 0);

        Component.find(Button::new, Button.Requirements.Equals.byTip("Редактировать геометрию")).click();

        Component.should(Component.find(Map.Point::new, Map.Point.DEFAULT_DESCRIPTION.copy(5)), Requirement.isAvailable(true), Duration.ofSeconds(60));

        Component.find(Button::new, Button.Requirements.Equals.byText("Отмена")).click();

        openTable();
    }

    private void createPolygonOnMap() {
        setSettings();

        Component.find(Button::new, Button.Requirements.Equals.byTip("Полигон")).click();

        map.createPolygon(new Point(100, 0), new Point(100, 100), new Point(0, 100));

        createObject();

        map.toAction().click();

        Component.should(Component.find(Map.Polygon::new), Requirement.isAvailable(true), Duration.ofSeconds(60));

        Component.should(Component.find(Map.Point::new), Requirement.isAvailable(false), Duration.ofSeconds(1), 0);

        Component.find(Button::new, Button.Requirements.Equals.byTip("Редактировать геометрию")).click();

        Component.should(Component.find(Map.Point::new, Map.Point.DEFAULT_DESCRIPTION.copy(7)), Requirement.isAvailable(true), Duration.ofSeconds(60));

        Component.find(Button::new, Button.Requirements.Equals.byText("Отмена")).click();

        openTable();
    }

    private void createMarkerOnMap() {
        setSettings();

        Component.find(Button::new, Button.Requirements.Equals.byTip("Метка")).click();

        map.toAction().click();

        createObject();

        openTable();
    }

    private void createLineOnMap() {
        setSettings();

        Component.find(Button::new, Button.Requirements.Equals.byTip("Линия")).click();

        map.createLine(new Point(100, 0));

        createObject();

        map.toAction().click();

        Component.should(Component.find(Map.Polygon::new), Requirement.isAvailable(true), Duration.ofSeconds(60));

        Component.should(Component.find(Map.Point::new), Requirement.isAvailable(false), Duration.ofSeconds(1), 0);

        Component.find(Button::new, Button.Requirements.Equals.byTip("Редактировать геометрию")).click();

        Component.should(Component.find(Map.Point::new, Map.Point.DEFAULT_DESCRIPTION.copy(2)), Requirement.isAvailable(true), Duration.ofSeconds(60));

        Component.find(Button::new, Button.Requirements.Equals.byText("Отмена")).click();

        openTable();
    }

    private void importPolygon() {
        Component.find(Button::new, Button.Requirements.Equals.byTip("Создать геометрию")).click();
        Component.find(() -> new WebComponent(new Description(By.cssSelector("input[type='file']"), "Ввод для файла"))).toAction().sendKeys(polygon.toPath().toAbsolutePath().toString());
        createObject();

        map.toAction().click();

        Component.should(Component.find(Map.Polygon::new), Requirement.isAvailable(true), Duration.ofSeconds(60));

        Component.should(Component.find(Map.Point::new), Requirement.isAvailable(false), Duration.ofSeconds(1), 0);

        Component.find(Button::new, Button.Requirements.Equals.byTip("Редактировать геометрию")).click();

        Component.should(Component.find(Map.Point::new, Map.Point.DEFAULT_DESCRIPTION.copy(5)), Requirement.isAvailable(true), Duration.ofSeconds(60));

        Component.find(Button::new, Button.Requirements.Equals.byText("Отмена")).click();

        openTable();
    }

    private void createObject() {
        Component.find(Button::new, Button.Requirements.Equals.byText("Сохранить")).click();

        Component.should(card, Requirement.isAvailable(true), Duration.ofSeconds(60));

        card.findInside(TextField::new, Field.Requirements.Equals.byTitle("Идентификатор:")).setValue(randomUUID);
        card.save();
        card.close();
    }

    private void setSettings() {
        Component.find(Button::new, Button.Requirements.Equals.byTip("Создать геометрию")).click();

        Component.find(DropdownField::new, Field.Requirements.Equals.byTitle("Тип нового объекта:")).setValue("Муниципальный район (СТП ОП)");
        Component.find(DropdownField::new, Field.Requirements.Equals.byTitle("Система координат:")).setValue("3857");
    }

    private void openTable() {
        Component.find(NavigationPanel.Item::new, NavigationPanel.Item.Requirements.Equals.byName("Базовая карта")).expand();
        Component.find(NavigationPanel.Item::new, NavigationPanel.Item.Requirements.Equals.byName("Муниципальный район (СТП ОП)")).open(NavigationPanel.Item.Option.TABLE);

        viewPanel.getSearchField().setValue(randomUUID);

        Component.find(ViewPanel.Item::new, ViewPanel.Item.Requirements.Equals.byValue("Идентификатор", randomUUID)).show(ViewPanel.Item.Option.MAP);

        Component.should(Component.find(Map.Polygon::new), Requirement.isAvailable(true), Duration.ofSeconds(60));
    }
}
