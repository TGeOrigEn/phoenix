package geometa;

import common.BaseTest;
import example.AuthorizationForm;
import example.NavigationPanel;
import example.Spinner;
import example.table.ViewPanel;
import example.window.Alert;
import example.window.Card;
import example.window.Window;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.untitled.phoenix.component.Component;

public abstract class BaseGeometaTest extends BaseTest {

    protected final @NotNull Alert alert = (Alert) Component.find(Alert::new, Window.Requirements.isActive(true));

    protected final @NotNull Card card = (Card) Component.find(Card::new, Window.Requirements.isActive(true));

    protected final @NotNull ViewPanel viewPanel = Component.find(ViewPanel::new);

    protected final @NotNull Spinner spinner = Component.find(Spinner::new);

    @Override
    protected @NotNull String initializeApplication() {
        return Application.GEOMETA;
    }

    protected abstract @NotNull String[] initializeItems();

    protected @NotNull User initializeUser() {
        return User.GEOMETA_USER;
    }

    @BeforeEach
    public void logInAndExpandItems() {
        Component.find(AuthorizationForm::new).logIn(initializeUser().getLogin(), initializeUser().getPassword());

        for (var folder : initializeItems())
            Component.find(NavigationPanel.Item::new, NavigationPanel.Item.Requirements.Equals.byName(folder)).expand();
    }
}
