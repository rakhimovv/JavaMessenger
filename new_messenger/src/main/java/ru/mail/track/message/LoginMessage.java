package ru.mail.track.message;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import ru.mail.track.commands.base.CommandType;

/**
 *
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "objectType")
public class LoginMessage extends Message {
    public enum ArgType {
        LOGIN,
        CREAT_USER,
        SELF_INFO,
        ID_INFO,
    }

    @JsonProperty
    private ArgType argType;

    @JsonProperty
    private String login;

    @JsonProperty
    private String pass;

    @JsonProperty
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

    @Override
    public String toString() {
        return "LoginMessage{" +
                "login='" + login + '\'' +
                ", pass='" + pass + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LoginMessage that = (LoginMessage) o;

        /*if (login != null ? !login.equals(that.login) : that.login != null) return false;
        return !(pass != null ? !pass.equals(that.pass) : that.pass != null);*/
        return (login != null ? !login.equals(that.login) : that.login != null) &&
                !(pass != null ? !pass.equals(that.pass) : that.pass != null);
    }

    @Override
    public int hashCode() {
        int result = login != null ? login.hashCode() : 0;
        result = 31 * result + (pass != null ? pass.hashCode() : 0);
        return result;
    }
}
