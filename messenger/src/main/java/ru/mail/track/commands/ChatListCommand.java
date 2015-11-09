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
public class ChatListCommand implements Command {

    static Logger log = LoggerFactory.getLogger(ChatListCommand.class);

    private MessageStore messageStore;
    private String answer;

    public ChatListCommand(MessageStore messageStore) {
        this.messageStore = messageStore;
    }


    @Override
    public void execute(Session session, Message msg) {
        SendMessage chatListMsg = (SendMessage) msg;
        if (session.getSessionUser() != null) {
            List<Long> chatIds = messageStore.getChatsByUserId(session.getSessionUser().getId());
            if (chatIds.isEmpty()) {
                answer += "You have no any chats:";
            } else {
                answer += "Your chats:";
                for (Long chatId : chatIds) {
                    answer += " " + chatId;
                }
            }
            log.info("Success chat_list: {}", session.getSessionUser());
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
