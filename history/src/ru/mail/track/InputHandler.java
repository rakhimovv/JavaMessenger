package ru.mail.track;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Map;

import ru.mail.track.authorization.UserAlreadyExistsException;
import ru.mail.track.commands.Command;
import ru.mail.track.commands.WrongArgumentsException;
import ru.mail.track.messenger.HistoryStore;
import ru.mail.track.messenger.Message;
import ru.mail.track.session.Session;

/**
 * Основная задача класса принимать на вход данные от пользователя и решать, что с ними делать
 * <p>
 * Можно крутить в цикле
 */
public class InputHandler {

    private Session session;

    private Map<String, Command> commandMap;

    private HistoryStore historyStore;


    public InputHandler(Session session, Map<String, Command> commandMap, HistoryStore historyStore) {
        this.session = session;
        this.commandMap = commandMap;
        this.historyStore = historyStore;
    }

    public void handle(String data) throws UserAlreadyExistsException, WrongArgumentsException, IOException {
        // проверяем на спецсимвол команды
        // Это пример!
        if (data.startsWith("\\")) {
            String[] tokens = data.split(" ");
            //System.out.println("args.length = " + tokens.length);
            // Получим конкретную команду, но нам не важно что за команда,
            // у нее есть метод execute()
            try {
                Command cmd = commandMap.get(tokens[0]);
                cmd.execute(session, tokens);
            } catch (NullPointerException ex) {
                System.out.println("Error. Wrong command.");
            }
        } else {
            //System.out.println(">" + data);
            if (session.getSessionUser() != null) {
                historyStore.addMessage(new Message(data, new Timestamp(System.currentTimeMillis())));
            }
        }
    }

}



