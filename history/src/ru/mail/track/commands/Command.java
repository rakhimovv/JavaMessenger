package ru.mail.track.commands;

import ru.mail.track.authorization.UserAlreadyExistsException;
import ru.mail.track.session.Session;

import java.io.IOException;

/**
 * Интерфейс для всех команд
 *
 *  То есть, даже имея возможность определить здесь абстрактный метод execute() я предпочту интерфейс
 * потому что интерфейс определяет поведение (свойство)
 *
 * А цель абстрактного класса - переиспользование кода
 */
public interface Command {


    /**
     * Здесь можно возвращать результат, подумайте как лучше сделать
     * результат желательно инкапсулировать в неком объекте Result
     *
     * В качестве пример оставлю void
     */
    void execute(Session session, String[] args) throws UserAlreadyExistsException, WrongArgumentsException, IOException;
}
