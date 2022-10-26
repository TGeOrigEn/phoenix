package org.untitled.phoenix.exception;

import org.untitled.phoenix.component.Component;

import org.jetbrains.annotations.NotNull;

public final class UnavailableComponentException extends ComponentException {

    public UnavailableComponentException(@NotNull Component component){
        super(component, String.format("Был недоступен на протяжении %d миллисекунд", component.getTimeout().toMillis()));
    }
}
