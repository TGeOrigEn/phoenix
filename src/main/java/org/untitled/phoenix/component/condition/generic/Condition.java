package org.untitled.phoenix.component.condition.generic;

import org.untitled.phoenix.component.requirement.BaseRequirement;
import org.untitled.phoenix.component.condition.BaseCondition;
import org.untitled.phoenix.component.Component;

import org.jetbrains.annotations.NotNull;

/**
 * <p>Представляет собой условие, позволяющее проверить, выполняет ли компонент условие.</p>
 * @param <TComponent> класс компонента, который выполняет данное условие
 */
public final class Condition<TComponent extends Component> extends BaseCondition {

    private final @NotNull BaseRequirement<TComponent> requirement;

    private final @NotNull TComponent component;

    /**
     * <p>Создаёт новый экземпляр условия в соответствии с указанным требованием для указанного компонента.</p>
     * @param component компонент, который будет выполнять условие
     * @param requirement требование, которое описывает, как выполнять условие компоненту
     */
    public Condition(@NotNull TComponent component, @NotNull BaseRequirement<TComponent> requirement) {
        this.requirement = requirement;
        this.component = component;
    }

    @Override
    public boolean isTrue() {
        return requirement.isTrue(component);
    }

    @Override
    public @NotNull String toString() {
        return requirement.toString();
    }
}
