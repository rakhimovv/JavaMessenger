package ru.mail.track;

import java.util.Scanner;
import java.io.Console;

public class AuthorizationService {

    private User currentUser;
    private UserStore userStore;
    private Scanner scanner;
    private Console console;

    public AuthorizationService(UserStore userStore) {
        this.userStore = userStore;
        scanner = new Scanner(System.in);
        console = System.console();
    }

    void startAuthorization() {
        while (isLogin()) {
            String command = null;
            while (command == null || !(command.equals("l") || command.equals("s"))) {
                System.out.println();
                System.out.print("Would you like to log in or sign up? [l/s] ");
                command = scanner.next();
                switch (command) {
                    case "l":
                        login();
                        break;
                    case "s":
                        creatUser();
                        break;
                    default:
                        System.out.println("Error. Wrong command. Try again.");
                        break;
                }
            }
        }
    }

    User login() {
        // 1. Ask for name
        // 2. Ask for password
        // 3. Ask UserStore for user:  userStore.getUser(name, pass)
        System.out.print("Login: ");
        String name = scanner.next();
        if (userStore.isUserExist(name)) {
            String pass = null;
            while (true) {
                System.out.print("Password: ");
                if (console != null) {
                    pass = new String(console.readPassword());
                } else {
                    pass = scanner.next();
                }
                if ((currentUser = userStore.getUser(name, pass)) == null) {
                    System.out.println("Error. Wrong password. Try again.");
                } else {
                    break;
                }
            }
            System.out.println();
            System.out.println("Hello, " + currentUser.getName() + "!");
        } else {
            System.out.println("Error. The user with this name doesn't exist.");
        }
        return currentUser;
    }

    User creatUser() {
        // 1. Ask for name
        // 2. Ask for pass
        // 3. Add user to UserStore: userStore.addUser(user)
        System.out.print("Type your new login: ");
        String name = scanner.next();
        System.out.print("Type your new password: ");
        String pass = null;
        if (console != null) {
            pass = new String(console.readPassword());
        } else {
            pass = scanner.next();
        }
        User newUser = new User(name, pass);
        userStore.addUser(newUser);
        return newUser;
    }

    boolean isLogin() {
        if (currentUser == null) {
            return true;
        }
        return false;
    }
}
