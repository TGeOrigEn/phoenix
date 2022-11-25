package geometa.objectCard;

import common.BaseTest;
import common.nested.TAG;
import example.Attachment;
import example.Menu;
import example.button.Button;
import example.field.Field;
import example.field.nested.TextField;
import example.table.ViewPanel;
import example.window.Card;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.untitled.phoenix.component.Component;
import org.untitled.phoenix.component.requirement.generic.Requirement;

import java.io.*;
import java.time.Duration;

@Tag(TAG.GEOMETA.OBJECT_CARD)
@DisplayName("Тест вложений карточки объекта")
public class BasicWithAttachmentsObjectCardTest extends BaseObjectCardTest {

    private static final @NotNull File attachmentA;

    private static final @NotNull File attachmentB;

    private static final @NotNull Attachment attachment = Component.find(Attachment::new);

    static {
        final var pathToAttachmentA = BaseTest.class.getClassLoader().getResource("geometa/objectCard/attachmentA.txt");
        final var pathToAttachmentB = BaseTest.class.getClassLoader().getResource("geometa/objectCard/attachmentB.txt");

        if (pathToAttachmentA == null) throw new NullPointerException("Вложения 'AttachmentA' не существует.");
        if (pathToAttachmentB == null) throw new NullPointerException("Вложения 'AttachmentB' не существует.");

        attachmentA = new File(pathToAttachmentA.getPath());
        attachmentB = new File(pathToAttachmentB.getPath());
    }

    @Override
    protected @NotNull String[] initializeItems() {
        return new String[]{"Приморский край", "autotests", "objectCard"};
    }

    @Override
    protected @NotNull String initializeTable() {
        return "Заявление";
    }

    @Test
    @DisplayName("Прикрепить файл к карточке объекта")
    public void attachFileToObjectCard() {
        card.findInside(TextField::new, Field.Requirements.Equals.byTitle("Номер:")).setValue(randomUUID);

        card.findInside(Card.Section::new, Card.Section.Requirements.Equal.byTitle("Вложения")).expand();

        card.upload(attachmentA);

        Component.should(card.findInside(Attachment.Item::new, Attachment.Item.Requirements.Equals.byName("attachmentA.txt")), Requirement.isAvailable(true), Duration.ofSeconds(5));

        card.save();
        card.close();

        viewPanel.getSearchField().setValue(randomUUID);

        spinner.wait(Duration.ofSeconds(60));

        final var item = viewPanel.getItemBy(ViewPanel.Item.Requirements.Equals.byValue("Номер", randomUUID));

        item.show(ViewPanel.Item.Option.ATTACHMENT);

        Component.should(attachment.findInside(Attachment.Item::new, Attachment.Item.Requirements.Equals.byName("attachmentA.txt")), Requirement.isAvailable(true), Duration.ofSeconds(5));

        attachment.close();

        item.select();

        viewPanel.deleteSelectedObjects();
    }

    @Test
    @DisplayName("Прикрепить несколько файлов к карточке объекта")
    public void attachFilesToObjectCard() {
        card.findInside(TextField::new, Field.Requirements.Equals.byTitle("Номер:")).setValue(randomUUID);

        card.findInside(Card.Section::new, Card.Section.Requirements.Equal.byTitle("Вложения")).expand();

        card.upload(attachmentA);
        card.upload(attachmentB);

        Component.should(card.findInside(Attachment.Item::new, Attachment.Item.Requirements.Equals.byName("attachmentA.txt")), Requirement.isAvailable(true), Duration.ofSeconds(5));
        Component.should(card.findInside(Attachment.Item::new, Attachment.Item.Requirements.Equals.byName("attachmentB.txt")), Requirement.isAvailable(true), Duration.ofSeconds(5));

        card.save();
        card.close();

        viewPanel.getSearchField().setValue(randomUUID);

        spinner.wait(Duration.ofSeconds(60));

        final var item = viewPanel.getItemBy(ViewPanel.Item.Requirements.Equals.byValue("Номер", randomUUID));

        item.show(ViewPanel.Item.Option.ATTACHMENT);

        Component.should(attachment.findInside(Attachment.Item::new, Attachment.Item.Requirements.Equals.byName("attachmentA.txt")), Requirement.isAvailable(true), Duration.ofSeconds(5));
        Component.should(attachment.findInside(Attachment.Item::new, Attachment.Item.Requirements.Equals.byName("attachmentB.txt")), Requirement.isAvailable(true), Duration.ofSeconds(5));

        attachment.close();

        item.select();

        viewPanel.deleteSelectedObjects();
    }

