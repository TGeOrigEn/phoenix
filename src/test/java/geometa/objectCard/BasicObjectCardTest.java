package geometa.objectCard;

import example.Menu;
import example.button.Button;
import example.field.Field;
import example.field.nested.SearchField;
import example.field.nested.TextField;
import example.table.ViewPanel;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.untitled.phoenix.component.Component;
import org.untitled.phoenix.component.requirement.generic.Requirement;

import java.time.Duration;
import java.util.UUID;

@DisplayName("Тест базовых возможностей карточки")
public class BasicObjectCardTest extends BaseObjectCardTest {

    @Override
    protected @NotNull String[] initializeItems() {
        return new String[]{"Приморский край", "autotests", "objectCard"};
    }

    @Override
    protected @NotNull String initializeTable() {
        return "Заявление";
    }

    @Test
    @DisplayName("Создание нового объекта")
    public void createNewObject() {
        card.findInside(TextField::new, Field.Requirements.Equals.byTitle("Номер:")).setValue(randomUUID);
        card.findInside(Button::new, Button.Requirements.Equals.byText("OK")).click();

        viewPanel.findInside(SearchField::new, Field.Requirements.Equals.byPlaceholder("Поиск в таблице")).setValue(randomUUID);

        final var item = viewPanel.findInside(ViewPanel.Item::new, ViewPanel.Item.Requirements.Equals.byValue("Номер", randomUUID));
        Component.should(item, Requirement.isAvailable(true), Duration.ofSeconds(60));
        item.select();

        viewPanel.findInside(Button::new, Button.Requirements.Equals.byText("Еще")).clickOnArrow().findInside(Menu.Item::new, Menu.Item.Requirements.Equals.byText("Удалить объект")).click();

        alert.findInside(Button::new, Button.Requirements.Equals.byText("Удалить")).click();

        spinner.wait(Duration.ofSeconds(60));
    }

    @Test
    @DisplayName("Отмена создания нового объекта")
    public void cancelCreationNewObject() {
        card.findInside(TextField::new, Field.Requirements.Equals.byTitle("Номер:")).setValue(randomUUID);
        card.findInside(Button::new, Button.Requirements.Equals.byText("Отмена")).click();

        alert.findInside(Button::new, Button.Requirements.Equals.byText("Нет")).click();

        viewPanel.findInside(SearchField::new, Field.Requirements.Equals.byPlaceholder("Поиск в таблице")).setValue(randomUUID);

        spinner.wait(Duration.ofSeconds(60));

        final var item = viewPanel.findInside(ViewPanel.Item::new, ViewPanel.Item.Requirements.Equals.byValue("Номер", randomUUID));

        Component.should(item, Requirement.isAvailable(false), Duration.ofSeconds(60));
    }

    @Test
    @DisplayName("Сохранение карточки объекта")
    public void saveObjectCard() {
        final var value = UUID.randomUUID().toString();

        card.findInside(TextField::new, Field.Requirements.Equals.byTitle("Номер:")).setValue(value);
        card.findInside(Button::new, Button.Requirements.Equals.byText("OK")).click();

        viewPanel.findInside(SearchField::new, Field.Requirements.Equals.byPlaceholder("Поиск в таблице")).setValue(value);
        viewPanel.findInside(ViewPanel.Item::new, ViewPanel.Item.Requirements.Equals.byValue("Номер", value)).show(ViewPanel.Item.Option.CARD);

        card.findInside(TextField::new, Field.Requirements.Equals.byTitle("Номер:")).setValue(randomUUID);
        card.findInside(Button::new, Button.Requirements.Equals.byTip("Сохранить")).click();

        spinner.wait(Duration.ofSeconds(60));

        card.close();

        viewPanel.findInside(SearchField::new, Field.Requirements.Equals.byPlaceholder("Поиск в таблице")).setValue(randomUUID);
        viewPanel.findInside(ViewPanel.Item::new, ViewPanel.Item.Requirements.Equals.byValue("Номер", randomUUID)).select();

        viewPanel.findInside(Button::new, Button.Requirements.Equals.byText("Еще")).clickOnArrow().findInside(Menu.Item::new, Menu.Item.Requirements.Equals.byText("Удалить объект")).click();

        alert.findInside(Button::new, Button.Requirements.Equals.byText("Удалить")).click();

        spinner.wait(Duration.ofSeconds(60));
    }

