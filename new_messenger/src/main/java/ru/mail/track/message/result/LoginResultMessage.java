package ru.mail.track.message.result;

import ru.mail.track.commands.base.CommandType;
import ru.mail.track.message.Message;

public class LoginResultMessage extends Message {
    private String login;
    private String pass;
    private Long userId;

    public LoginResultMessage() {
        setType(CommandType.LOGIN_RESULT);
    }

    public LoginResultMessage(String login, Long userId) {
        this();
        this.login = login;
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    @Override
    public String toString() {
        return "LoginResultMessage{" +
                "login='" + login + '\'' +
                ", userId=" + userId +
                "} " + super.toString();
    }
}