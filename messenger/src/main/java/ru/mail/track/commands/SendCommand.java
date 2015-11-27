package ru.mail.track.commands;

import java.io.IOException;
import java.util.List;

import ru.mail.track.message.Chat;
import ru.mail.track.message.Message;
import ru.mail.track.message.MessageStore;
import ru.mail.track.message.SendMessage;
import ru.mail.track.net.SessionManager;
import ru.mail.track.session.Session;

/**
 *
 */
public class SendCommand implements Command {

    private MessageStore messageStore;
    private SessionManager sessionManager;
    private BaseCommandResult commandResult;

    public SendCommand(SessionManager sessionManager, MessageStore messageStore) {
        this.sessionManager = sessionManager;
        this.messageStore = messageStore;
        commandResult = new BaseCommandResult();
        commandResult.setStatus(CommandResult.Status.OK);
    }

    @Override
    public BaseCommandResult execute(Session session, Message message) {

        SendMessage sendMessage = (SendMessage) message;
        Chat chat = messageStore.getChatById(sendMessage.getChatId());
        List<Long> parts = chat.getParticipantIds();
        try {
            for (Long userId : parts) {
                Session userSession = sessionManager.getSessionByUser(userId);
                if (userSession != null) {
                    userSession.getConnectionHandler().send(message);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return commandResult;
    }
}
