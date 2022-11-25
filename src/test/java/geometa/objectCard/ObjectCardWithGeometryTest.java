package geometa.objectCard;

import common.nested.TAG;
import example.Map;
import example.button.Button;
import example.field.Field;
import example.field.nested.TextField;
import example.table.ViewPanel;
import example.window.MapWindow;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Point;
import org.untitled.phoenix.component.Component;
import org.untitled.phoenix.component.requirement.generic.Requirement;

import java.time.Duration;

@Tag(TAG.GEOMETA.OBJECT_CARD)
@DisplayName("Работа с геометрией в карточке объекта")
public class ObjectCardWithGeometryTest extends BaseObjectCardTest {

    private static final @NotNull MapWindow mapWindow = Component.find(MapWindow::new);

    private static final @NotNull Map map = Component.find(Map::new);

    @Override
    protected @NotNull String[] initializeItems() {
        return new String[]{"Приморский край", "autotests", "objectCard"};
    }

    @Override
    protected @NotNull String initializeTable() {
        return "Муниципальный район (СТП ОП)";
    }

    @Test
    @DisplayName("Задать геометрию объекту")
    public void createObjectAndSetGeometry() {
        card.findInside(TextField::new, Field.Requirements.Equals.byTitle("Идентификатор:")).setValue(randomUUID);
        card.save();

        card.setGeometry();
        map.toAction().click();

        Component.should(map.findInside(Map.Polygon::new), Requirement.isAvailable(true), Duration.ofSeconds(60));

        mapWindow.save();
        card.save();
        card.close();

        viewPanel.getSearchField().setValue(randomUUID);

        viewPanel.getItemBy(ViewPanel.Item.Requirements.Equals.byValue("Идентификатор", randomUUID)).show(ViewPanel.Item.Option.MAP);

        Component.should(map.findInside(Map.Polygon::new), Requirement.isAvailable(true), Duration.ofSeconds(60));

        viewPanel.deleteSelectedObjects();
    }

    @Test
    @DisplayName("Задать геометрию для объекта")
    public void createObjectAndCreateGeometry() {
        card.findInside(TextField::new, Field.Requirements.Equals.byTitle("Идентификатор:")).setValue(randomUUID);
        card.save();

        card.createGeometry();
        Component.find(Button::new, Button.Requirements.Equals.byTip("Полигон")).click();

        map.createPolygon(
                new Point(100, 0),
                new Point(100, 100),
                new Point(0, 100)
        );

        Component.should(map.findInside(Map.Polygon::new), Requirement.isAvailable(true), Duration.ofSeconds(60));

        Component.find(Button::new, Button.Requirements.Equals.byText("Сохранить")).click();

        card.save();
        card.close();

        viewPanel.getSearchField().setValue(randomUUID);

        viewPanel.getItemBy(ViewPanel.Item.Requirements.Equals.byValue("Идентификатор", randomUUID)).show(ViewPanel.Item.Option.MAP);

        Component.should(map.findInside(Map.Polygon::new), Requirement.isAvailable(true), Duration.ofSeconds(60));

        viewPanel.deleteSelectedObjects();

        Component.should(map.findInside(Map.Polygon::new), Requirement.isAvailable(false), Duration.ofSeconds(60));
    }
}
