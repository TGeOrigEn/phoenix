package geometa.viewPanel.search;

import common.nested.TAG;
import example.NavigationPanel;
import example.field.Field;

import example.field.nested.TextField;
import example.table.ViewPanel;
import geometa.BaseGeometaTest;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.untitled.phoenix.component.Component;
import org.untitled.phoenix.component.requirement.generic.Requirement;

import java.time.Duration;

@Tag(TAG.GEOMETA.VIEW_PANEL)
public class ViewPanelSearchTest extends BaseGeometaTest {
    @Override
    protected @NotNull String[] initializeItems() {
        return new String[]{"Приморский край", "autotests", "objectCard"};
    }

    @BeforeEach
    public void beforeEach() {
        Component.find(NavigationPanel.Item::new, NavigationPanel.Item.Requirements.Equals.byName("Заявление")).open(NavigationPanel.Item.Option.TABLE);

        viewPanel.createNewObject();

        card.findInside(TextField::new, Field.Requirements.Equals.byTitle("Номер:")).setValue(randomUUID);

        card.save();
        card.close();

        viewPanel.refreshTable();

        Component.should(viewPanel.getItemBy(ViewPanel.Item.Requirements.Equals.byValue("Номер", randomUUID)), Requirement.isAvailable(true), Duration.ofSeconds(60));
    }

    @Test
    public void search() {
        viewPanel.getSearchField().setValue(randomUUID);
        spinner.wait(Duration.ofSeconds(60));

        final var item = viewPanel.getItemBy(ViewPanel.Item.Requirements.Equals.byValue("Номер", randomUUID));

        item.show(ViewPanel.Item.Option.CARD);

        card.delete();

        viewPanel.getSearchField().setValue(randomUUID);
        spinner.wait(Duration.ofSeconds(60));

        Component.should(item, Requirement.isAvailable(false), Duration.ofSeconds(1), 0);
    }
}
