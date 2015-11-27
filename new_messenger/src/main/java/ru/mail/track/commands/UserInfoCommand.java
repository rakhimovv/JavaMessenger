package ru.mail.track.commands;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.mail.track.commands.base.Command;
import ru.mail.track.commands.base.CommandResultState;
import ru.mail.track.message.*;
import ru.mail.track.message.result.*;
import ru.mail.track.session.Session;

/**
 * Выполняем информацию о пользователе
 */
public class UserInfoCommand extends Command {

    static Logger log = LoggerFactory.getLogger(UserInfoCommand.class);

    private UserStore userStore;

    public UserInfoCommand() {
        super();
        name = "user_info";
        description = "<id> Получить всю информацию о пользователе, " +
                "без аргументов - о себе (только для залогиненных пользователей).";
    }

    public UserInfoCommand(UserStore userStore) {
        this();
        this.userStore = userStore;
    }

    @Override
    public Message execute(Session session, Message msg) {
        if (session.getSessionUser() == null) {
            log.info("User isn't logged in.");
            return new CommandResultMessage(CommandResultState.NOT_LOGGED, "You need to login.");
        }

        LoginMessage userInfoMsg = (LoginMessage) msg;
        User user;
        switch (userInfoMsg.getArgType()) {
            case SELF_INFO:
                user = session.getSessionUser();
                log.info("Success self_info: {}", session.getSessionUser());
                break;
            default:
                user = userStore.getUserById(userInfoMsg.getUserId());
                break;
        }

        return new UserInfoResultMessage(user);
    }
}
