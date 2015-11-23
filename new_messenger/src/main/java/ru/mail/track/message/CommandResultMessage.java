package ru.mail.track.message;

import ru.mail.track.message.SendMessage;

/**
 *
 */
public class CommandResultMessage extends SendMessage {

    public enum Status {
        OK,
        FAILED,
        NOT_LOGGINED,
    }

    private Status status;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getResponse() {
        return getMessage();
    }

    public void appendResponse(String response) {
        setMessage(getMessage() + response + "\n");
    }

    public void setResponse(String response) {
        setMessage(response + "\n");
    }
}
