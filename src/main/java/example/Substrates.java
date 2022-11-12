package example;

import org.gems.WebComponent;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.By;
import org.untitled.phoenix.component.Component;
import org.untitled.phoenix.component.Description;
import org.untitled.phoenix.component.requirement.BaseRequirement;
import org.untitled.phoenix.component.requirement.generic.Requirement;

public class Substrates extends Component {

    public static final class Item extends Component {

        public static class Requirements {

            public static class Equals {

                public static @NotNull BaseRequirement<Item> byName(@NotNull String name) {
                    return new Requirement<>(Item::getName, name, "Имеет имя");
                }
            }

            public static class Contains {

                public static @NotNull BaseRequirement<Item> byName(@NotNull String name) {
                    return new Requirement<>(Item::getName, name, "Содержит имя", String::contains);
                }
            }

            public static @NotNull BaseRequirement<Item> isSelected(boolean isSelected) {
                return new Requirement<>(Item::isSelected, isSelected, "Является выделенным");
            }
        }

        private static final @NotNull Description DEFAULT_DESCRIPTION = new Description(By.cssSelector("div[class*='bl-wrap']"), "Подложка");

        private static final @NotNull Description NAME_DESCRIPTION = new Description(By.tagName("span"), "Имя");

        private final @NotNull Component name;

        public Item() {
            name = findInside(() -> new WebComponent(NAME_DESCRIPTION));
        }

        @Override
        protected @NotNull Description initialize() {
            return DEFAULT_DESCRIPTION;
        }

        public boolean isSelected() {
            return toAction().getCssClass().contains("selected");
        }

        public @NotNull String getName() {
            return name.toAction().getText();
        }

        public void select() {
            toAction().click();
        }
    }

    public static final @NotNull Description DEFAULT_DESCRIPTION = new Description(By.cssSelector("div[id*='baselayersmanager'][class*='x-panel']"), "Подложки");

    @Override
    protected @NotNull Description initialize() {
        return DEFAULT_DESCRIPTION;
    }
}
