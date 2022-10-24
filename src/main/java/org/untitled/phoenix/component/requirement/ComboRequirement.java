package org.untitled.phoenix.component.requirement;

import org.jetbrains.annotations.NotNull;
import org.untitled.phoenix.component.Component;

public class ComboRequirement<TComponent extends Component> extends BaseRequirement<TComponent> {

    private final @NotNull BaseRequirement<TComponent> requirementA;

    private final @NotNull BaseRequirement<TComponent> requirementB;

    public ComboRequirement(@NotNull BaseRequirement<TComponent> requirementA, @NotNull BaseRequirement<TComponent> requirementB) {
        super(requirementA.getValue(), description);
    }

    @Override
    public boolean isTrue(TComponent component) {
        return false;
    }
}
