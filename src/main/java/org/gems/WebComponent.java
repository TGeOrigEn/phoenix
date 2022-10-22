package org.gems;

import org.untitled.phoenix.component.Description;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.By;
import org.untitled.phoenix.component.Component;

public final class WebComponent extends Component {

    public WebComponent(@NotNull Description description) {
        super(description);
    }

    @Override
    protected @NotNull Description initialize() {
        return new Description(By.cssSelector("*"), "Веб-компонент");
    }
}
