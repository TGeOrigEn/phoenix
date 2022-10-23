package org.untitled.phoenix.component;

import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public final class Property {

    private final @NotNull Component[] trace;

    private final @Nullable Component parent;

    private final @NotNull String path;

    public Property(@NotNull Component component, @Nullable Component parent) {
        this.parent = parent;

        if (parent == null) trace = new Component[]{component};
        else {
            final var length = parent.getProperties().trace.length;
            final var trace = new Component[length + 1];

            System.arraycopy(parent.getProperties().trace, 0, trace, 0, length);

            trace[length] = component;
            this.trace = trace;
        }

        path = String.join("->", Arrays.stream(trace).map(element -> String.format("'%s[%d]'", element.getDescription().getName(), element.getDescription().getIndex())).toArray(String[]::new));
    }

    public @Nullable Component getParent() {
        return parent;
    }

    public @NotNull Component[] getTrace() {
        return trace;
    }

    public @NotNull String getPath() {
        return path;
    }
}
