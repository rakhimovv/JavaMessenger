package ru.mail.track.authorization;

import ru.mail.track.session.User;

import java.io.IOException;
import java.util.Scanner;
import java.io.Console;

public class AuthorizationService {

    private UserStore userStore;
    private Scanner scanner;
    private Console console;

    public AuthorizationService(UserStore userStore) {
        this.userStore = userStore;
        scanner = new Scanner(System.in);
        console = System.console();
    }

    public User login(String name, String password) {
        if (userStore.isUserExist(name)) {
            User currentUser = userStore.getUser(name, password);
            if (currentUser == null) {
                System.out.println("Error. Wrong password.");
            } else {
                return currentUser;
            }
        } else {
            System.out.println("Error. The user with this name doesn't exist.");
        }
        return null;
    }

    public User creatUser() throws UserAlreadyExistsException, IOException {
        System.out.print("Type your new login: ");
        String name = scanner.next();
        if (userStore.isUserExist(name)) {
            throw new UserAlreadyExistsException();
        }
        System.out.print("Type your new password: ");
        String pass = null;
        if (console != null) {
            pass = new String(console.readPassword());
        } else {
            pass = scanner.next();
        }
        User newUser = new User(name, pass, name);
        userStore.addUser(newUser);
        return newUser;
    }
}
