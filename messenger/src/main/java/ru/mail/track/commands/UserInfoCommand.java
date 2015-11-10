package ru.mail.track.commands;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.mail.track.message.*;
import ru.mail.track.session.Session;

/**
 * Выполняем информацию о пользователе
 */
public class UserInfoCommand implements Command {

    static Logger log = LoggerFactory.getLogger(UserInfoCommand.class);

    private UserStore userStore;
    private BaseCommandResult commandResult;

    public UserInfoCommand(UserStore userStore) {
        this.userStore = userStore;
        commandResult = new BaseCommandResult();
        commandResult.setStatus(CommandResult.Status.OK);
    }


    @Override
    public BaseCommandResult execute(Session session, Message msg) {
        // TODO почему-то с каждым вызовом команды ее вывод накапливается, разобраться с этим
        LoginMessage userInfoMsg = (LoginMessage) msg;
        switch (userInfoMsg.getArgType()) {
            case SELF_INFO:
                if (session.getSessionUser() != null) {
                    commandResult.appendNewLine("login: " + session.getSessionUser().getName());
                    commandResult.appendNewLine("password: " + session.getSessionUser().getPass());
                    log.info("Success self_info: {}", session.getSessionUser());
                } else {
                    commandResult.setStatus(CommandResult.Status.NOT_LOGGINED);
                    log.info("User isn't logged in.");
                }
                break;
            case ID_INFO:
                User user = userStore.getUserById(userInfoMsg.getUserId());
                if (user != null) {
                    commandResult.appendNewLine("login: " + user.getName());
                    commandResult.appendNewLine("password: " + user.getPass());
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
