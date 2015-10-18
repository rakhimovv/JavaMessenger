package ru.mail.track.messenger;

import java.io.IOException;
import java.util.List;

public interface HistoryStore {

    // Добавить сообщение пользователя
    void addMessage(Message msg) throws IOException;

    List<Message> getMessages();

    Message findMessage(String key);
}
