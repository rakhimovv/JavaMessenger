package ru.mail.track.commands;

/**
 *
 */
public class BaseCommandResult extends CommandResult {

    private String response = "";

    public String getResponse() {
        return response;
    }

    public void appendNewLine(String response) {
        this.response += response + "\n";
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
