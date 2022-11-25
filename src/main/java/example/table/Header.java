package example.table;

import example.Menu;
import org.gems.WebComponent;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.By;
import org.untitled.phoenix.component.Component;
import org.untitled.phoenix.component.Description;
import org.untitled.phoenix.component.requirement.BaseRequirement;
import org.untitled.phoenix.component.requirement.generic.Requirement;

public class Header extends Component {

    public static class Requirements {

        public static class Equals {

            public static @NotNull BaseRequirement<Header> byName(@NotNull String name) {
                return new Requirement<>(Header::getName, name, "Имеет имя");
            }
        }

        public static class Contains {

            public static @NotNull BaseRequirement<Header> byName(@NotNull String name) {
                return new Requirement<>(Header::getName, name, "Содержит имя", String::contains);
            }
        }
    }

    public static final @NotNull Description DEFAULT_DESCRIPTION = new Description(By.cssSelector("div[class*='x-column-header x-column-header-align-left']:not([style*='display'])"), "Заголовок столбца");

    private static final @NotNull Description ARROW_BUTTON_DESCRIPTION = new Description(By.cssSelector("div[class='x-column-header-trigger']"), "Кнопка 'Стрелка'");

    private static final @NotNull Description TEXT_DESCRIPTION = new Description(By.cssSelector("span[class='x-column-header-text']"), "Текст");

    private final @NotNull Component arrowButton;

    private final @NotNull Component text;

    public Header() {
        arrowButton = findInside(() -> new WebComponent(ARROW_BUTTON_DESCRIPTION));
        text = findInside(() -> new WebComponent(TEXT_DESCRIPTION));
    }

    @Override
    protected @NotNull Description initialize() {
        return DEFAULT_DESCRIPTION;
    }

    public @NotNull String getName() {
        return text.toAction().getText();
    }

    public @NotNull Menu openMenu() {
        toAction().hover();
        arrowButton.toAction().click();
        return Component.find(Menu::new, Menu.Requirements.isActive(true));
    }
}
