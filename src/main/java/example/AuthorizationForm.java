package example;

import org.untitled.phoenix.component.Component;
import org.untitled.phoenix.component.Description;
import org.gems.WebComponent;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.By;

public final class AuthorizationForm extends Component {

    // Описание для внутренних компонентов

    private static final @NotNull Description LOGIN_BUTTON_DESCRIPTION = new Description(By.cssSelector("button[class='btn btn-primary btn-block']"), "Кнопка 'Войти'");

    private static final @NotNull Description USERNAME_FILED_DESCRIPTION = new Description(By.cssSelector("input[id='Username']"), "Поле 'Имя пользователя'");

    private static final @NotNull Description PASSWORD_FILED_DESCRIPTION = new Description(By.cssSelector("input[id='Password']"), "Поле 'Пароль'");

    // Внутренние компоненты

    private final @NotNull WebComponent usernameField;

    private final @NotNull WebComponent passwordField;

    private final @NotNull WebComponent loginButton;

    // Конструктор компонента

    public AuthorizationForm() {
        usernameField = findInside(() -> new WebComponent(USERNAME_FILED_DESCRIPTION));
        passwordField = findInside(() -> new WebComponent(PASSWORD_FILED_DESCRIPTION));
        loginButton = findInside(() -> new WebComponent(LOGIN_BUTTON_DESCRIPTION));
    }

    // Переопределённый метод инициализации описания компонента

    @Override
    protected @NotNull Description initialize() {
        return new Description(By.cssSelector("section[class='auth-form']"), "Форма авторизации");
    }

    // Метод авторизации

    public void logIn(@NotNull String username, @NotNull String password) {
        usernameField.toAction().setValue(username);
        passwordField.toAction().setValue(password);
        loginButton.toAction().click();
    }
}
