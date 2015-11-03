package ru.mail.track.commands;

import ru.mail.track.messenger.HistoryStore;
import ru.mail.track.messenger.Message;
import ru.mail.track.session.Session;

import java.util.List;

/**
 * Вывести историю сообщений
 */
public class HistoryCommand implements Command {

    private List<Message> messages;

    public HistoryCommand(HistoryStore historyStore) {
        this.messages = historyStore.getMessages();
    }

    @Override
    public void execute(Session session, String[] args) throws WrongArgumentsException {
        int n;
        if(args.length == 2) {
            n = Integer.parseInt(args[1]);
        } else if(args.length == 1) {
            n = messages.size();
        }
        else {
            throw new WrongArgumentsException();
        }

        if(messages.size() < n) {
            throw new WrongArgumentsException();
        }

        for (int i = messages.size() - n; i < messages.size(); i++) {
            System.out.println(messages.get(i).toString());
        }
    }
}
