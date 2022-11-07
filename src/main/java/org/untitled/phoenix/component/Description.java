package org.untitled.phoenix.component;

import org.jetbrains.annotations.NotNull;

import org.openqa.selenium.By;

/**
 * <p>Данный класс хранит базовую информацию, которая описывает компонент.</p>
 * <ul>
 *     <p><b><i>Виды информации</i></b>:</p>
 *     <li>{@link #getIndex()} - возвращает значение логического индекса, который определяет порядковый номер компонента среди тех, что были найдены.</li>
 *     <li>{@link #getName()} - возвращает строку, которая является описанием компонента или его именем.</li>
 *     <li>{@link #getBy()} - возвращает механизм поиска компонента, описание которого определяет, чем будет являться компонент в документе.</li>
 * </ul>
 */
public final class Description {

    private final @NotNull String name;

    private final @NotNull By by;

    private final int index;

    /**
     * <p>Создаёт экземпляр описания в соответствии с указанными аргументами.</p>
     * @param by механизм поиска
     * @param name имя
     * @param index логический индекс
     */
    public Description(@NotNull By by, @NotNull String name, int index) {
        this.by = by;
        this.index = index;
        this.name = name;
    }

    /**
     * <p>Создаёт экземпляр описания в соответствии с указанными аргументами.</p><br>
     * <p><b><i>Логический индекс будет равен нулю</i></b>.</p>
     * @param by механизм поиска
     * @param name имя
     */
    public Description(@NotNull By by, @NotNull String name) {
        this(by, name, 0);
    }

    /**
     * <p>Возвращает новый экземпляр описания, значения которого меняются в соответствии с указанными аргументами.</p>
     * @param by механизм поиска
     * @param name имя
     * @return новый экземпляр описания
     */
    public @NotNull Description copy(@NotNull By by, @NotNull String name) {
        return new Description(by, name, index);
    }

    /**
     * <p>Возвращает новый экземпляр описания, значения которого меняются в соответствии с указанными аргументами.</p>
     * @param name имя
     * @param index логический индекс
     * @return новый экземпляр описания
     */
    public @NotNull Description copy(@NotNull String name, int index) {
        return new Description(by, name, index);
    }

    /**
     * <p>Возвращает новый экземпляр описания, значения которого меняются в соответствии с указанными аргументами.</p>
     * @param by механизм поиска
     * @param index логический индекс
     * @return новый экземпляр описания
     */
    public @NotNull Description copy(@NotNull By by, int index) {
        return new Description(by, name, index);
    }

    /**
     * <p>Возвращает новый экземпляр описания, значения которого меняются в соответствии с указанными аргументами.</p>
     * @param name имя
     * @return новый экземпляр описания
     */
    public @NotNull Description copy(@NotNull String name) {
        return new Description(by, name, index);
    }

    /**
     * <p>Возвращает новый экземпляр описания, значения которого меняются в соответствии с указанными аргументами.</p>
     * @param by механизм поиска
     * @return новый экземпляр описания
     */
    public @NotNull Description copy(@NotNull By by) {
        return new Description(by, name, index);
    }

    /**
     * <p>Возвращает новый экземпляр описания, значения которого меняются в соответствии с указанными аргументами.</p>
     * @param index логический индекс
     * @return новый экземпляр описания
     */
    public @NotNull Description copy(int index) {
        return new Description(by, name, index);
    }

    /**
     * <p>Возвращает сущность {@link By}, которая является механизмом поиска веб-элементов в документе.</p><br>
     * <p><b>Примечание</b>: механизм поиска непосредственно отвечает за поиск компонента, так как компонент - это тот же самый веб-элемент в своей сути.</p>
     * <ul>
     *     <p><b><i>Рекомендации</i></b>:</p>
     *     <li>Должен быть максимально коротким;</li>
     *     <li>При формировании простых механизмов поиска необходимо использовать подходящие по смыслу методы: {@link By#tagName(String)}, {@link By#id(String)} и т.д.</li>
     * </ul>
     * @return механизм поиска
     */
    public @NotNull By getBy() {
        return by;
    }

    /**
     * <p>Возвращает строку, которая является именем компонента.</p>
     * <ul>
     *     <p><b><i>Рекомендации</i></b>:</p>
     *     <li>Не должно содержать в себе значений, которые являются переменными для других экземпляром этого же компонента;</li>
     *     <li>Должно давать понять, каким компонентом в системе он является.</li><br>
     *     <p><b><i>Хорошие/плохие примеры</i></b>:</p>
     *     <li>Модальное окно/Закрытое модальное окно - в этом примере содержится информация о состоянии модального окна - эта информация должна содержаться в {@link Component#getCondition()};</li>
     *     <li>Элемент списка/Первый элемент списка - в этом примере содержится информация о порядковом номере элемента списка - эта информация должна содержаться в {@link #getIndex()};</li>
     *     <li>Кнопка/Кнопка "Войти" - в этом примере содержится информация о том, какой текст имеет кнопка - эта информация должна содержаться в {@link Component#getCondition()}.</li>
     * </ul>
     * @return имя компонента
     */
    public @NotNull String getName() {
        return name;
    }

    /**
     * <p>Возвращает логический индекс, обозначающий, какой порядковый номер должен быть у компонента.</p><br>
     * <p><b><i>Примечание</i></b>: логический индекс предопределяет то, с каким из найденных компонентов необходимо проводить дальнейшие манипуляции, но он не гарантирует доступность компонента с таким индексом.</p>
     * @return логический индекс
     */
    public int getIndex() {
        return index;
    }

    /**
     * <p>Возвращает строку формата: "<b>Имя</b>[<b>Индекс</b>]".</p>
     * <ul>
     *     <li><b>Индекс</b> имеет значение из {@link #getIndex()};</li>
     *     <li><b>Имя</b> имеет значение из {@link #getName()}.</li>
     * </ul>
     * @return форматированную строку
     */
    @Override
    public @NotNull String toString() {
        return String.format("%s[%d]", getName(), getIndex());
    }
}
