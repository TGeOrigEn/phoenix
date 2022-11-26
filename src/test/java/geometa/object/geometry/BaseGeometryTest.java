package geometa.object.geometry;

import example.Menu;
import example.NavigationPanel;
import geometa.BaseGeometaTest;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.untitled.phoenix.component.Component;

import java.time.Duration;

public abstract class BaseGeometryTest extends BaseGeometaTest {

    @Override
    protected @NotNull String[] initializeItems() {
        return new String[] { "Приморский край", "autotests", "mapPanel" };
    }

    @BeforeEach
    public void changeMap() {
        Component.find(NavigationPanel.Item::new, NavigationPanel.Item.Requirements.Equals.byName("Базовая карта"))
                .more().getItemBy(Menu.Item.Requirements.Equals.byText("Сменить карту")).click();

        map.wait(Duration.ofSeconds(1));
    }
}
