package ru.mail.track.commands;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.mail.track.message.*;
import ru.mail.track.session.Session;

import java.io.IOException;
import java.util.List;

/**
 * Выполняем авторизацию по этой команде
 */
public class ChatHistoryCommand implements Command {

    static Logger log = LoggerFactory.getLogger(ChatListCommand.class);

    private MessageStore messageStore;
    private String answer;

    public ChatHistoryCommand(MessageStore messageStore) {
        this.messageStore = messageStore;
    }


    @Override
    public void execute(Session session, Message msg) {
        SendMessage chatHistoryMsg = (SendMessage) msg;
        if (session.getSessionUser() != null) {
            Long chatId = Long.parseLong(chatHistoryMsg.getMessage());
            Chat chat = messageStore.getChatById(chatId);
            if (chat == null) {
                answer = "This chat doesn't exist.";
            } else {
                List<Long> messages = messageStore.getMessagesFromChat(chatId);
                for (Long id : messages) {
                    SendMessage chatMessage = (SendMessage) messageStore.getMessageById(id);
                    answer += chatMessage.getMessage() + "\n";
                }
                log.info("Success chat_history: {}", chat);
            }
        } else {
            answer = "You are not logged in.";
            log.info("User isn't logged in.");
        }

        try {
            SendMessage sendMessage = new SendMessage();
            sendMessage.setType(CommandType.MSG_SEND);
            sendMessage.setChatId(0L);
            sendMessage.setMessage(answer + "\n");
            session.getConnectionHandler().send(sendMessage);
            answer = "";
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
