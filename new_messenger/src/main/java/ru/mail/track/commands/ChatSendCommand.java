package ru.mail.track.commands;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.mail.track.commands.base.Command;
import ru.mail.track.commands.base.CommandResultState;
import ru.mail.track.message.*;
import ru.mail.track.message.result.*;
import ru.mail.track.net.SessionManager;
import ru.mail.track.session.Session;

import java.io.IOException;
import java.util.List;

/**
 *
 */
public class ChatSendCommand extends Command {
    static Logger log = LoggerFactory.getLogger(ChatSendCommand.class);

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
    public Message execute(Session session, Message message) {
        if (session.getSessionUser() == null) {
            return new CommandResultMessage(CommandResultState.NOT_LOGGED, "You need to login.");
        }

        SendMessage sendMessage = (SendMessage) message;

        Chat chat = messageStore.getChatById(sendMessage.getChatId());
        if (chat == null) {
            return new CommandResultMessage(CommandResultState.FAILED, "Chat with id " +
                    sendMessage.getChatId() + " doesn't exist.");
        }

        List<Long> parts = chat.getParticipantIds();
        if (!parts.contains(message.getSender())) {
            return new CommandResultMessage(CommandResultState.FAILED, "You can't send message to this chat.");
        }

        try {
            messageStore.addMessage(sendMessage.getChatId(), sendMessage);
            sendMessage.setMessage(userStore.getUserById(sendMessage.getSender()).getName() + ": " +
                    sendMessage.getMessage());
            for (Long userId : parts) {
                Session userSession = sessionManager.getSessionByUser(userId);
                if (userSession != null) {
                    userSession.getConnectionHandler().send(sendMessage);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
