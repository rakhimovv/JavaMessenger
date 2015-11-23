package ru.mail.track.commands.base;

import ru.mail.track.message.CommandResultMessage;
import ru.mail.track.message.Message;
import ru.mail.track.session.Session;

/**
 * Интерфейс для всех команд
 *
 *  То есть, даже имея возможность определить здесь абстрактный метод execute() я предпочту интерфейс
 * потому что интерфейс определяет поведение (свойство)
 *
 * А цель абстрактного класса - переиспользование кода
 */
public abstract class Command {

    protected String name;
    protected String description;

    public abstract CommandResultMessage execute(Session session, Message message);

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return name + " " + description;
    }

}
