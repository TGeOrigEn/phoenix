package example;

import org.gems.WebComponent;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.By;
import org.untitled.phoenix.component.Action;
import org.untitled.phoenix.component.Component;
import org.untitled.phoenix.component.Description;
import org.untitled.phoenix.component.requirement.generic.Requirement;

import java.time.Duration;

public class Map extends Component {

    public static class Polygon extends Component {

        public static final @NotNull Description DEFAULT_DESCRIPTION = new Description(By.cssSelector("path[d]"), "Полигон");

        @Override
        protected @NotNull Description initialize() {
            return DEFAULT_DESCRIPTION;
        }
    }

    public static class Point extends Component {

        public static final @NotNull Description DEFAULT_DESCRIPTION = new Description(By.cssSelector("div[class*='leaflet-marker-icon']"), "Точка");

        @Override
        protected @NotNull Description initialize() {
            return DEFAULT_DESCRIPTION;
        }
    }

    public static final @NotNull Description DEFAULT_DESCRIPTION = new Description(By.cssSelector("div[id^='leafletmap']"), "Карта");

    private static final @NotNull Description LOADING_DESCRIPTION = new Description(By.cssSelector("div[class='gems-leaflet-control-map-load-indicator animated leaflet-control fade-in']"), "Индикатор загрузки карты");

    private final @NotNull Component loading;

    public Map() {
        loading = findInside(() -> new WebComponent(LOADING_DESCRIPTION));
    }

    @Override
    protected @NotNull Description initialize() {
        return DEFAULT_DESCRIPTION;
    }

    public void createPolygon(org.openqa.selenium.Point @NotNull ... points) {
        final var firstPoint = findInside(Point::new, Point.DEFAULT_DESCRIPTION.copy(1));

        toAction().click();

        var lastPoint = new org.openqa.selenium.Point(0, 0);


        for (var point : points) {
            new Action.Factory().moveToComponent(firstPoint, point.x - lastPoint.x, point.y - lastPoint.y)
                    .click().perform();
            lastPoint = new org.openqa.selenium.Point(point.x, point.y);
        }

        new Action.Factory().moveToComponent(firstPoint, 0, 0)
                .click().perform();
    }

    public void createLine(org.openqa.selenium.Point @NotNull ... points) {
        final var firstPoint = findInside(Point::new, Point.DEFAULT_DESCRIPTION.copy(1));

        toAction().click();

        var lastPoint = new org.openqa.selenium.Point(0, 0);


        for (var point : points) {
            new Action.Factory().moveToComponent(firstPoint, point.x - lastPoint.x, point.y - lastPoint.y)
                    .click().perform();
            lastPoint = new org.openqa.selenium.Point(point.x, point.y);
        }

        new Action.Factory().moveToComponent(findInside(Point::new, Point.DEFAULT_DESCRIPTION.copy(2)), 0, 0)
                .click().perform();
    }

    public void wait(Duration timeout) {
        if (Component.has(loading, Requirement.isAvailable(true), Duration.ofSeconds(1)))
            Component.has(loading, Requirement.isAvailable(false), timeout);
    }
}
