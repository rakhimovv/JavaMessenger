package ru.mail.track.session;


public class User {
    private String name;
    private String pass;
    private String nickname;

    public User(String name, String pass, String nickname) {
        this.name = name;
        this.pass = pass;
        this.nickname = nickname;
    }

    public User(String serializedUser) {
        this.name = serializedUser.split(" ")[0];
        this.pass = serializedUser.split(" ")[1];
        this.nickname = serializedUser.split(" ")[2];
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public boolean passwordIsCorrect(String pass) {
        return pass.equals(this.pass);
    }

    public String toString() {
        return name + " " + pass + " " + nickname;
    }
}
