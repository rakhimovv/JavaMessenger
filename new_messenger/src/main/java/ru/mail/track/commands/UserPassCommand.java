package ru.mail.track.commands;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.mail.track.commands.base.Command;
import ru.mail.track.message.CommandResultMessage;
import ru.mail.track.message.Message;
import ru.mail.track.message.SendMessage;
import ru.mail.track.message.UserStore;
import ru.mail.track.session.Session;

/**
 * Поменять пароль
 */
public class UserPassCommand extends Command {

    static Logger log = LoggerFactory.getLogger(UserPassCommand.class);

    private UserStore userStore;

    public UserPassCommand() {
        super();
        name = "user_pass";
        description = "<old_pass> <new_pass> Сменить пароль (только для залогиненных пользователей).";
    }

    public UserPassCommand(UserStore userStore) {
        this();
        this.userStore = userStore;
    }

    @Override
    public CommandResultMessage execute(Session session, Message msg) {
        CommandResultMessage commandResult = new CommandResultMessage();
        commandResult.setStatus(CommandResultMessage.Status.OK);

        SendMessage userPassMsg = (SendMessage) msg;
        if (session.getSessionUser() != null) {
            String[] args = userPassMsg.getMessage().split(">");
            if (session.getSessionUser().getPass().equals(args[0])) {
                session.getSessionUser().setPass(args[1]);
                userStore.updateUser(session.getSessionUser());
                commandResult.setResponse("The password changed.");
                log.info("Success set_pass: {}", session.getSessionUser());
            } else {
                commandResult.setResponse("Wrong old password.");
                log.info("set_pass: Wrong old password.");
            }
        } else {
            commandResult.setStatus(CommandResultMessage.Status.NOT_LOGGINED);
            log.info("User isn't logged in.");
        }

        return commandResult;
    }
}
