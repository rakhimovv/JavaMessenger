package ru.mail.track.message;

import ru.mail.track.comands.CommandType;

/**
 *
 */
public class LoginMessage extends Message {

    public final int LOGIN = 1;
    public final int CREAT_USER = 2;
    public final int SELF_INFO = 3;
    public final int ID_INFO = 4;
    private int argType;
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

    public int getArgType() {
        return argType;
    }

    public void setArgType(int argType) {
        this.argType = argType;
    }

    public Long getUserId() {
        return id;
    }

    public void setUserId(Long id) {
        this.id = id;
    }
}
