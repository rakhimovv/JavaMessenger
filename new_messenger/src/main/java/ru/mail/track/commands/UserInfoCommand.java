package ru.mail.track.commands;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.mail.track.commands.base.Command;
import ru.mail.track.message.*;
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
    public CommandResultMessage execute(Session session, Message msg) {
        CommandResultMessage commandResult = new CommandResultMessage();
        commandResult.setStatus(CommandResultMessage.Status.OK);

        LoginMessage userInfoMsg = (LoginMessage) msg;
        switch (userInfoMsg.getArgType()) {
            case SELF_INFO:
                if (session.getSessionUser() != null) {
                    commandResult.setResponse("login: " + session.getSessionUser().getName());
                    commandResult.appendResponse("password: " + session.getSessionUser().getPass());
                    log.info("Success self_info: {}", session.getSessionUser());
                } else {
                    commandResult.setStatus(CommandResultMessage.Status.NOT_LOGGINED);
                    log.info("User isn't logged in.");
                }
                break;
            case ID_INFO:
                User user = userStore.getUserById(userInfoMsg.getUserId());
                if (user != null) {
                    commandResult.setResponse("login: " + user.getName());
                    commandResult.appendResponse("password: " + user.getPass());
                    log.info("Success id_info: {}", userStore.getUserById(userInfoMsg.getUserId()));
                } else {
                    log.info("Wrong userId: {}", userInfoMsg.getUserId());
                    commandResult.setResponse("Wrong user id.");
                }
                break;
            default:
                log.info("Wrong argType: {}", userInfoMsg.getArgType());
        }

        return commandResult;
    }
}
