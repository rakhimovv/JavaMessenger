package ru.mail.track.message;

import ru.mail.track.comands.Command;
import ru.mail.track.comands.CommandType;

import java.util.Map;

/**
 *
 */
public class HelpMessage extends Message {

    private Map<CommandType, Command> commands;

    public HelpMessage() {
        setType(CommandType.USER_HELP);
    }

    public HelpMessage(Map<CommandType, Command> commands) {
        this.commands = commands;
    }
}
