package ru.mail.track.commands;

import ru.mail.track.messenger.HistoryStore;
import ru.mail.track.messenger.Message;
import ru.mail.track.session.Session;

/**
 * Вывести историю сообщений
 */
public class FindCommand implements Command {

    private HistoryStore historyStore;

    public FindCommand(HistoryStore historyStore) {
        this.historyStore = historyStore;
    }

    @Override
    public void execute(Session session, String[] args) throws WrongArgumentsException {
        if(args.length == 2) {
            Message msg = historyStore.findMessage(args[1]);
            if(msg != null) {
                System.out.println(msg.getTime() + " > " + msg.getText());
            } else {
                System.out.println("Can't find this word in history.");
            }
        } else {
            throw new WrongArgumentsException();
        }
    }
}