    @Test
    @DisplayName("Скачать несколько файлов из карточки объекта")
    public void downloadFilesFromObjectCard() throws IOException {
        card.findInside(TextField::new, Field.Requirements.Equals.byTitle("Номер:")).setValue(randomUUID);

        card.findInside(Card.Section::new, Card.Section.Requirements.Equal.byTitle("Вложения")).expand();

        card.upload(attachmentA);
        card.upload(attachmentB);

        Component.should(card.findInside(Attachment.Item::new, Attachment.Item.Requirements.Equals.byName("attachmentA.txt")), Requirement.isAvailable(true), Duration.ofSeconds(5));
        Component.should(card.findInside(Attachment.Item::new, Attachment.Item.Requirements.Equals.byName("attachmentB.txt")), Requirement.isAvailable(true), Duration.ofSeconds(5));

        card.save();
        card.close();

        viewPanel.getSearchField().setValue(randomUUID);

        spinner.wait(Duration.ofSeconds(60));

        final var item = viewPanel.getItemBy(ViewPanel.Item.Requirements.Equals.byValue("Номер", randomUUID));

        item.show(ViewPanel.Item.Option.CARD);

        final var attachmentSection = card.findInside(Card.Section::new, Card.Section.Requirements.Equal.byTitle("Вложения (2)"));
        attachmentSection.expand();

        final var actualAttachmentA = attachmentSection.findInside(Attachment.Item::new, Attachment.Item.Requirements.Equals.byName("attachmentA.txt")).download(Duration.ofSeconds(2));
        final var actualAttachmentB = attachmentSection.findInside(Attachment.Item::new, Attachment.Item.Requirements.Equals.byName("attachmentB.txt")).download(Duration.ofSeconds(2));

        if (!new BufferedReader(new FileReader(actualAttachmentA)).readLine().equals("attachmentA"))
            throw new RuntimeException("Неправильный текст в скаченном файле 'attachmentA'.");

        if (!new BufferedReader(new FileReader(actualAttachmentB)).readLine().equals("attachmentB"))
            throw new RuntimeException("Неправильный текст в скаченном файле 'attachmentB'.");

        card.close();

        viewPanel.deleteSelectedObjects();
    }

    @Test
    @DisplayName("Удалить файл в карточке объекта")
    public void removeFilesToObjectCard() {
        card.findInside(TextField::new, Field.Requirements.Equals.byTitle("Номер:")).setValue(randomUUID);
        final var attachmentSection = card.findInside(Card.Section::new, Card.Section.Requirements.Contains.byTitle("Вложения"));
        attachmentSection.expand();

        card.upload(attachmentA);
        card.upload(attachmentB);

        Component.should(card.findInside(Attachment.Item::new, Attachment.Item.Requirements.Equals.byName("attachmentA.txt")), Requirement.isAvailable(true), Duration.ofSeconds(5));
        Component.should(card.findInside(Attachment.Item::new, Attachment.Item.Requirements.Equals.byName("attachmentB.txt")), Requirement.isAvailable(true), Duration.ofSeconds(5));

        card.save();
        card.close();

        viewPanel.getSearchField().setValue(randomUUID);

        spinner.wait(Duration.ofSeconds(60));

        final var item = viewPanel.getItemBy(ViewPanel.Item.Requirements.Equals.byValue("Номер", randomUUID));

        item.show(ViewPanel.Item.Option.CARD);

        attachmentSection.expand();

        final var attachmentA = attachmentSection.findInside(Attachment.Item::new, Attachment.Item.Requirements.Equals.byName("attachmentA.txt"));
        final var attachmentB = attachmentSection.findInside(Attachment.Item::new, Attachment.Item.Requirements.Equals.byName("attachmentB.txt"));

        attachmentA.delete();

        card.save();
        card.close();

        item.show(ViewPanel.Item.Option.CARD);

        attachmentSection.expand();

        Component.should(attachmentA, Requirement.isAvailable(false), Duration.ofSeconds(1), 0);
        Component.should(attachmentB, Requirement.isAvailable(true), Duration.ofSeconds(5));

        card.close();

        viewPanel.deleteSelectedObjects();
    }

}
