package geometa.object.card;

import common.nested.TAG;
import example.Menu;
import example.field.Field;
import example.field.nested.DropdownField;
import example.field.nested.TextAreaField;
import example.field.nested.TextField;
import example.table.ViewPanel;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.untitled.phoenix.component.Component;

import java.time.Duration;

@Tag(TAG.GEOMETA.OBJECT_CARD)
@DisplayName("Связи объектов в карточке объекта")
public class ObjectCardRelationshipTest extends BaseObjectCardTest {

    @Override
    protected @NotNull String[] initializeItems() {
        return new String[]{"Приморский край", "autotests", "objectCard"};
    }

    @Override
    protected @NotNull String initializeTable() {
        return "Заявление";
    }

    @Test
    @DisplayName("Создать объект и установить с ним связь в карточке другого объекта")
    public void createRelationship() {
        final var dropdownField = ((DropdownField) card.findInside(DropdownField::new, Field.Requirements.Equals.byTitle("Заявитель:")));
        dropdownField.addNewObjectBy(Menu.Item.Requirements.Equals.byText("Физическое лицо"));

        card.findInside(TextAreaField::new, Field.Requirements.Equals.byTitle("Фамилия:")).setValue(randomUUID);
        card.save();
        card.close();

        Component.should(dropdownField, Field.Requirements.Equals.byValue(randomUUID), Duration.ofSeconds(10));

        card.save();
        card.close();

        spinner.wait(Duration.ofSeconds(30));

        viewPanel.getSearchField().setValue(randomUUID);
        viewPanel.getItemBy(ViewPanel.Item.Requirements.Equals.byValue("Заявитель", randomUUID)).show(ViewPanel.Item.Option.CARD);

        Component.should(dropdownField, Field.Requirements.Equals.byValue(randomUUID), Duration.ofSeconds(5));
        dropdownField.getItemBy(DropdownField.Item.Requirements.Equals.byText(randomUUID)).open();

        card.delete();
        card.delete();
    }

    @Test
    @DisplayName("Найти объект и установить с ним связь в карточке другого объекта")
    public void CreateRelationshipAndFind() {
        final var dropdownField = ((DropdownField) card.findInside(DropdownField::new, Field.Requirements.Equals.byTitle("Заявитель:")));
        dropdownField.addNewObjectBy(Menu.Item.Requirements.Equals.byText("Физическое лицо"));

        card.findInside(TextAreaField::new, Field.Requirements.Equals.byTitle("Фамилия:")).setValue(randomUUID);
        card.save();
        card.close();

        Component.should(dropdownField, Field.Requirements.Equals.byValue(randomUUID), Duration.ofSeconds(10));
        dropdownField.clear();
        dropdownField.sendKeys(randomUUID);
        dropdownField.setValue(randomUUID);

        card.save();
        card.close();

        spinner.wait(Duration.ofSeconds(30));

        viewPanel.getSearchField().setValue(randomUUID);
        viewPanel.getItemBy(ViewPanel.Item.Requirements.Equals.byValue("Заявитель", randomUUID)).show(ViewPanel.Item.Option.CARD);

        Component.should(dropdownField, Field.Requirements.Equals.byValue(randomUUID), Duration.ofSeconds(5));

        dropdownField.getItemBy(DropdownField.Item.Requirements.Equals.byText(randomUUID)).open();
        card.delete();
        card.delete();
    }

    @Test
    @DisplayName("Создать объект и удалить с ним связь в карточке другого объекта")
    public void removeRelationship() {
        card.findInside(TextField::new, Field.Requirements.Equals.byTitle("Номер:")).setValue(randomUUID);
        final var dropdownField = ((DropdownField) card.findInside(DropdownField::new, Field.Requirements.Equals.byTitle("Заявитель:")));
        dropdownField.addNewObjectBy(Menu.Item.Requirements.Equals.byText("Физическое лицо"));

        card.findInside(TextAreaField::new, Field.Requirements.Equals.byTitle("Фамилия:")).setValue(randomUUID);
        card.save();
        card.close();

        Component.should(dropdownField, Field.Requirements.Equals.byValue(randomUUID), Duration.ofSeconds(10));

        card.save();
        card.close();

        spinner.wait(Duration.ofSeconds(30));

        viewPanel.getSearchField().setValue(randomUUID);
        viewPanel.getItemBy(ViewPanel.Item.Requirements.Equals.byValue("Заявитель", randomUUID)).show(ViewPanel.Item.Option.CARD);

        Component.should(dropdownField, Field.Requirements.Equals.byValue(randomUUID), Duration.ofSeconds(5));

        dropdownField.clear();

        card.save();
        card.close();

        spinner.wait(Duration.ofSeconds(30));

        viewPanel.getSearchField().setValue(randomUUID);
        viewPanel.getItemBy(ViewPanel.Item.Requirements.Equals.byValue("Номер", randomUUID)).show(ViewPanel.Item.Option.CARD);

        Component.should(dropdownField, Field.Requirements.Equals.byValue(""), Duration.ofSeconds(5));

        card.delete();
    }

    @Test
    @DisplayName("Создать объект, установить с ним связь в карточке другого объекта и проверить, что объект корретный")
    public void createRelationshipAndOpenItem() {
        final var dropdownField = ((DropdownField) card.findInside(DropdownField::new, Field.Requirements.Equals.byTitle("Заявитель:")));
        final var textAreaField = card.findInside(TextAreaField::new, Field.Requirements.Equals.byTitle("Фамилия:"));

        dropdownField.addNewObjectBy(Menu.Item.Requirements.Equals.byText("Физическое лицо"));

        textAreaField.setValue(randomUUID);
        card.save();
        card.close();

        Component.should(dropdownField, Field.Requirements.Equals.byValue(randomUUID), Duration.ofSeconds(10));

        card.save();
        card.close();

        spinner.wait(Duration.ofSeconds(30));

        viewPanel.getSearchField().setValue(randomUUID);
        viewPanel.getItemBy(ViewPanel.Item.Requirements.Equals.byValue("Заявитель", randomUUID)).show(ViewPanel.Item.Option.CARD);

        Component.should(dropdownField, Field.Requirements.Equals.byValue(randomUUID), Duration.ofSeconds(5));

        dropdownField.getItemBy(DropdownField.Item.Requirements.Equals.byText(randomUUID)).open();

        Component.should(textAreaField, Field.Requirements.Equals.byValue(randomUUID), Duration.ofSeconds(5));
        card.delete();
        card.delete();
    }
}
