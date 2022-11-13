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

import java.util.function.Supplier;
import java.util.ArrayList;
import java.time.Duration;
import java.util.List;

public abstract class Component {

    private @Nullable BaseCondition condition;

    private @NotNull Description description;

    private @NotNull Context context;

    private boolean initialized;

    private @NotNull Duration timeout = Duration.ofSeconds(60);

    private int index = 0;

    protected Component(@NotNull Description description) {
        this.description = description;
        this.context = new Context(this, null);
    }

    protected Component() {
        this.description = initialize();
        this.context = new Context(this, null);
    }

    protected abstract @NotNull Description initialize();

    public static <TComponent extends Component> TComponent should(@NotNull TComponent component, @NotNull BaseRequirement<TComponent> requirement, @NotNull Duration timeout) {
        final var condition = new Condition<>(component, requirement);
        final var startTime = System.currentTimeMillis();

        while (true) {
            if (condition.isTrue()) return component;
            if (System.currentTimeMillis() - startTime >= timeout.toMillis())
                throw new ComponentConditionException(component, condition, timeout);
        }
    }

    public static <TComponent extends Component> boolean has(@NotNull TComponent component, @NotNull BaseRequirement<TComponent> requirement, @NotNull Duration timeout) {
        final var condition = new Condition<>(component, requirement);
        final var startTime = System.currentTimeMillis();

        while (true) {
            if (condition.isTrue()) return true;
            if (System.currentTimeMillis() - startTime >= timeout.toMillis()) return false;
        }
    }

    public static <TComponent extends Component> @NotNull TComponent find(@NotNull Supplier<@NotNull TComponent> constructor, @NotNull Description description, @NotNull BaseRequirement<TComponent> requirement) {
        return findComponent(constructor, description, requirement, null);
    }

    public static <TComponent extends Component> @NotNull TComponent find(@NotNull Supplier<@NotNull TComponent> constructor, @NotNull BaseRequirement<TComponent> requirement) {
        return findComponent(constructor, null, requirement, null);
    }

    public static <TComponent extends Component> @NotNull TComponent find(@NotNull Supplier<@NotNull TComponent> constructor, @NotNull Description description) {
        return findComponent(constructor, description, null, null);
    }

    public static <TComponent extends Component> @NotNull TComponent find(@NotNull Supplier<@NotNull TComponent> constructor) {
        return findComponent(constructor, null, null, null);
    }

    public static  <TComponent extends Component> @NotNull List<TComponent> findEveryone(@NotNull Supplier<@NotNull TComponent> constructor, @NotNull Description description, @NotNull BaseRequirement<TComponent> requirement, @NotNull Duration timeout) {
        return findComponents(constructor, description, requirement, null, timeout);
    }

    public static <TComponent extends Component> @NotNull List<TComponent> findEveryone(@NotNull Supplier<@NotNull TComponent> constructor, @NotNull BaseRequirement<TComponent> requirement, @NotNull Duration timeout) {
        return findComponents(constructor, null, requirement, null, timeout);
    }

    public static <TComponent extends Component> @NotNull List<TComponent> findEveryone(@NotNull Supplier<@NotNull TComponent> constructor, @NotNull Description description, @NotNull Duration timeout) {
        return findComponents(constructor, description, null, null, timeout);
    }

    public static <TComponent extends Component> @NotNull List<TComponent> findEveryone(@NotNull Supplier<@NotNull TComponent> constructor, @NotNull Duration timeout) {
        return findComponents(constructor, null, null, null, timeout);
    }

    public static <TComponent extends Component> @NotNull List<TComponent> findEveryone(@NotNull Supplier<@NotNull TComponent> constructor, @NotNull Description description, @NotNull BaseRequirement<TComponent> requirement) {
        return findComponents(constructor, description, requirement, null, Duration.ZERO);
    }

    public static <TComponent extends Component> @NotNull List<TComponent> findEveryone(@NotNull Supplier<@NotNull TComponent> constructor, @NotNull BaseRequirement<TComponent> requirement) {
        return findComponents(constructor, null, requirement, null, Duration.ZERO);
    }

    public static <TComponent extends Component> @NotNull List<TComponent> findEveryone(@NotNull Supplier<@NotNull TComponent> constructor, @NotNull Description description) {
        return findComponents(constructor, description, null, null, Duration.ZERO);
    }

    public static <TComponent extends Component> @NotNull List<TComponent> findEveryone(@NotNull Supplier<@NotNull TComponent> constructor) {
        return findComponents(constructor, null, null, null, Duration.ZERO);
    }

    public <TComponent extends Component> @NotNull TComponent findInside(@NotNull Supplier<@NotNull TComponent> constructor, @NotNull Description description, @NotNull BaseRequirement<TComponent> requirement) {
        return findComponent(constructor, description, requirement, this);
    }

    public <TComponent extends Component> @NotNull TComponent findInside(@NotNull Supplier<@NotNull TComponent> constructor, @NotNull BaseRequirement<TComponent> requirement) {
        return findComponent(constructor, null, requirement, this);
    }

    public <TComponent extends Component> @NotNull TComponent findInside(@NotNull Supplier<@NotNull TComponent> constructor, @NotNull Description description) {
        return findComponent(constructor, description, null, this);
    }

    public <TComponent extends Component> @NotNull TComponent findInside(@NotNull Supplier<@NotNull TComponent> constructor) {
        return findComponent(constructor, null, null, this);
    }

    public <TComponent extends Component> @NotNull List<TComponent> findInsideEveryone(@NotNull Supplier<@NotNull TComponent> constructor, @NotNull Description description, @NotNull BaseRequirement<TComponent> requirement, @NotNull Duration timeout) {
        return findComponents(constructor, description, requirement, this, timeout);
    }

