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
public class ChatFindCommand implements Command {

    static Logger log = LoggerFactory.getLogger(ChatListCommand.class);

    private MessageStore messageStore;
    private String answer;

    public ChatFindCommand(MessageStore messageStore) {
        this.messageStore = messageStore;
    }


    @Override
    public void execute(Session session, Message msg) {
        SendMessage chatFindMsg = (SendMessage) msg;
        if (session.getSessionUser() != null) {
            // TODO: поменять принимаемый формат и перенести логику
            String[] args = chatFindMsg.getMessage().split(">");
            Long chatId = Long.parseLong(args[0]);
            Chat chat = messageStore.getChatById(chatId);

            for (long msgId : chat.getMessageIds()) {
                SendMessage chatMsg = (SendMessage) messageStore.getMessageById(msgId);
                if (chatMsg.getMessage().contains(args[1])) {
                    answer += chatMsg.getMessage();
                    break;
                }
            }
            if (answer.isEmpty()) {
                answer = "Cant' find any message.";
            }
            log.info("Success chat_find: {}", chat);
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
