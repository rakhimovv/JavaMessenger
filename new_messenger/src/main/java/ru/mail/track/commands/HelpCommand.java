package ru.mail.track.commands;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.mail.track.message.Message;
import ru.mail.track.session.Session;

import java.util.Map;

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
    public CommandResultMessage execute(Session session, Message msg) {
        /**
         * В простом случае просто выводим данные на консоль
         * Если будем работать через сеть, то команде придется передать также объект для работы с сетью
         */
        CommandResultMessage commandResult = new CommandResultMessage();
        commandResult.setStatus(CommandResultMessage.Status.OK);

        for (Map.Entry<CommandType, Command> entry : commands.entrySet()) {
            commandResult.appendResponse(entry.getKey().toString());
        }

        return commandResult;
    }
}
