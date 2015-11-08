package ru.mail.track.message;

import ru.mail.track.comands.Command;
import ru.mail.track.comands.CommandType;

/**
 *
 */
public class HelpMessage extends Message {

    public HelpMessage() {
        setType(CommandType.USER_HELP);
    }

}
