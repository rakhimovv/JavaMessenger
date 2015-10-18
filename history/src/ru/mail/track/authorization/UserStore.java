package ru.mail.track.authorization;

import ru.mail.track.session.User;

import java.io.IOException;


/**
 * Хорошая идея спрятать реализацию хранилища за интерфейсом
 * Тогда практически везде мы будем работать с UserStore, не знаю как он реализован внутри
 * <p>
 * Поменять код нам придется только в месте инициализации UserStore
 */
public interface UserStore {
    // проверить, есть ли пользователь с таким именем
    // если есть, вернуть true
    boolean isUserExist(String name);

    // Добавить пользователя в хранилище
    void addUser(User user) throws UserAlreadyExistsException, IOException;

    void addUserNickname(User user, String nickname);

    // Получить пользователя по имени и паролю
    User getUser(String name, String pass);
}
