package org.untitled.phoenix.component;

import org.jetbrains.annotations.Unmodifiable;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.ArrayList;
import java.util.List;

public final class Property {

    private final @Unmodifiable @NotNull List<Component> trace;

    private final @Nullable Component parent;

    public Property(@NotNull Component component, @Nullable Component parent) {
        final var trace = new ArrayList<Component>();
        trace.add(component);

        if (parent != null)
            trace.addAll(0, parent.getProperties().trace);

        this.trace = Collections.unmodifiableList(trace);
        this.parent = parent;
    }

    public @Unmodifiable @NotNull List<Component> getTrace() {
        return trace;
    }

    public @Nullable Component getParent() {
        return parent;
    }

    @Override
    public @NotNull String toString() {
        return String.join("->", trace.stream().map(Component::toString).toArray(String[]::new));
    }
}
