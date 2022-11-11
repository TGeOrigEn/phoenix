package example.window;

import example.Button;
import org.gems.WebComponent;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.By;
import org.untitled.phoenix.component.Component;
import org.untitled.phoenix.component.Description;
import org.untitled.phoenix.component.requirement.BaseRequirement;
import org.untitled.phoenix.component.requirement.generic.Requirement;

public abstract class Window extends Component {

    public static class Requirements {

        public static class Equal {

            public @NotNull BaseRequirement<Window> byTitle(@NotNull String title) {
                return new Requirement<>(Window::getTitle, title, "Имеет заголовок");
            }
        }

        public static class Contains {

            public @NotNull BaseRequirement<Window> byTitle(@NotNull String title) {
                return new Requirement<>(Window::getTitle, title, "Имеет заголовок", String::contains);
            }
        }

        public static @NotNull BaseRequirement<Window> isActive(boolean isActive) {
            return new Requirement<>(Window::isActive, isActive, "Является активной");
        }
    }

    private static final @NotNull Description MAXIMIZE_BUTTON_DESCRIPTION = new Description(By.cssSelector("img[class*='x-tool-maximize']"), "Кнопка 'Развернуть'");

    private static final @NotNull Description MINIMIZE_BUTTON_DESCRIPTION = new Description(By.cssSelector("img[class*='x-tool-minimize']"), "Кнопка 'Свернуть'");

    private static final @NotNull Description CLOSE_BUTTON_DESCRIPTION = new Description(By.cssSelector("img[class*='x-tool-close']"), "Кнопка 'Закрыть'");

    private static final @NotNull Description TITLE_DESCRIPTION = new Description(By.cssSelector("div[class*='x-title-item']"), "Заголовок");

    private final @NotNull Component minimizeButton;

    private final @NotNull Component maximizeButton;

    private final @NotNull Component closeButton;

    private final @NotNull Component title;

    public Window() {

        minimizeButton = findInside(() -> new WebComponent(MINIMIZE_BUTTON_DESCRIPTION));
        maximizeButton = findInside(() -> new WebComponent(MAXIMIZE_BUTTON_DESCRIPTION));
        closeButton = findInside(() -> new WebComponent(CLOSE_BUTTON_DESCRIPTION));
        title = findInside(() -> new WebComponent(TITLE_DESCRIPTION));
    }

    public abstract boolean isActive();

    public @NotNull String getTitle() {
        return title.toAction().getText();
    }

    public void maximize() {
        maximizeButton.toAction().click();
    }

    public void minimize() {
        minimizeButton.toAction().click();
    }

    public void close() {
        closeButton.toAction().click();
    }

    protected int getCssIndex() {
        return Integer.parseInt(toAction().getCssValue("z-index"));
    }
}
