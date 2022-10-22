package geoMeta;

import org.untitled.phoenix.component.Component;
import org.untitled.phoenix.component.Description;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.By;

public class QuickTip extends Component {

    @Override
    protected @NotNull Description initialize() {
        return new Description(By.id("ext-quicktips-tip-outerCt"), "Быстрая подсказка");
    }
}
