package ru.mail.track;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.nio.file.Paths;

import ru.mail.track.authorization.AuthorizationService;
import ru.mail.track.authorization.FileUserStore;
import ru.mail.track.authorization.UserAlreadyExistsException;
import ru.mail.track.authorization.UserStore;

import ru.mail.track.commands.*;
import ru.mail.track.messenger.FileHistoryStore;
import ru.mail.track.messenger.HistoryStore;

import ru.mail.track.session.Session;


// Это псевдокод. Показывает работу паттерна Команда
public class Main {

    private static final String EXIT = "q|exit";

    public static void main(String[] args) throws UserAlreadyExistsException, WrongArgumentsException, IOException{

        Map<String, Command> commands = new HashMap<>();


        // В этом объекте хранится инфа о сесии
        // то есть текущее стостояние чата
        Session session = new Session();

        // Реализация интерфейса задается в одном месте
        UserStore userStore = new FileUserStore(Paths.get("users.txt"));
        HistoryStore historyStore = new FileHistoryStore(Paths.get("history.txt"));
        AuthorizationService authService = new AuthorizationService(userStore);

        //Создаем команды
        Command loginCommand = new LoginCommand(authService);
        Command helpCommand = new HelpCommand(commands);
        Command historyCommand = new HistoryCommand(historyStore);
        Command findCommand = new FindCommand(historyStore);
        Command userCommand = new UserCommand(userStore);

        commands.put("\\login", loginCommand);
        commands.put("\\help", helpCommand);
        commands.put("\\history", historyCommand);
        commands.put("\\find", findCommand);
        commands.put("\\user", userCommand);

        InputHandler handler = new InputHandler(session, commands, historyStore);

        Scanner scanner = new Scanner(System.in);
        while (true) {
            String line = scanner.nextLine();
            if (line != null && line.matches(EXIT)) {
                break;
            }
            handler.handle(line);
        }


    }
}
