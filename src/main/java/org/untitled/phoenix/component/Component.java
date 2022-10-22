package org.untitled.phoenix.component;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.untitled.phoenix.component.condition.generic.Condition;
import org.untitled.phoenix.configuration.Configuration;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.Callable;
import java.util.function.Supplier;

public abstract class Component {

    private @NotNull Description description;

    private @NotNull Property property;

    private final @NotNull Action action;

    private @Nullable org.untitled.phoenix.component.condition.Condition condition;

    private @NotNull Duration timeout = Duration.ofSeconds(30);

    private int index = 0;

    public Component(@NotNull Description description) {
        this.action = new Action(this);
        this.description = description;
        this.property = new Property(this, null);
    }

    public Component() {
        this.action = new Action(this);
        this.description = initialize();
        this.property = new Property(this, null);
    }

    public static <TComponent extends Component> boolean has(@NotNull TComponent component, @NotNull Requirement<TComponent> requirement, @NotNull Duration timeout) {
        final var condition = new Condition<>(component, requirement);
        final var startTime = System.currentTimeMillis();

        while (true) {
            if (condition.isTrue()) return true;
            if (System.currentTimeMillis() - startTime >= timeout.toMillis()) return false;
        }
    }

    public static <TComponent extends Component> TComponent should(@NotNull TComponent component, @NotNull Requirement<TComponent> requirement, @NotNull Duration timeout) {
        final var condition = new Condition<>(component, requirement);
        final var startTime = System.currentTimeMillis();

        while (true) {
            if (condition.isTrue()) return component;
            if (System.currentTimeMillis() - startTime >= timeout.toMillis()) throw new RuntimeException(String.format("%s :: Не выполнял условие '%s -> %s' на протяжении %d миллисекунд.", component, requirement.getDescription(), requirement.getValue(), timeout.toMillis()));
        }
    }

    public static <TComponent extends Component> @NotNull TComponent find(@NotNull Supplier<@NotNull TComponent> constructor) {
        return findComponent(constructor, null, null, null);
    }

    public static <TComponent extends Component> @NotNull TComponent find(@NotNull Supplier<@NotNull TComponent> constructor, @NotNull Requirement<TComponent> requirement) {
        return findComponent(constructor, null, requirement, null);
    }

    public <TComponent extends Component> @NotNull TComponent findInside(@NotNull Supplier<@NotNull TComponent> constructor) {
        return findComponent(constructor, null, null, this);
    }

    public <TComponent extends Component> @NotNull TComponent findInside(@NotNull Supplier<@NotNull TComponent> constructor, @NotNull Requirement<TComponent> requirement) {
        return findComponent(constructor, null, requirement, this);
    }

    protected abstract @NotNull Description initialize();

    public @NotNull Description getDescription() {
        return description;
    }

    public @NotNull Property getProperties() {
        return property;
    }

    public @Nullable org.untitled.phoenix.component.condition.Condition getCondition() {
        return condition;
    }

    public @NotNull Duration getTimeout() {
        return timeout;
    }

    public void setTimeout(@NotNull Duration timeout) {
        this.timeout = timeout;
    }

    public int getIndex() {
        return index;
    }

    public boolean isAvailable() {
        return toWebElement(this) != null;
    }

    public @NotNull Action toAction() {
        return action;
    }

    public @NotNull WebElement toWebElement() {
        final var startTime = System.currentTimeMillis();
        final var trace = getProperties().getTrace();

        while (true) {
            Component unavailable;
            for (int index = trace.length - 1; index >= 0; index--) {
                var element = toWebElement(trace[index]);
                if (element != null) {
                    if (trace[index] == this)
                        return element;
                    break;
                } else unavailable = trace[index];

                if (System.currentTimeMillis() - startTime >= getTimeout().toMillis())
                    throw new RuntimeException(String.format("%s :: Был недоступен на протяжении %d миллисекунд.", unavailable, timeout.toMillis()));
            }
        }
    }

    @Override
    public String toString() {
        return condition == null
                ? property.getPath()
                : String.format("%s ?? '%s -> %s'", property.getPath(), condition.getDescription(), condition.getValue());
    }

    private static <TComponent extends Component> @NotNull TComponent findComponent(@NotNull Supplier<@NotNull TComponent> constructor, @Nullable Description description, @Nullable Requirement<TComponent> requirement, @Nullable Component parent) {
        final var component = constructor.get();
        final var condition = requirement != null
                ? new Condition<>(component, requirement)
                : null;

        if (description != null) ((Component) component).description = description;
        ((Component) component).property = new Property(component, parent);
        ((Component) component).condition = condition;

        return component;
    }

    private static <TComponent extends Component> @NotNull Supplier<List<TComponent>> findComponents(@NotNull Supplier<@NotNull TComponent> constructor, @Nullable Description description, @Nullable Requirement<TComponent> requirement, @Nullable Component parent, @NotNull Duration timeout) {
        return () -> {
            final var components = new ArrayList<TComponent>();
            for (int index1 = 0; index1 < Integer.MAX_VALUE; index1++) {
                final var component = findComponent(constructor, description, requirement, parent);
                if (!has(component, Requirement.byAvailable(true), timeout)) break;
                components.add(component);
            }
            return components;
        };
    }

    private static @NotNull List<WebElement> findWebElements(By by, SearchContext context) {
        return context == null ? Configuration.getWebDriver().findElements(by) : context.findElements(by);
    }

    private static @Nullable WebElement findWebElement(By by, SearchContext context, int index) {
        final var elements = context == null
                ? Configuration.getWebDriver().findElements(by)
                : context.findElements(by);

        if (elements.size() > index)
            return elements.get(index);

        return null;
    }

    private static @Nullable WebElement toWebElement(Component component) {
        try {
            SearchContext context = null;

            if (component.getProperties().getParent() != null) {
                context = toWebElement(component.getProperties().getParent());
                if (context == null) return null;
            }

            if (component.getCondition() == null || !component.getCondition().isEnabled())
                return findWebElement(component.getDescription().getMechanism(), context, component.getIndex());

            var elements = findWebElements(component.getDescription().getMechanism(), context);
            component.getCondition().setEnabled(false);
            int count = -1;

            for (int index = 0; index < elements.size(); index++) {
                component.index = index;

                if (component.getCondition().isTrue())
                    if (++count == component.getDescription().getIndex()) {
                        component.getCondition().setEnabled(true);
                        return elements.get(index);
                    }
            }

            component.getCondition().setEnabled(true);
            return null;
        } catch (Exception e) {
            return null;
        }
    }
}
