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

    private final @NotNull Action action;

    private @NotNull Property property;

    private @NotNull Duration timeout = Duration.ofSeconds(10);

    private int index = 0;

    public Component(@NotNull Action action, @NotNull Description description){
        this.action = action;
        this.description = description;
        this.property = new Property(this, null);
    }

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

    //region Методы глобального поиска

    //region Одного

    // Без требования

    public static <TComponent extends Component> @NotNull TComponent find(@NotNull Supplier<@NotNull TComponent> constructor, @NotNull Description description) {
        return findComponent(constructor, description, null, null);
    }

    public static <TComponent extends Component> @NotNull TComponent find(@NotNull Supplier<@NotNull TComponent> constructor, @NotNull By mechanism, @NotNull String name) {
        return findComponent(constructor, null, null, mechanism, name);
    }

    public static <TComponent extends Component> @NotNull TComponent find(@NotNull Supplier<@NotNull TComponent> constructor, @NotNull By mechanism) {
        return findComponent(constructor, null, null, mechanism, null);
    }

    public static <TComponent extends Component> @NotNull TComponent find(@NotNull Supplier<@NotNull TComponent> constructor, @NotNull String name) {
        return findComponent(constructor, null, null, null, name);
    }

    public static <TComponent extends Component> @NotNull TComponent find(@NotNull Supplier<@NotNull TComponent> constructor, int index) {
        return findComponent(constructor, null, null, index);
    }

    public static <TComponent extends Component> @NotNull TComponent find(@NotNull Supplier<@NotNull TComponent> constructor) {
        return findComponent(constructor, null, null, null);
    }

    // С требованием

    public static <TComponent extends Component> @NotNull TComponent find(@NotNull Supplier<@NotNull TComponent> constructor, @NotNull Description description, @NotNull BaseRequirement<TComponent> requirement) {
        return findComponent(constructor, description, requirement, null);
    }

    public static <TComponent extends Component> @NotNull TComponent find(@NotNull Supplier<@NotNull TComponent> constructor, @NotNull By mechanism, @NotNull String name, @NotNull BaseRequirement<TComponent> requirement) {
        return findComponent(constructor, requirement, null, mechanism, name);
    }

    public static <TComponent extends Component> @NotNull TComponent find(@NotNull Supplier<@NotNull TComponent> constructor, @NotNull By mechanism, @NotNull BaseRequirement<TComponent> requirement) {
        return findComponent(constructor, requirement, null, mechanism, null);
    }

    public static <TComponent extends Component> @NotNull TComponent find(@NotNull Supplier<@NotNull TComponent> constructor, @NotNull String name, @NotNull BaseRequirement<TComponent> requirement) {
        return findComponent(constructor, requirement, null, null, name);
    }

    public static <TComponent extends Component> @NotNull TComponent find(@NotNull Supplier<@NotNull TComponent> constructor, int index, @NotNull BaseRequirement<TComponent> requirement) {
        return findComponent(constructor, requirement, null, index);
    }

    public static <TComponent extends Component> @NotNull TComponent find(@NotNull Supplier<@NotNull TComponent> constructor, @NotNull BaseRequirement<TComponent> requirement) {
        return findComponent(constructor, null, requirement, null);
    }

    //endregion

    //region Множество

    public static <TComponent extends Component> @NotNull List<TComponent> findEveryone(@NotNull Supplier<@NotNull TComponent> constructor, @NotNull Description description) {
        return findComponents(constructor, description, null, null, Duration.ZERO);
    }

    public static <TComponent extends Component> @NotNull List<TComponent> findEveryone(@NotNull Supplier<@NotNull TComponent> constructor, @NotNull By mechanism, @NotNull String name) {
        return findComponents(constructor, null, null, Duration.ZERO, mechanism, name);
    }

    public static <TComponent extends Component> @NotNull List<TComponent> findEveryone(@NotNull Supplier<@NotNull TComponent> constructor, @NotNull By mechanism) {
        return findComponents(constructor, null, null, Duration.ZERO, mechanism, null);
    }

    public static <TComponent extends Component> @NotNull List<TComponent> findEveryone(@NotNull Supplier<@NotNull TComponent> constructor, @NotNull String name) {
        return findComponents(constructor, null, null, Duration.ZERO, null, name);
    }

    public static <TComponent extends Component> @NotNull List<TComponent> findEveryone(@NotNull Supplier<@NotNull TComponent> constructor) {
        return findComponents(constructor, null, null, null, Duration.ZERO);
    }

    //

    public static <TComponent extends Component> @NotNull List<TComponent> findEveryone(@NotNull Supplier<@NotNull TComponent> constructor, @NotNull Description description, @NotNull Duration timeout) {
        return findComponents(constructor, description, null, null, timeout);
    }

    public static <TComponent extends Component> @NotNull List<TComponent> findEveryone(@NotNull Supplier<@NotNull TComponent> constructor, @NotNull By mechanism, @NotNull String name, @NotNull Duration timeout) {
        return findComponents(constructor, null, null, timeout, mechanism, name);
    }

    public static <TComponent extends Component> @NotNull List<TComponent> findEveryone(@NotNull Supplier<@NotNull TComponent> constructor, @NotNull By mechanism, @NotNull Duration timeout) {
        return findComponents(constructor, null, null, timeout, mechanism, null);
    }

    public static <TComponent extends Component> @NotNull List<TComponent> findEveryone(@NotNull Supplier<@NotNull TComponent> constructor, @NotNull String name, @NotNull Duration timeout) {
        return findComponents(constructor, null, null, timeout, null, name);
    }

    public static <TComponent extends Component> @NotNull List<TComponent> findEveryone(@NotNull Supplier<@NotNull TComponent> constructor, @NotNull Duration timeout) {
        return findComponents(constructor, null, null, null, timeout);
    }

    // С требованием

    public static <TComponent extends Component> @NotNull List<TComponent> findEveryone(@NotNull Supplier<@NotNull TComponent> constructor, @NotNull Description description, @NotNull BaseRequirement<TComponent> requirement) {
        return findComponents(constructor, description, requirement, null, Duration.ZERO);
    }

    public static <TComponent extends Component> @NotNull List<TComponent> findEveryone(@NotNull Supplier<@NotNull TComponent> constructor, @NotNull By mechanism, @NotNull String name, @NotNull BaseRequirement<TComponent> requirement) {
        return findComponents(constructor, requirement, null, Duration.ZERO, mechanism, name);
    }

    public static <TComponent extends Component> @NotNull List<TComponent> findEveryone(@NotNull Supplier<@NotNull TComponent> constructor, @NotNull By mechanism, @NotNull BaseRequirement<TComponent> requirement) {
        return findComponents(constructor, requirement, null, Duration.ZERO, mechanism, null);
    }

    public static <TComponent extends Component> @NotNull List<TComponent> findEveryone(@NotNull Supplier<@NotNull TComponent> constructor, @NotNull String name, @NotNull BaseRequirement<TComponent> requirement) {
        return findComponents(constructor, requirement, null, Duration.ZERO, null, name);
    }

    public static <TComponent extends Component> @NotNull List<TComponent> findEveryone(@NotNull Supplier<@NotNull TComponent> constructor, @NotNull BaseRequirement<TComponent> requirement) {
        return findComponents(constructor, null, requirement, null, Duration.ZERO);
    }

    //

    public static <TComponent extends Component> @NotNull List<TComponent> findEveryone(@NotNull Supplier<@NotNull TComponent> constructor, @NotNull Description description, @NotNull BaseRequirement<TComponent> requirement, @NotNull Duration timeout) {
        return findComponents(constructor, description, requirement, null, timeout);
    }

    public static <TComponent extends Component> @NotNull List<TComponent> findEveryone(@NotNull Supplier<@NotNull TComponent> constructor, @NotNull By mechanism, @NotNull String name, @NotNull BaseRequirement<TComponent> requirement, @NotNull Duration timeout) {
        return findComponents(constructor, requirement, null, timeout, mechanism, name);
    }

    public static <TComponent extends Component> @NotNull List<TComponent> findEveryone(@NotNull Supplier<@NotNull TComponent> constructor, @NotNull By mechanism, @NotNull BaseRequirement<TComponent> requirement, @NotNull Duration timeout) {
        return findComponents(constructor, requirement, null, timeout, mechanism, null);
    }

    public static <TComponent extends Component> @NotNull List<TComponent> findEveryone(@NotNull Supplier<@NotNull TComponent> constructor, @NotNull String name, @NotNull BaseRequirement<TComponent> requirement, @NotNull Duration timeout) {
        return findComponents(constructor, requirement, null, timeout, null, name);
    }

    public static <TComponent extends Component> @NotNull List<TComponent> findEveryone(@NotNull Supplier<@NotNull TComponent> constructor, @NotNull BaseRequirement<TComponent> requirement, @NotNull Duration timeout) {
        return findComponents(constructor, null, requirement, null, timeout);
    }

    //endregion

    //endregion

    //region Методы внутреннего поиска

    //region Одного

    // Без требования

    public <TComponent extends Component> @NotNull TComponent findInside(@NotNull Supplier<@NotNull TComponent> constructor, @NotNull Description description) {
        return findComponent(constructor, description, null, this);
    }

    public <TComponent extends Component> @NotNull TComponent findInside(@NotNull Supplier<@NotNull TComponent> constructor, @NotNull By mechanism, @NotNull String name) {
        return findComponent(constructor, null, this, mechanism, name);
    }

    public <TComponent extends Component> @NotNull TComponent findInside(@NotNull Supplier<@NotNull TComponent> constructor, @NotNull By mechanism) {
        return findComponent(constructor, null, this, mechanism, null);
    }

    public <TComponent extends Component> @NotNull TComponent findInside(@NotNull Supplier<@NotNull TComponent> constructor, @NotNull String name) {
        return findComponent(constructor, null, this, null, name);
    }

    public <TComponent extends Component> @NotNull TComponent findInside(@NotNull Supplier<@NotNull TComponent> constructor, int index) {
        return findComponent(constructor, null, this, index);
    }

    public <TComponent extends Component> @NotNull TComponent findInside(@NotNull Supplier<@NotNull TComponent> constructor) {
        return findComponent(constructor, null, null, this);
    }

    // С требованием

    public <TComponent extends Component> @NotNull TComponent findInside(@NotNull Supplier<@NotNull TComponent> constructor, @NotNull Description description, @NotNull BaseRequirement<TComponent> requirement) {
        return findComponent(constructor, description, requirement, this);
    }

    public <TComponent extends Component> @NotNull TComponent findInside(@NotNull Supplier<@NotNull TComponent> constructor, @NotNull By mechanism, @NotNull String name, @NotNull BaseRequirement<TComponent> requirement) {
        return findComponent(constructor, requirement, this, mechanism, name);
    }

    public <TComponent extends Component> @NotNull TComponent findInside(@NotNull Supplier<@NotNull TComponent> constructor, @NotNull By mechanism, @NotNull BaseRequirement<TComponent> requirement) {
        return findComponent(constructor, requirement, this, mechanism, null);
    }

    public <TComponent extends Component> @NotNull TComponent findInside(@NotNull Supplier<@NotNull TComponent> constructor, @NotNull String name, @NotNull BaseRequirement<TComponent> requirement) {
        return findComponent(constructor, requirement, this, null, name);
    }

    public <TComponent extends Component> @NotNull TComponent findInside(@NotNull Supplier<@NotNull TComponent> constructor, int index, @NotNull BaseRequirement<TComponent> requirement) {
        return findComponent(constructor, requirement, this, index);
    }

    public <TComponent extends Component> @NotNull TComponent findInside(@NotNull Supplier<@NotNull TComponent> constructor, @NotNull BaseRequirement<TComponent> requirement) {
        return findComponent(constructor, null, requirement, this);
    }

    //endregion

    //region Множество

    public <TComponent extends Component> @NotNull List<TComponent> findInsideEveryone(@NotNull Supplier<@NotNull TComponent> constructor, @NotNull Description description) {
        return findComponents(constructor, description, null, null, Duration.ZERO);
    }

    public <TComponent extends Component> @NotNull List<TComponent> findInsideEveryone(@NotNull Supplier<@NotNull TComponent> constructor, @NotNull By mechanism, @NotNull String name) {
        return findComponents(constructor, null, null, Duration.ZERO, mechanism, name);
    }

    public <TComponent extends Component> @NotNull List<TComponent> findInsideEveryone(@NotNull Supplier<@NotNull TComponent> constructor, @NotNull By mechanism) {
        return findComponents(constructor, null, null, Duration.ZERO, mechanism, null);
    }

    public <TComponent extends Component> @NotNull List<TComponent> findInsideEveryone(@NotNull Supplier<@NotNull TComponent> constructor, @NotNull String name) {
        return findComponents(constructor, null, null, Duration.ZERO, null, name);
    }

    public <TComponent extends Component> @NotNull List<TComponent> findInsideEveryone(@NotNull Supplier<@NotNull TComponent> constructor) {
        return findComponents(constructor, null, null, null, Duration.ZERO);
    }

    //

    public <TComponent extends Component> @NotNull List<TComponent> findInsideEveryone(@NotNull Supplier<@NotNull TComponent> constructor, @NotNull Description description, @NotNull Duration timeout) {
        return findComponents(constructor, description, null, null, timeout);
    }

    public <TComponent extends Component> @NotNull List<TComponent> findInsideEveryone(@NotNull Supplier<@NotNull TComponent> constructor, @NotNull By mechanism, @NotNull String name, @NotNull Duration timeout) {
        return findComponents(constructor, null, null, timeout, mechanism, name);
    }

    public <TComponent extends Component> @NotNull List<TComponent> findInsideEveryone(@NotNull Supplier<@NotNull TComponent> constructor, @NotNull By mechanism, @NotNull Duration timeout) {
        return findComponents(constructor, null, null, timeout, mechanism, null);
    }

    public <TComponent extends Component> @NotNull List<TComponent> findInsideEveryone(@NotNull Supplier<@NotNull TComponent> constructor, @NotNull String name, @NotNull Duration timeout) {
        return findComponents(constructor, null, null, timeout, null, name);
    }

    public <TComponent extends Component> @NotNull List<TComponent> findInsideEveryone(@NotNull Supplier<@NotNull TComponent> constructor, @NotNull Duration timeout) {
        return findComponents(constructor, null, null, null, timeout);
    }

    // С требованием

    public <TComponent extends Component> @NotNull List<TComponent> findInsideEveryone(@NotNull Supplier<@NotNull TComponent> constructor, @NotNull Description description, @NotNull BaseRequirement<TComponent> requirement) {
        return findComponents(constructor, description, requirement, this, Duration.ZERO);
    }

    public <TComponent extends Component> @NotNull List<TComponent> findInsideEveryone(@NotNull Supplier<@NotNull TComponent> constructor, @NotNull By mechanism, @NotNull String name, @NotNull BaseRequirement<TComponent> requirement) {
        return findComponents(constructor, requirement, this, Duration.ZERO, mechanism, name);
    }

    public <TComponent extends Component> @NotNull List<TComponent> findInsideEveryone(@NotNull Supplier<@NotNull TComponent> constructor, @NotNull By mechanism, @NotNull BaseRequirement<TComponent> requirement) {
        return findComponents(constructor, requirement, this, Duration.ZERO, mechanism, null);
    }

    public <TComponent extends Component> @NotNull List<TComponent> findInsideEveryone(@NotNull Supplier<@NotNull TComponent> constructor, @NotNull String name, @NotNull BaseRequirement<TComponent> requirement) {
        return findComponents(constructor, requirement, this, Duration.ZERO, null, name);
    }

    public <TComponent extends Component> @NotNull List<TComponent> findInsideEveryone(@NotNull Supplier<@NotNull TComponent> constructor, @NotNull BaseRequirement<TComponent> requirement) {
        return findComponents(constructor, null, requirement, this, Duration.ZERO);
    }

    //

    public <TComponent extends Component> @NotNull List<TComponent> findInsideEveryone(@NotNull Supplier<@NotNull TComponent> constructor, @NotNull Description description, @NotNull BaseRequirement<TComponent> requirement, @NotNull Duration timeout) {
        return findComponents(constructor, description, requirement, this, timeout);
    }

    public <TComponent extends Component> @NotNull List<TComponent> findInsideEveryone(@NotNull Supplier<@NotNull TComponent> constructor, @NotNull By mechanism, @NotNull String name, @NotNull BaseRequirement<TComponent> requirement, @NotNull Duration timeout) {
        return findComponents(constructor, requirement, this, timeout, mechanism, name);
    }

    public <TComponent extends Component> @NotNull List<TComponent> findInsideEveryone(@NotNull Supplier<@NotNull TComponent> constructor, @NotNull By mechanism, @NotNull BaseRequirement<TComponent> requirement, @NotNull Duration timeout) {
        return findComponents(constructor, requirement, this, timeout, mechanism, null);
    }

    public <TComponent extends Component> @NotNull List<TComponent> findInsideEveryone(@NotNull Supplier<@NotNull TComponent> constructor, @NotNull String name, @NotNull BaseRequirement<TComponent> requirement, @NotNull Duration timeout) {
        return findComponents(constructor, requirement, this, timeout, null, name);
    }

    public <TComponent extends Component> @NotNull List<TComponent> findInsideEveryone(@NotNull Supplier<@NotNull TComponent> constructor, @NotNull BaseRequirement<TComponent> requirement, @NotNull Duration timeout) {
        return findComponents(constructor, null, requirement, this, timeout);
    }

    //endregion

    //endregion

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
        return condition == null
                ? String.format("{'%s(%d)'}", description, index)
                : String.format("{'%s(%d)' ?? %s}", description, index, condition);
    }

    private static <TComponent extends Component> @NotNull TComponent findComponent(@NotNull Supplier<@NotNull TComponent> constructor, @Nullable BaseRequirement<TComponent> requirement, @Nullable Component parent, @Nullable By mechanism, @Nullable String name) {
        final var component = constructor.get();

        final var condition = requirement != null
                ? new Condition<>(component, requirement)
                : null;

        final var description = new Description(
                mechanism == null ? component.getDescription().getMechanism() : mechanism,
                name == null ? component.getDescription().getName() : name,
                component.getDescription().getIndex());

        ((Component) component).condition = condition;
        ((Component) component).description = description;
        ((Component) component).property = new Property(component, parent);

        return component;
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

    private static <TComponent extends Component> @NotNull TComponent findComponent(@NotNull Supplier<@NotNull TComponent> constructor, @Nullable BaseRequirement<TComponent> requirement, @Nullable Component parent, int index) {
        final var component = constructor.get();

        final var condition = requirement != null
                ? new Condition<>(component, requirement)
                : null;

        final var description = new Description(
                component.getDescription().getMechanism(),
                component.getDescription().getName(),
                index);

        ((Component) component).condition = condition;
        ((Component) component).description = description;
        ((Component) component).property = new Property(component, parent);

        return component;
    }

    private static <TComponent extends Component> @NotNull List<TComponent> findComponents(@NotNull Supplier<@NotNull TComponent> constructor, @Nullable BaseRequirement<TComponent> requirement, @Nullable Component parent, @NotNull Duration timeout, @Nullable By mechanism, @Nullable String name) {
        final var components = new ArrayList<TComponent>();

        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            final var component = findComponent(constructor, requirement, parent, mechanism, name);
            if (!has(component, Requirement.byAvailable(true), timeout)) break;
            components.add(component);
        }

        return components;
    }

    private static <TComponent extends Component> @NotNull List<TComponent> findComponents(@NotNull Supplier<@NotNull TComponent> constructor, @Nullable Description description, @Nullable BaseRequirement<TComponent> requirement, @Nullable Component parent, @NotNull Duration timeout) {
        final var components = new ArrayList<TComponent>();

        for (int index = 0; index < Integer.MAX_VALUE; index++) {
            final var component = findComponent(constructor, description, requirement, parent);
            if (!has(component, Requirement.byAvailable(true), timeout)) break;
            components.add(component);
        }

        return components;
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
