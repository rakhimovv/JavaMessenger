package ru.mail.track.commands;

import ru.mail.track.commands.base.Command;
import ru.mail.track.message.*;
import ru.mail.track.net.SessionManager;
import ru.mail.track.session.Session;

import java.io.IOException;
import java.util.List;

/**
 *
 */
public class ChatSendCommand extends Command {

    private MessageStore messageStore;
    private UserStore userStore;
    private SessionManager sessionManager;

    public ChatSendCommand() {
        super();
        name = "chat_send";
        description = "<id> <message> Отправить сообщение в заданный чат, " +
                "чат должен быть в списке чатов пользователя (только для залогиненных пользователей)";
    }

    public ChatSendCommand(SessionManager sessionManager, UserStore userStore, MessageStore messageStore) {
        this();
        this.sessionManager = sessionManager;
        this.userStore = userStore;
        this.messageStore = messageStore;
    }

    @Override
    public CommandResultMessage execute(Session session, Message message) {
        CommandResultMessage commandResult = new CommandResultMessage();
        commandResult.setStatus(CommandResultMessage.Status.OK);

        SendMessage sendMessage = (SendMessage) message;

        if (sendMessage.getSender() == null) {
            sendMessage.setMessage(sendMessage.getMessage() + "\n");
        } else {
            sendMessage.setMessage(userStore.getUserById(sendMessage.getSender()).getName() + ": " +
                    sendMessage.getMessage() + "\n");
        }

        Chat chat = messageStore.getChatById(sendMessage.getChatId());
        messageStore.addMessage(sendMessage.getChatId(), sendMessage);

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
