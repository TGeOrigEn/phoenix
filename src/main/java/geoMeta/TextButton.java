package geoMeta;

import org.untitled.phoenix.component.Component;
import org.untitled.phoenix.component.Description;
import org.gems.WebComponent;
import org.untitled.phoenix.component.requirement.generic.Requirement;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.By;

public class TextButton extends Component {

    public static @NotNull Requirement<TextButton, String> byText(String buttonText) {
        return new Requirement<>(TextButton::getText, buttonText, "Имеет текст");
    }

    public static @NotNull Requirement<TextButton, String> byQuickTipText(String quickTipText) {
        return new Requirement<>(TextButton::getQuickTipText, quickTipText, "Имеет текст подсказки");
    }

    private static final Description TEXT_DESCRIPTION = new Description(By.cssSelector("span[class^='x-btn-inner']"), "Текст кнопки");

    private final Component text;

    public TextButton() {
        text = findInside(() -> new WebComponent(TEXT_DESCRIPTION));
    }

    @Override
    protected @NotNull Description initialize() {
        return new Description(By.cssSelector("a[class*='x-btn x-unselectable x-box-item']:not([style*=display])"), "Текстовая кнопка");
    }

    public void click() {
        getAction().click();
    }

    public String getText() {
        return text.getAction().getText();
    }

    public String getQuickTipText(){
       return getAction().getAttribute("data-qtip");
    }
}
