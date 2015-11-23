package ru.mail.track.commands;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.mail.track.message.Message;
import ru.mail.track.message.SendMessage;
import ru.mail.track.net.MessageListener;
import ru.mail.track.session.Session;

import java.io.IOException;
import java.util.Map;

/**
 *
 */
public class CommandHandler implements MessageListener {

    static Logger log = LoggerFactory.getLogger(CommandHandler.class);

    Map<CommandType, Command> commands;

    public CommandHandler(Map<CommandType, Command> commands) {
        this.commands = commands;
    }

    @Override
    public void onMessage(Session session, Message message) {
        Command cmd = commands.get(message.getType());
        if (session.getSessionUser() != null) {
            message.setSender(session.getSessionUser().getId());
        }
        log.info("Set sender:" + session.getSessionUser());

        log.info("onMessage: {} type {}", message, message.getType());

        CommandResultMessage result = cmd.execute(session, message);

        switch (result.getStatus()) {
            case OK:
                break;
            case NOT_LOGGINED:
                result.setResponse("You must log in.");
                break;
            case FAILED:
                break;
            default:
        }

        if (!result.getMessage().isEmpty()) {
            try {
                result.setMessage("\n" + result.getMessage() + "\n");
                session.getConnectionHandler().send(result);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
