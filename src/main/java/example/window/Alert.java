package example.window;

import org.gems.WebComponent;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.By;
import org.untitled.phoenix.component.Component;
import org.untitled.phoenix.component.Description;

import java.util.Comparator;

public class Alert extends Window {

    public static final @NotNull Description DEFAULT_DESCRIPTION = new Description(By.cssSelector("div[class^='x-window x-message-box x-layer']:not([class*=x-hidden-offsets])"), "Предупреждение");

    private static final @NotNull Description MESSAGE_DESCRIPTION = new Description(By.cssSelector("div[id*='-msg']:not([style*=display])"), "Сообщение");

    private final @NotNull Component message;

    public Alert() {
        message = findInside(() -> new WebComponent(MESSAGE_DESCRIPTION));
    }

    @Override
    protected @NotNull Description initialize() {
        return DEFAULT_DESCRIPTION;
    }

    @Override
    public boolean isActive() {
        final var cards = Component.findEveryone(Alert::new).stream().sorted(Comparator.comparing(Alert::getCssIndex).reversed()).toList();
        if (cards.isEmpty()) throw new RuntimeException();
        return cards.get(0).getCssIndex() == getCssIndex();
    }

    public @NotNull String getMessage() {
        return message.toAction().getText();
    }
}
