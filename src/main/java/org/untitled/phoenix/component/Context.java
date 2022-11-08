package org.untitled.phoenix.component;

import org.jetbrains.annotations.Unmodifiable;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>Представляет собой контекст компонента.</p>
 * <ul>
 *     <p><b><i>Хранит информацию о</i></b>:</p>
 *     <li>Последовательности компонентов, находящихся в контексте;</li>
 *     <li>Родителе компонента, за которым закреплён контекст.</li>
 * </ul>
 */
public final class Context {

    private final @Unmodifiable @NotNull List<Component> trace;

    private final @Nullable Component parent;

    /**
     * <p>Создаёт экземпляр контекста в соответствии с указанными аргументами.</p>
     * @param component текущий компонент
     * @param parent родитель
     */
    public Context(@NotNull Component component, @Nullable Component parent) {
        final var trace = new ArrayList<Component>();
        trace.add(component);

        if (parent != null)
            trace.addAll(0, parent.getContext().trace);

        this.trace = Collections.unmodifiableList(trace);
        this.parent = parent;
    }

    /**
     * <p>Возвращает список компонентов, порядок которых определяется принципом: от <b>прародителя</b> по <b>текущий компонент</b>.</p>
     * <ul>
     *     <p><b><i>Прародителем является тот компонент</i></b>:</p>
     *     <li>Который является первым в списке компонентов;</li>
     *     <li>В контексте которого осуществляется поиск всех последующих за ним в списке компонентов.</li><br>
     *     <p><b><i>Родителем является тот компонент</i></b>:</p>
     *     <li>Который является предпоследним в списке компонентов;</li>
     *     <li>В контексте которого осуществляется поиск текущего компонента.</li><br>
     *     <p><b><i>Текущим является тот компонент</i></b>:</p>
     *     <li>Который является последним в списке компонентов;</li>
     *     <li>В контексте которого завершается поиск.</li>
     * </ul>
     * <p><b><i>Примечание</i></b>: <b>прародителем</b> и <b>родителем</b> может быть один и тот же компонент, если в списке всего два компонента.</p><br>
     * <p><b>Контекстом является область поиска компонентов</b>: если у компонента нет родителя, то его поиск осуществляется во всём документе, но если родитель есть, то поиск будет осуществляться внутри родителя.</p><br>
     * <p><b><i>Пример</i></b>: { <b>прародитель</b> -> { <b>родитель</b> -> { <b>текущий компонент</b> } } }</p>
     *  @return список компонентов
     */
    public @Unmodifiable @NotNull List<Component> getTrace() {
        return trace;
    }

    /**
     * <p>Возвращает компонент, который является родителем текущего компонента. Если такого компонента нет, то будет возвращен <b>null</b>.</p>
     * @return компонент или <b>null</b>
     */
    public @Nullable Component getParent() {
        return parent;
    }

    /**
     * <p>Возвращает строку, которая является перечислением компонентов, полученных из {@link #getTrace()}, между которыми ставится разделительная стрелка вправо, указывающая на порядок поиска.</p><br>
     * <nobr><b><i>Пример</i></b>: <b>прародитель</b>-><b>родитель</b>-><b>текущий компонент</b></nobr>
     * @return форматированную строку
     */
    @Override
    public @NotNull String toString() {
        return String.join("->", trace.stream().map(Component::toString).toArray(String[]::new));
    }
}
