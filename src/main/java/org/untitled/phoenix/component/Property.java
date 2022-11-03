package org.untitled.phoenix.component;

import org.jetbrains.annotations.Unmodifiable;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.ArrayList;
import java.util.List;

/**
 * Представляет собой сущность, которая хранит информацию о том,
 */
public final class Property {

    private final @Unmodifiable @NotNull List<Component> trace;

    private final @Nullable Component parent;

    public Property(@NotNull Component component, @Nullable Component parent) {
        final var trace = new ArrayList<Component>();
        trace.add(component);

        if (parent != null)
            trace.addAll(0, parent.getProperty().trace);

        this.trace = Collections.unmodifiableList(trace);
        this.parent = parent;
    }

    /**
     * <p>Возвращает <b>неизменяемый</b> список компонентов, который сформирован по принципу: от прародителя, с которого начинается поиск и в котором происходит поиск последующего компонента в списке, и так далее вплоть до родителя, в котором завершается поиск текущего компонента.</p><br>
     * <p><i><b>Родителем</b> является компонент, внутри которого завершается поиск текущего компонента.</i></p>
     * <p><i><b>Прародителем</b> является компонент, с которого начинается начинается поиск.</i></p>
     * <ul>
     *     <p><b><i>Примечания:</i></b></p>
     *     <li>Если компонент имеет родителя, которы также имеет родителя, то будет возвращён <strong>неизменяемый</strong> список, в котором содержатся три компонента: прародитель, родитель и текущий компонент.</li>
     *     <li>Если компонент имеет родителя, который не имеет родителя, то будет возвращён <strong>неизменяемый</strong> список, в котором содержатся два компонента: его прародитель/родитель и текущий компонент.</li>
     *     <li>Если компонент не имеет родителя, то будет возращён <strong>неизменяемый</strong> список, в котором содержится только текущий компонент.</li>
     * </ul>
     * <p><b><i>Пример:</i></b> { <b>прародитель</b> -> { <b>родитель</b> -> { <b>текущий компонент</b> } } }</p>
     * @return <b>неизменяемый</b> список компонентов
     */
    public @Unmodifiable @NotNull List<Component> getTrace() {
        return trace;
    }

    /**
     * <p>Возвращает родителя компонента. Если у компонента нет родителя, то возвращает <strong>null</strong>.</p>
     * @return родителя компонента или <strong>null</strong>
     */
    public @Nullable Component getParent() {
        return parent;
    }

    @Override
    public @NotNull String toString() {
        return String.join("->", trace.stream().map(Component::toString).toArray(String[]::new));
    }
}
