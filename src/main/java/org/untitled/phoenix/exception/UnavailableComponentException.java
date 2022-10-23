package org.untitled.phoenix.exception;

import org.untitled.phoenix.component.Component;

import org.jetbrains.annotations.NotNull;

public final class UnavailableComponentException extends RuntimeException {

    private final @NotNull Component component;

    public UnavailableComponentException(@NotNull Component component){
        this.component = component;
    }

    @Override
    public String toString() {
        return String.format("%s :: Был недоступен на протяжении %d миллисекунд.", component, component.getTimeout().toMillis());
    }
}
