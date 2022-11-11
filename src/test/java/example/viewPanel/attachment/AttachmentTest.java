package example.viewPanel.attachment;

import example.*;
import example.field.Field;
import example.field.TextField;
import example.field.dropdown.DropdownField;
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

import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.time.Duration;

@DisplayName("Вложения")
public class AttachmentTest extends BaseTest {

    private static final Card card = (Card) Component.find(Card::new, Window.Requirements.isActive(true));
    private static final ViewPanel viewPanel = Component.find(ViewPanel::new);
    private static final Alert alert = Component.find(Alert::new);

    @Override
    protected @NotNull String getAddress() {
        return "https://autotests.gemsdev.ru/";
    }

    @BeforeEach
    @Step("Подготовка тестовых данных")
    public void beforeEach() throws URISyntaxException {
        Component.find(AuthorizationForm::new).logIn("gemsAdmin", "gemsAdmin123$");

        Component.find(NavigationPanel.Item::new, NavigationPanel.Item.Requirements.Equals.byName("Приморский край")).expand();
        Component.find(NavigationPanel.Item::new, NavigationPanel.Item.Requirements.Equals.byName("autotests")).expand();
        Component.find(NavigationPanel.Item::new, NavigationPanel.Item.Requirements.Equals.byName("presentationPanel")).expand();

        Component.find(NavigationPanel.Item::new, NavigationPanel.Item.Requirements.Equals.byName("Акт приемки ОКС")).open(NavigationPanel.Item.Option.TABLE);

        removeAllItem();

        viewPanel.findInside(Button::new, Button.Requirements.Equals.byTip("Создать новый объект")).click();

        card.findInside(DropdownField::new, Field.Requirements.Equals.byTitle("Земельный участок:")).setValue("Земельный участок №25:36:030101:1");

        card.upload(new File(getClass().getClassLoader().getResource("TextDocument.txt").toURI()));
        card.findInside(Button::new, Button.Requirements.Equals.byTip("Сохранить")).click();
        card.close();

        refreshTable();

        Component.should(viewPanel.findInside(ViewPanel.Item::new, ViewPanel.Item.Requirements.Equals.byValue("Земельный участок", "Земельный участок №25:36:030101:1")), Requirement.byAvailable(true), Duration.ofSeconds(60));
    }

    @Test
    @Step("Скачать вложенный файл")
    public void downloadAttachment() throws IOException {
        viewPanel.findInside(ViewPanel.Item::new, ViewPanel.Item.Requirements.Equals.byValue("Земельный участок", "Земельный участок №25:36:030101:1")).show(ViewPanel.Item.Option.ATTACHMENT);
        final var file = Component.find(Attachment::new).findInside(Attachment.Item::new, Attachment.Item.Requirements.Equals.byName("TextDocument.txt")).download(Duration.ofSeconds(2));

        if (new BufferedReader(new FileReader(file)).readLine().equals(""))
            throw new RuntimeException("This is a test text document.");
    }

    @AfterEach
    @Step("Удаление тестовых данных")
    public void afterEach() {
        removeAllItem();
    }

    private static void refreshTable() {
        viewPanel.findInside(Button::new, Button.Requirements.Equals.byTip("Обновить таблицу")).click();
        Component.find(Spinner::new).wait(Duration.ofSeconds(60));
    }

    private static void removeAllItem() {
        final var item = viewPanel.findInside(ViewPanel.Item::new);

        while (Component.has(item,  Requirement.byAvailable(true), Duration.ofSeconds(1))) {
            item.show(ViewPanel.Item.Option.CARD);

            card.findInside(Button::new, Button.Requirements.Equals.byText("Еще")).showMenu().findInside(Menu.Option::new, Menu.Option.Requirements.Equals.byText("Удалить объект")).click();
            alert.findInside(Button::new, Button.Requirements.Equals.byText("Удалить")).click();

            refreshTable();
        }
    }
}
