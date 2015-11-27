package ru.mail.track.message.result;

import ru.mail.track.commands.base.CommandType;
import ru.mail.track.message.Message;

import java.util.List;

public class HelpResultMessage extends Message {
    private List<String> helpContent;

    public HelpResultMessage() {
        setType(CommandType.HELP_RESULT);
    }

    public HelpResultMessage(List<String> helpContent) {
        this();
        this.helpContent = helpContent;
    }

    public List<String> getHelpContent() {
        return helpContent;
    }

    public void setHelpContent(List<String> helpContent) {
        this.helpContent = helpContent;
    }

    @Override
    public String toString() {
        return "HelpResultMessage{" +
                "helpContent=" + helpContent +
                "} " + super.toString();
    }
}