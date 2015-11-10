package ru.mail.track.commands;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.mail.track.message.*;
import ru.mail.track.session.Session;

/**
 * Поменять пароль
 */
public class UserPassCommand implements Command {

    static Logger log = LoggerFactory.getLogger(UserPassCommand.class);

    private BaseCommandResult commandResult;

    public UserPassCommand() {
        commandResult = new BaseCommandResult();
        commandResult.setStatus(CommandResult.Status.OK);
    }

    @Override
    public BaseCommandResult execute(Session session, Message msg) {
        LoginMessage userPassMsg = (LoginMessage) msg;
        User user;
        // TODO неправильная логика команды и странный вывод при вызове user_info
        if ((user = session.getSessionUser()) != null) {
            user.setPass(userPassMsg.getPass());
            commandResult.setResponse("The password changed.");
            log.info("Success set_pass: {}", user);
        } else {
            commandResult.setStatus(CommandResult.Status.NOT_LOGGINED);
            log.info("User isn't logged in.");
        }

        return commandResult;
    }
}
