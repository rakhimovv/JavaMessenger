package ru.mail.track.message;

import ru.mail.track.commands.CommandType;

/**
 *
 */
public class SendMessage extends Message {

    private Long chatId;
    private String message;

    public SendMessage() {
        setType(CommandType.MSG_SEND);
    }

    public SendMessage(Long chatId, String message) {
        this.chatId = chatId;
        this.message = message;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SendMessage that = (SendMessage) o;

        if (chatId != null ? !chatId.equals(that.chatId) : that.chatId != null) return false;
        return !(message != null ? !message.equals(that.message) : that.message != null);

    }

    @Override
    public int hashCode() {
        int result = chatId != null ? chatId.hashCode() : 0;
        result = 31 * result + (message != null ? message.hashCode() : 0);
        return result;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
