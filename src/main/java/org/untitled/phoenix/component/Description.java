package org.untitled.phoenix.component;

import org.jetbrains.annotations.NotNull;

import org.openqa.selenium.By;

public final class Description {

    private final @NotNull By mechanism;

    private final @NotNull String name;

    private final int index;

    public Description(@NotNull By mechanism, @NotNull String name, int index) {
        this.mechanism = mechanism;
        this.index = index;
        this.name = name;
    }

    public Description(@NotNull By mechanism, @NotNull String name) {
        this(mechanism, name, 0);
    }

    public @NotNull By getMechanism() {
        return mechanism;
    }

    public @NotNull String getName() {
        return name;
    }

    public int getIndex() {
        return index;
    }

    @Override
    public @NotNull String toString() {
        return String.format("%s[%d]", name, index);
    }
}
