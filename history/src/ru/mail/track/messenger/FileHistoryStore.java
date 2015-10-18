package ru.mail.track.messenger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.LinkedList;
import java.util.Scanner;
import java.nio.file.StandardOpenOption;
import java.io.PrintWriter;

public class FileHistoryStore implements HistoryStore {

    private Path path;
    private List<Message> messages;

    public FileHistoryStore(Path path) throws IOException {
        this.path = path;
        this.messages = new LinkedList<Message>();
        if (Files.exists(path)) {
            try (Scanner scanner = new Scanner(Files.newInputStream(path, StandardOpenOption.CREATE))) {
                while (scanner.hasNextLine()) {
                    String serializedMessage = scanner.nextLine();
                    messages.add(new Message(serializedMessage));
                }
            }
        }
    }

    @Override
    public void addMessage(Message msg) throws IOException {
        messages.add(msg);
        try (PrintWriter out = new PrintWriter(Files.newOutputStream(path,
                StandardOpenOption.CREATE, StandardOpenOption.APPEND))) {
            out.println(msg.toString());
        }
    }

    @Override
    public Message findMessage(String key) {
        for (int i = 0; i < messages.size(); i++) {
            if (messages.get(i).getText().contains(key)) {
                return messages.get(i);
            }
        }
        return null;
    }

    @Override
    public List<Message> getMessages() {
        return messages;
    }
}
