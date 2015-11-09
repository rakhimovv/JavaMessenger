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
public class ChatCreateCommand implements Command {

    static Logger log = LoggerFactory.getLogger(ChatCreateCommand.class);

    private MessageStore messageStore;
    private UserStore userStore;
    private String answer;

    public ChatCreateCommand(UserStore userStore, MessageStore messageStore) {
        this.messageStore = messageStore;
        this.userStore = userStore;
    }


    @Override
    public void execute(Session session, Message msg) {
        SendMessage chatCreateMsg = (SendMessage) msg;
        if (session.getSessionUser() != null) {
            // TODO: перенести данную логику в MessageStore
            Chat chat = new Chat();
            chat.addParticipant(session.getSessionUser().getId());

            boolean success = true;
            for (String arg : chatCreateMsg.getMessage().split(",")) {
                Long id = Long.parseLong(arg);
                if (userStore.getUserById(id) == null) {
                    // TODO: вернуть список несуществующих пользователей, а не только одного
                    answer = "User " + id + " doesn't exist.";
                    success = false;
                    break;
                } else {
                    chat.addParticipant(id);
                }
            }

            if (success) {
                messageStore.addChat(chat);
                answer = "The chat was created";
                log.info("Success chat_create: {}", chat);
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
