package geometa.objectCard;

import common.BaseTest;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;

public class BasicWithAttachmentsObjectCardTest extends BaseObjectCardTest {

    private static final @NotNull File attachmentA;

    private static final @NotNull File attachmentB;

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

    }
}