    public <TComponent extends Component> @NotNull List<TComponent> findInsideEveryone(@NotNull Supplier<@NotNull TComponent> constructor, @NotNull BaseRequirement<TComponent> requirement, @NotNull Duration timeout) {
        return findComponents(constructor, null, requirement, this, timeout);
    }

    public <TComponent extends Component> @NotNull List<TComponent> findInsideEveryone(@NotNull Supplier<@NotNull TComponent> constructor, @NotNull Description description, @NotNull Duration timeout) {
        return findComponents(constructor, description, null, this, timeout);
    }

    public <TComponent extends Component> @NotNull List<TComponent> findInsideEveryone(@NotNull Supplier<@NotNull TComponent> constructor, @NotNull Duration timeout) {
        return findComponents(constructor, description, null, this, timeout);
    }

    public <TComponent extends Component> @NotNull List<TComponent> findInsideEveryone(@NotNull Supplier<@NotNull TComponent> constructor, @NotNull Description description, @NotNull BaseRequirement<TComponent> requirement) {
        return findComponents(constructor, description, requirement, this, Duration.ZERO);
    }

    public <TComponent extends Component> @NotNull List<TComponent> findInsideEveryone(@NotNull Supplier<@NotNull TComponent> constructor, @NotNull BaseRequirement<TComponent> requirement) {
        return findComponents(constructor, null, requirement, this, Duration.ZERO);
    }

    public <TComponent extends Component> @NotNull List<TComponent> findInsideEveryone(@NotNull Supplier<@NotNull TComponent> constructor, @NotNull Description description) {
        return findComponents(constructor, description, null, this, Duration.ZERO);
    }

    public <TComponent extends Component> @NotNull List<TComponent> findInsideEveryone(@NotNull Supplier<@NotNull TComponent> constructor) {
        return findComponents(constructor, description, null, this, Duration.ZERO);
    }

    public @NotNull Description getDescription() {
        return description;
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

    public @NotNull Context getContext() {
        return context;
    }

    public boolean isInitialized() {
        return initialized;
    }

    public @NotNull Action toAction() {
        return new Action(this);
    }

    public int getIndex() {
        return index;
    }

    public boolean isAvailable() {
        return toWebElement(this) != null;
    }

    public @NotNull WebElement toWebElement() {
        final var startTime = System.currentTimeMillis();
        final var trace = getContext().getTrace();

        while (true) {
            Component unavailable = this;
            for (int index = trace.size() - 1; index >= 0; index--) {
                var element = toWebElement(trace.get(index));
                if (element != null) {
                    if (trace.get(index) == this) {
                        initialized = true;
                        return element;
                    }
                    break;
                } else unavailable = trace.get(index);
            }
            if (System.currentTimeMillis() - startTime >= getTimeout().toMillis())
                throw new UnavailableComponentException(unavailable);
        }
    }

    @Override
    public @NotNull String toString() {
        return condition == null
                ? String.format("{ %s }", description.getName())
                : String.format("{ %s ?? %s }", description.getName(), condition);
    }

    private static <TComponent extends Component> @NotNull List<TComponent> findComponents(@NotNull Supplier<@NotNull TComponent> constructor, @Nullable Description description, @Nullable BaseRequirement<TComponent> requirement, @Nullable Component parent, @NotNull Duration timeout) {
        final var components = new ArrayList<TComponent>();

        for (int index = 0; index < Integer.MAX_VALUE; index++) {
            final var component = findComponent(constructor, description, requirement, parent);
            ((Component)component).description = component.getDescription().copy(index);
            if (!has(component, Requirement.byAvailable(true), timeout)) break;
            components.add(component);
        }

        return components;
    }

    private static <TComponent extends Component> @NotNull TComponent findComponent(@NotNull Supplier<@NotNull TComponent> constructor, @Nullable Description description, @Nullable BaseRequirement<TComponent> requirement, @Nullable Component parent) {
        final var component = constructor.get();
        final var condition = requirement != null
                ? new Condition<>(component, requirement)
                : null;

        if (description != null) ((Component) component).description = description;
        ((Component) component).condition = condition;
        ((Component) component).context = new Context(component, parent);

        return component;
    }

    private static @Nullable WebElement findWebElement(@NotNull By by, @Nullable SearchContext searchContext, int index) {
        final var elements = searchContext == null
                ? Configuration.getWebDriver().findElements(by)
                : searchContext.findElements(by);

        if (elements.size() > index)
            return elements.get(index);

        return null;
    }

    private static @NotNull List<WebElement> findWebElements(@NotNull By by, @Nullable SearchContext searchContext) {
        return searchContext == null
                ? Configuration.getWebDriver().findElements(by)
                : searchContext.findElements(by);
    }

    private static @Nullable WebElement toWebElement(@NotNull Component component) {
        try {
            SearchContext context = null;

            if (component.getContext().getParent() != null) {
                context = toWebElement(component.getContext().getParent());
                if (context == null) {
                    component.getContext().getParent().index = 0;
                    return null;
                }
            }

            if (component.getCondition() == null) {
                final var element = findWebElement(component.getDescription().getBy(), context, component.getDescription().getIndex());
                if (element == null) component.index = 0;
                return element;
            }

            if (!component.getCondition().isEnabled()) {
                final var element = findWebElement(component.getDescription().getBy(), context, component.getIndex());
                if (element == null) component.index = 0;
                return element;
            }

            var elements = findWebElements(component.getDescription().getBy(), context);
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
