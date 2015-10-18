package ru.mail.track.authorization;

import ru.mail.track.session.User;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.io.PrintWriter;
import java.nio.file.StandardOpenOption;

public class FileUserStore implements UserStore {

    private Path path;
    private Map<String, User> users;

    public FileUserStore(Path path) throws IOException {
        this.path = path;
        this.users = new HashMap<>();
        if (Files.exists(path)) {
            try (Scanner scanner = new Scanner(Files.newInputStream(path, StandardOpenOption.CREATE))) {
                while (scanner.hasNextLine()) {
                    String serializedUser = scanner.nextLine();
                    User user = new User(serializedUser);
                    users.put(user.getName(), user);
                }
            }
        }
    }

    @Override
    public boolean isUserExist(String name) {
        return (users.containsKey(name));
    }

    @Override
    public void addUser(User user) throws UserAlreadyExistsException, IOException {
        if (isUserExist(user.getName()))
            throw new UserAlreadyExistsException();
        users.put(user.getName(), user);
        try (PrintWriter out = new PrintWriter(Files.newOutputStream(path,
                StandardOpenOption.CREATE, StandardOpenOption.APPEND))) {
            out.println(user.toString());
        }
    }

    @Override
    public void addUserNickname(User user, String nickname) {
        user.setNickname(nickname);

        // Дописать код.
        // Найти соответствующую строку в файле и заменить ее.
    }

    @Override
    public User getUser(String name, String pass) {
        User user = users.get(name);
        return ((user != null) && (user.passwordIsCorrect(pass))) ? user : null;
    }
}
