package geometa.objectCard;

import example.NavigationPanel;
import example.button.Button;

import geometa.BaseGeometaTest;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.untitled.phoenix.component.Component;

public abstract class BaseObjectCardTest extends BaseGeometaTest {

    protected abstract @NotNull String initializeTable();

    @BeforeEach
    public void createNewObjectFromViewPanel() {
        Component.find(NavigationPanel.Item::new, NavigationPanel.Item.Requirements.Equals.byName(initializeTable())).open(NavigationPanel.Item.Option.TABLE);
        viewPanel.findInside(Button::new, Button.Requirements.Equals.byTip("Создать новый объект")).click();
    }
}