    @Test
    @DisplayName("Закрыть карточку через кнопку [X] - затем сохранить")
    public void closeObjectCardBySave() {
        card.findInside(TextField::new, Field.Requirements.Equals.byTitle("Номер:")).setValue(randomUUID);
        card.close();

        alert.findInside(Button::new, Button.Requirements.Equals.byText("Да")).click();

        spinner.wait(Duration.ofSeconds(60));

        Component.should(card, Requirement.isAvailable(true), Duration.ofSeconds(60));

        card.close();

        spinner.wait(Duration.ofSeconds(60));

        Component.should(card, Requirement.isAvailable(false), Duration.ofSeconds(60));

        viewPanel.findInside(SearchField::new, Field.Requirements.Equals.byPlaceholder("Поиск в таблице")).setValue(randomUUID);
        viewPanel.findInside(ViewPanel.Item::new, ViewPanel.Item.Requirements.Equals.byValue("Номер", randomUUID)).select();

        viewPanel.findInside(Button::new, Button.Requirements.Equals.byText("Еще")).clickOnArrow().findInside(Menu.Item::new, Menu.Item.Requirements.Equals.byText("Удалить объект")).click();

        alert.findInside(Button::new, Button.Requirements.Equals.byText("Удалить")).click();
    }

    @Test
    @DisplayName("Закрыть карточку через кнопку [X] - затем не сохранить")
    public void closeObjectCardByDoNotSave() {
        card.findInside(TextField::new, Field.Requirements.Equals.byTitle("Номер:")).setValue(randomUUID);
        card.close();

        alert.findInside(Button::new, Button.Requirements.Equals.byText("Нет")).click();

        Component.should(card, Requirement.isAvailable(false), Duration.ofSeconds(60));

        viewPanel.findInside(SearchField::new, Field.Requirements.Equals.byPlaceholder("Поиск в таблице")).setValue(randomUUID);
        final var item = viewPanel.findInside(ViewPanel.Item::new, ViewPanel.Item.Requirements.Equals.byValue("Номер", randomUUID));

        spinner.wait(Duration.ofSeconds(60));

        Component.should(item, Requirement.isAvailable(false), Duration.ofSeconds(60));
    }

    @Test
    @DisplayName("Отмена закрытия карточки")
    public void cancelClosingObjectCard() {
        card.findInside(TextField::new, Field.Requirements.Equals.byTitle("Номер:")).setValue(randomUUID);
        card.close();

        alert.findInside(Button::new, Button.Requirements.Equals.byText("Отмена")).click();

        Component.should(card, Requirement.isAvailable(true), Duration.ofSeconds(5), 0);
    }

    @Test
    @DisplayName("Некорректное значение поля карточки")
    public void incorrectFieldValue() {
        final var filed =  card.findInside(TextField::new, Field.Requirements.Equals.byTitle("Дата:"));
        filed.setValue("00.00.0000");

        card.findInside(TextField::new, Field.Requirements.Equals.byTitle("Номер:")).setValue(randomUUID);

        Component.should(filed, Field.Requirements.isCorrect(false), Duration.ofSeconds(30));
    }

    @Test
    @DisplayName("Сохранение карточки невозможно")
    public void objectCardCanNotBeSaved() {
        card.findInside(TextField::new, Field.Requirements.Equals.byTitle("Дата:")).setValue("00.00.0000");
        card.findInside(Button::new, Button.Requirements.Equals.byTip("Сохранить")).click();
        alert.findInside(Button::new, Button.Requirements.Equals.byText("OK")).click();
    }
}
