package ru.mail.track.message.result;

import ru.mail.track.commands.base.CommandType;
import ru.mail.track.message.Message;
import ru.mail.track.message.User;


public class UserInfoResultMessage extends Message {
    private User user;

    public UserInfoResultMessage() {
        setType(CommandType.USER_INFO_RESULT);
    }

    public UserInfoResultMessage(User user) {
        this();
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "UserInfoResultMessage{" +
                "user=" + user +
                "} " + super.toString();
    }
}