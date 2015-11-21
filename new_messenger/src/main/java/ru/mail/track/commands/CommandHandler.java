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
        log.info("onMessage: {} type {}", message, message.getType());
        BaseCommandResult commandResult = cmd.execute(session, message);

        switch (commandResult.getStatus()) {
            case OK:
                break;
            case NOT_LOGGINED:
                commandResult.setResponse("You must log in.");
                break;
            case FAILED:
                break;
            default:
        }

        // Отправить текстовый результат выполнения команды
        try {
            SendMessage sendMessage = new SendMessage();
            sendMessage.setType(CommandType.MSG_SEND);
            sendMessage.setChatId(0L);
            sendMessage.setMessage("\n\n" + commandResult.getResponse() + "\n\n");
            session.getConnectionHandler().send(sendMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
