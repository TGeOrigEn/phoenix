package geometa.viewPanel.attachment;

import common.BaseTest;
import example.*;
import example.field.Field;
import example.field.nested.DropdownField;
import example.field.nested.TextField;
import example.table.ViewPanel;
import geometa.BaseGeometaTest;
import io.qameta.allure.Step;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.*;
import org.untitled.phoenix.component.Component;
import org.untitled.phoenix.component.requirement.generic.Requirement;

import java.io.*;
import java.time.Duration;

@DisplayName("Вложения")
public class ViewPanelAttachmentsTest extends BaseGeometaTest {

    private static final @NotNull File attachmentA;

    private static final @NotNull File attachmentB;

    private static final @NotNull Attachment attachment = Component.find(Attachment::new);

    static {
        final var pathToAttachmentA = BaseTest.class.getClassLoader().getResource("geometa/objectCard/attachmentA.txt");
        final var pathToAttachmentB = BaseTest.class.getClassLoader().getResource("geometa/objectCard/attachmentB.txt.txt");

        if (pathToAttachmentA == null) throw new NullPointerException("Вложения 'AttachmentA' не существует.");
        if (pathToAttachmentB == null) throw new NullPointerException("Вложения 'AttachmentB' не существует.");

        attachmentA = new File(pathToAttachmentA.getPath());
        attachmentB = new File(pathToAttachmentB.getPath());
    }

    @Override
    protected @NotNull String[] initializeItems() {
        return new String[]{"Приморский край", "autotests", "presentationPanel"};
    }

    @BeforeEach
    public void beforeEach() {
        Component.find(NavigationPanel.Item::new, NavigationPanel.Item.Requirements.Equals.byName("Акт приемки ОКС")).open(NavigationPanel.Item.Option.TABLE);

        removeAllItem();

        viewPanel.createNewObject();

        card.findInside(TextField::new, Field.Requirements.Equals.byTitle("Номер:")).setValue(randomUUID);
        card.findInside(DropdownField::new, Field.Requirements.Equals.byTitle("Земельный участок:")).setValue("Земельный участок №55555 ()");

        card.upload(attachmentA);
        card.upload(attachmentB);

        card.save();
        card.close();

        viewPanel.refreshTable();

        Component.should(viewPanel.getItemBy(ViewPanel.Item.Requirements.Equals.byValue("Номер", randomUUID)), Requirement.isAvailable(true), Duration.ofSeconds(60));
    }

    @Test
    @Step("Скачать вложенные файлы")
    public void downloadAttachment() throws IOException {
        viewPanel.getSearchField().setValue(randomUUID);

        final var item = viewPanel.getItemBy(ViewPanel.Item.Requirements.Equals.byValue("Номер", randomUUID));

        item.show(ViewPanel.Item.Option.ATTACHMENT);
        final var actualAttachmentA = attachment.findInside(Attachment.Item::new, Attachment.Item.Requirements.Equals.byName("attachmentA.txt")).download(Duration.ofSeconds(2));

        item.show(ViewPanel.Item.Option.ATTACHMENT);
        final var actualAttachmentB = attachment.findInside(Attachment.Item::new, Attachment.Item.Requirements.Equals.byName("attachmentB.txt.txt")).download(Duration.ofSeconds(2));

        if (!new BufferedReader(new FileReader(actualAttachmentA)).readLine().equals("attachmentA"))
            throw new RuntimeException("Неправильный текст в скаченном файле 'attachmentA'.");

        if (!new BufferedReader(new FileReader(actualAttachmentB)).readLine().equals("attachmentB.txt"))
            throw new RuntimeException("Неправильный текст в скаченном файле 'attachmentB.txt'.");
    }

    @AfterEach
    public void afterEach() {
        removeAllItem();
    }

    private static void removeAllItem() {
        while (Component.has(viewPanel.findInside(ViewPanel.Item::new),  Requirement.isAvailable(true), Duration.ofSeconds(1))) {
            viewPanel.findInside(ViewPanel.Item::new).select();
            viewPanel.deleteSelectedObjects();
            viewPanel.refreshTable();
        }
    }
}
