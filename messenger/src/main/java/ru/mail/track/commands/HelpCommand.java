package ru.mail.track.commands;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.mail.track.message.Message;
import ru.mail.track.session.Session;

/**
 * Вывести помощь
 */
public class HelpCommand implements Command {

    static Logger log = LoggerFactory.getLogger(HelpCommand.class);

    private Map<CommandType, Command> commands;
    private String answer;
    private BaseCommandResult commandResult;

    public HelpCommand(Map<CommandType, Command> commands) {
        this.commands = commands;
        commandResult = new BaseCommandResult();
        commandResult.setStatus(CommandResult.Status.OK);
    }


    @Override
    public BaseCommandResult execute(Session session, Message msg) {
        /**
         * В простом случае просто выводим данные на консоль
         * Если будем работать через сеть, то команде придется передать также объект для работы с сетью
         */

        for (Map.Entry<CommandType, Command> entry : commands.entrySet()) {
            commandResult.appendNewLine(entry.getKey().toString());
        }

        return commandResult;
    }
}
