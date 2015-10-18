package ru.mail.track.messenger;

import java.sql.Time;
import java.sql.Timestamp;

public class Message {
    private String text;
    private Timestamp time;

    public Message(String text, Timestamp time) {
        this.text = text;
        this.time = time;
    }

    public Message(String serializedUser) {
        this.time = Timestamp.valueOf(serializedUser.split(" > ")[0]);
        this.text = serializedUser.split(" > ")[1];
    }

    public String getText() {
        return text;
    }

    public void setName(String text) {
        this.text = text;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public String toString() {
        return time + " > " + text;
    }
}
