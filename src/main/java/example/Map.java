package example;

import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.By;
import org.untitled.phoenix.component.Component;
import org.untitled.phoenix.component.Description;

public class Map extends Component {

    public static class Polygon extends Component {

        private static final @NotNull Description DEFAULT_DESCRIPTION = new Description(By.cssSelector("path[d]"), "Полигон");

        @Override
        protected @NotNull Description initialize() {
            return DEFAULT_DESCRIPTION;
        }
    }

    public static class Point extends Component {

        private static final @NotNull Description DEFAULT_DESCRIPTION = new Description(By.cssSelector("div[class='leaflet-marker-icon leaflet-div-icon leaflet-editing-icon leaflet-zoom-hide leaflet-clickable']"), "Точка");

        @Override
        protected @NotNull Description initialize() {
            return DEFAULT_DESCRIPTION;
        }
    }

    public static final @NotNull Description DEFAULT_DESCRIPTION = new Description(By.cssSelector("div[id^='leafletmap']"), "Карта");

    @Override
    protected @NotNull Description initialize() {
        return DEFAULT_DESCRIPTION;
    }
}
