package example.viewPanel.filter;

import example.*;
import example.button.Button;
import example.field.Field;
import example.field.TextField;
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
import org.untitled.phoenix.configuration.Configuration;

import java.time.Duration;

@DisplayName("Фильтрация по числовому полю")
public class NumberFilterTest extends BaseTest {

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
        Configuration.getWebDriver().navigate().to("https://autotests.gemsdev.ru/");

        Component.find(AuthorizationForm::new).logIn("gemsAdmin", "gemsAdmin123$");

        Component.find(NavigationPanel.Item::new, NavigationPanel.Item.Requirements.Equals.byName("Приморский край")).expand();
        Component.find(NavigationPanel.Item::new, NavigationPanel.Item.Requirements.Equals.byName("autotests")).expand();
        Component.find(NavigationPanel.Item::new, NavigationPanel.Item.Requirements.Equals.byName("presentationPanel")).expand();

        Component.find(NavigationPanel.Item::new, NavigationPanel.Item.Requirements.Equals.byName("Качество среды - параметры улиц")).open(NavigationPanel.Item.Option.TABLE);

        removeAllItem();

        viewPanel.findInside(Button::new, Button.Requirements.Equals.byTip("Создать новый объект")).click();

        card.findInside(TextField::new, Field.Requirements.Equals.byTitle("Радиус поиска объектов торговли, м.:")).setValue("0");
        card.findInside(Button::new, Button.Requirements.Equals.byTip("Сохранить")).click();
        card.close();

        viewPanel.findInside(Button::new, Button.Requirements.Equals.byTip("Обновить таблицу")).click();

        Component.should(viewPanel.findInside(ViewPanel.Item::new, ViewPanel.Item.Requirements.Equals.byValue("Радиус поиска объектов торговли, м.", "0")), Requirement.byAvailable(true), Duration.ofSeconds(60));

        viewPanel.findInside(Button::new, Button.Requirements.Equals.byTip("Создать новый объект")).click();

        card.findInside(TextField::new, Field.Requirements.Equals.byTitle("Радиус поиска объектов торговли, м.:")).setValue("10");
        card.findInside(Button::new, Button.Requirements.Equals.byTip("Сохранить")).click();
        card.close();

        refreshTable();

        Component.should(viewPanel.findInside(ViewPanel.Item::new, ViewPanel.Item.Requirements.Equals.byValue("Радиус поиска объектов торговли, м.", "10")), Requirement.byAvailable(true), Duration.ofSeconds(60));
    }

    @Test
    @Step("Фильтровать по числовому полю (Равно)")
    public void numberFilterByLess() {
        var sort = viewPanel.findInside(ViewPanel.Header::new, ViewPanel.Header.Requirements.Equals.byName("Радиус поиска объектов торговли, м.")).openSort();
        sort.findInside(Menu.Item::new, Menu.Item.Requirements.Equals.byText("Фильтр")).hover();
        sort.findInside(TextField::new, Field.DEFAULT_DESCRIPTION.copy(0), Field.Requirements.Equals.byPlaceholder("Введите число ...")).toAction().click();
        sort.findInside(TextField::new, Field.DEFAULT_DESCRIPTION.copy(0), Field.Requirements.Equals.byPlaceholder("Введите число ...")).setValue("10");

        refreshTable();

        Component.should(viewPanel.findInside(ViewPanel.Item::new, ViewPanel.Item.Requirements.Equals.byValue("Радиус поиска объектов торговли, м.", "0")), Requirement.byAvailable(true), Duration.ofSeconds(60));
        Component.should(viewPanel.findInside(ViewPanel.Item::new, ViewPanel.Item.Requirements.Equals.byValue("Радиус поиска объектов торговли, м.", "10")), Requirement.byAvailable(false), Duration.ofSeconds(60));

        checkItems();
    }

    @Test
    @Step("Фильтровать по числовому полю (Больше)")
    public void numberFilterByMore() {
        var sort = viewPanel.findInside(ViewPanel.Header::new, ViewPanel.Header.Requirements.Equals.byName("Радиус поиска объектов торговли, м.")).openSort();
        sort.findInside(Menu.Item::new, Menu.Item.Requirements.Equals.byText("Фильтр")).hover();
        sort.findInside(TextField::new, Field.DEFAULT_DESCRIPTION.copy(1), Field.Requirements.Equals.byPlaceholder("Введите число ...")).toAction().click();
        sort.findInside(TextField::new, Field.DEFAULT_DESCRIPTION.copy(1), Field.Requirements.Equals.byPlaceholder("Введите число ...")).setValue("0");

        refreshTable();

        Component.should(viewPanel.findInside(ViewPanel.Item::new, ViewPanel.Item.Requirements.Equals.byValue("Радиус поиска объектов торговли, м.", "0")), Requirement.byAvailable(false), Duration.ofSeconds(60));
        Component.should(viewPanel.findInside(ViewPanel.Item::new, ViewPanel.Item.Requirements.Equals.byValue("Радиус поиска объектов торговли, м.", "10")), Requirement.byAvailable(true), Duration.ofSeconds(60));

        checkItems();
    }

    @Test
    @Step("Фильтровать по числовому полю (Равно)")
    public void numberFilterByEquals() {
        var sort = viewPanel.findInside(ViewPanel.Header::new, ViewPanel.Header.Requirements.Equals.byName("Радиус поиска объектов торговли, м.")).openSort();
        sort.findInside(Menu.Item::new, Menu.Item.Requirements.Equals.byText("Фильтр")).hover();
        sort.findInside(TextField::new, Field.DEFAULT_DESCRIPTION.copy(2), Field.Requirements.Equals.byPlaceholder("Введите число ...")).toAction().click();
        sort.findInside(TextField::new, Field.DEFAULT_DESCRIPTION.copy(2), Field.Requirements.Equals.byPlaceholder("Введите число ...")).setValue("0");

        refreshTable();

        Component.should(viewPanel.findInside(ViewPanel.Item::new, ViewPanel.Item.Requirements.Equals.byValue("Радиус поиска объектов торговли, м.", "0")), Requirement.byAvailable(true), Duration.ofSeconds(60));
        Component.should(viewPanel.findInside(ViewPanel.Item::new, ViewPanel.Item.Requirements.Equals.byValue("Радиус поиска объектов торговли, м.", "10")), Requirement.byAvailable(false), Duration.ofSeconds(60));

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

        Component.should(viewPanel.findInside(ViewPanel.Item::new, ViewPanel.Item.Requirements.Equals.byValue("Радиус поиска объектов торговли, м.", "0")), Requirement.byAvailable(true), Duration.ofSeconds(60));
        Component.should(viewPanel.findInside(ViewPanel.Item::new, ViewPanel.Item.Requirements.Equals.byValue("Радиус поиска объектов торговли, м.", "10")), Requirement.byAvailable(true), Duration.ofSeconds(60));
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
