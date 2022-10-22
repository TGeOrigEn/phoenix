package geoMeta;

import org.untitled.phoenix.component.Component;
import org.untitled.phoenix.component.Description;
import org.gems.WebComponent;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.By;

public final class AuthorizationForm extends Component {

    private static final Description LOGIN_BUTTON_DESCRIPTION = new Description(By.cssSelector("button[class='btn btn-primary btn-block']"), "Кнопка 'Войти'");

    private static final Description USERNAME_FILED_DESCRIPTION = new Description(By.cssSelector("input[id='Username']"), "Поле 'Имя пользователя'");

    private static final Description PASSWORD_FILED_DESCRIPTION = new Description(By.cssSelector("input[id='Password']"), "Поле 'Пароль'");

    private final WebComponent usernameField;

    private final WebComponent passwordField;

    private final WebComponent loginButton;

    public AuthorizationForm() {
        usernameField = findInside(() -> new WebComponent(USERNAME_FILED_DESCRIPTION));
        passwordField = findInside(() -> new WebComponent(PASSWORD_FILED_DESCRIPTION));
        loginButton = findInside(() -> new WebComponent(LOGIN_BUTTON_DESCRIPTION));
    }

    @Override
    protected @NotNull Description initialize() {
        return new Description(By.cssSelector("section[class='auth-form']"), "Форма авторизации");
    }

    public void logIn(String username, String password)
    {
        usernameField.toAction().setValue(username);
        passwordField.toAction().setValue(password);
        loginButton.toAction().click();
    }
}
