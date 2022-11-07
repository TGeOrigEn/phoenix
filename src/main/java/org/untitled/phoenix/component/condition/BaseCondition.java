package org.untitled.phoenix.component.condition;

public abstract class BaseCondition {

    private boolean enabled = true;

    /**
     * <p>Позволяет выключить или включить проверку условия.</p>
     * @param value будет ли проверяться условие
     */
    public final void setEnabled(boolean value) {
        enabled = value;
    }

    /**
     * <nobr>Возвращает значение, указывающее, проверяется ли условие или нет.</nobr>
     * @return
     * <p><b>true</b> - если условие проверяется</p>
     * <p><b>false</b> - если условие не проверяется</p>
     */
    public final boolean isEnabled() {
        return enabled;
    }

    /**
     * <nobr>Возвращает значение, указывающее, выполняет ли компонент условие или нет.</nobr>
     * @return
     * <p><b>true</b> - если компонент выполняет условие</p>
     * <p><b>false</b> - если компонент не выполняет условие</p>
     */
    public abstract boolean isTrue();
}
