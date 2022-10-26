package org.untitled.phoenix.component;

import org.untitled.phoenix.exception.UnavailableComponentException;
import org.untitled.phoenix.exception.ComponentConditionException;

import org.untitled.phoenix.component.requirement.generic.Requirement;
import org.untitled.phoenix.component.requirement.BaseRequirement;
import org.untitled.phoenix.component.condition.generic.Condition;
import org.untitled.phoenix.component.condition.BaseCondition;

import org.untitled.phoenix.configuration.Configuration;

import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;

import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.By;


import java.util.Arrays;
import java.util.function.Supplier;
import java.util.ArrayList;
import java.time.Duration;
import java.util.List;

public abstract class Component {

    private @Nullable BaseCondition condition;

    private @NotNull Description description;

    private final @NotNull Action action;

    private @NotNull Property property;

    private @NotNull Duration timeout = Duration.ofSeconds(10);

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

    public static <TComponent extends Component> boolean has(@NotNull TComponent component, @NotNull BaseRequirement<TComponent> requirement, @NotNull Duration timeout) {
        final var condition = new Condition<>(component, requirement);
        final var startTime = System.currentTimeMillis();

        while (true) {
            if (condition.isTrue()) return true;
            if (System.currentTimeMillis() - startTime >= timeout.toMillis()) return false;
        }
    }

    public static <TComponent extends Component> TComponent should(@NotNull TComponent component, @NotNull BaseRequirement<TComponent> requirement, @NotNull Duration timeout) {
        final var condition = new Condition<>(component, requirement);
        final var startTime = System.currentTimeMillis();

        while (true) {
            if (condition.isTrue()) return component;
            if (System.currentTimeMillis() - startTime >= timeout.toMillis())
                throw new ComponentConditionException(component, condition, timeout);
        }
    }

    public static <TComponent extends Component> @NotNull TComponent find(@NotNull Supplier<@NotNull TComponent> constructor) {
        return findComponent(constructor, null, null, null);
    }

    public static <TComponent extends Component> @NotNull TComponent find(@NotNull Supplier<@NotNull TComponent> constructor, @NotNull BaseRequirement<TComponent> requirement) {
        return findComponent(constructor, null, requirement, null);
    }

    public static <TComponent extends Component> @NotNull TComponent find(@NotNull Supplier<@NotNull TComponent> constructor, @NotNull Description description, @NotNull BaseRequirement<TComponent> requirement) {
        return findComponent(constructor, description, requirement, null);
    }

    public <TComponent extends Component> @NotNull TComponent findInside(@NotNull Supplier<@NotNull TComponent> constructor) {
        return findComponent(constructor, null, null, this);
    }

    public <TComponent extends Component> @NotNull TComponent findInside(@NotNull Supplier<@NotNull TComponent> constructor, @NotNull BaseRequirement<TComponent> requirement) {
        return findComponent(constructor, null, requirement, this);
    }

    protected abstract @NotNull Description initialize();

    public @NotNull Description getDescription() {
        return description;
    }

    public @NotNull Property getProperties() {
        return property;
    }

    public @Nullable BaseCondition getCondition() {
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
            Component unavailable = this;
            for (int index = trace.length - 1; index >= 0; index--) {
                var element = toWebElement(trace[index]);
                if (element != null) {
                    if (trace[index] == this)
                        return element;
                    break;
                } else unavailable = trace[index];
            }
            if (System.currentTimeMillis() - startTime >= getTimeout().toMillis())
                throw new UnavailableComponentException(unavailable);
        }
    }

    @Override
    public @NotNull String toString() {
        final var s = Arrays.stream(property.getTrace()).map(component -> {
            final var description = component.getDescription();
            final var condition = component.condition;

            return condition == null
                    ? String.format("{'%s[%d](%d)'}", description.getName(), description.getIndex(), component.getIndex())
                    : String.format("{'%s[%d](%d)' ?? %s}", description.getName(), description.getIndex(), component.getIndex(), condition);
        });
        return String.join(" -> ", s.toArray(String[]::new));
    }

    private static <TComponent extends Component> @NotNull TComponent findComponent(@NotNull Supplier<@NotNull TComponent> constructor, @Nullable Description description, @Nullable BaseRequirement<TComponent> requirement, @Nullable Component parent) {
        final var component = constructor.get();
        final var condition = requirement != null
                ? new Condition<>(component, requirement)
                : null;

        if (description != null) ((Component) component).description = description;
        ((Component) component).condition = condition;
        ((Component) component).property = new Property(component, parent);

        return component;
    }

    private static <TComponent extends Component> @NotNull Supplier<List<TComponent>> findComponents(@NotNull Supplier<@NotNull TComponent> constructor, @Nullable Description description, @Nullable BaseRequirement<TComponent> requirement, @Nullable Component parent, @NotNull Duration timeout) {
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
                if (context == null) {
                    component.getProperties().getParent().index = 0;
                    return null;
                }
            }

            if (component.getCondition() == null || !component.getCondition().isEnabled()) {
                final var element = findWebElement(component.getDescription().getMechanism(), context, component.getIndex());
                if (element == null) component.index = 0;
                return element;
            }

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
            component.index = 0;
            return null;
        } catch (Exception e) {
            component.index = 0;
            return null;
        }
    }
}
