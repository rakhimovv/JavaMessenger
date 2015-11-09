package ru.mail.track.comands;

import java.io.IOException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.mail.track.message.SendMessage;
import ru.mail.track.message.Message;
import ru.mail.track.session.Session;

/**
 * Вывести помощь
 */
public class HelpCommand implements Command {

    static Logger log = LoggerFactory.getLogger(HelpCommand.class);

    private Map<CommandType, Command> commands;
    private String answer;

    public HelpCommand(Map<CommandType, Command> commands) {
        this.commands = commands;
    }


    @Override
    public void execute(Session session, Message msg) {
        /**
         * В простом случае просто выводим данные на консоль
         * Если будем работать через сеть, то команде придется передать также объект для работы с сетью
         */

        for (Map.Entry<CommandType, Command> entry : commands.entrySet()) {
            answer += entry.getKey() + "\n";
        }

        try {
            SendMessage sendMessage = new SendMessage();
            sendMessage.setType(CommandType.MSG_SEND);
            sendMessage.setChatId(0L);
            sendMessage.setMessage(answer);
            session.getConnectionHandler().send(sendMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
