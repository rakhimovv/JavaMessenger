package ru.mail.track.message;

import ru.mail.track.commands.CommandType;

/**
 *
 */
public class LoginMessage extends Message {

    public enum ArgType {
        LOGIN,
        CREAT_USER,
        SELF_INFO,
        ID_INFO,
    }

    private ArgType argType;
    private String login;
    private String pass;
    private Long id;

    public LoginMessage() {
        setType(CommandType.USER_LOGIN);
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

    public ArgType getArgType() {
        return argType;
    }

    public void setArgType(ArgType argType) {
        this.argType = argType;
    }

    public Long getUserId() {
        return id;
    }

    public void setUserId(Long id) {
        this.id = id;
    }
}
