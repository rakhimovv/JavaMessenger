package ru.mail.track.commands;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.mail.track.message.*;
import ru.mail.track.session.Session;

import java.util.ArrayList;
import java.util.List;

/**
 * Создать новый чат
 */
public class ChatCreateCommand implements Command {

    static Logger log = LoggerFactory.getLogger(ChatCreateCommand.class);

    private MessageStore messageStore;
    private UserStore userStore;
    private BaseCommandResult commandResult;

    public ChatCreateCommand(UserStore userStore, MessageStore messageStore) {
        this.messageStore = messageStore;
        this.userStore = userStore;
        commandResult = new BaseCommandResult();
        commandResult.setStatus(CommandResult.Status.OK);
    }


    @Override
    public BaseCommandResult execute(Session session, Message msg) {
        SendMessage chatCreateMsg = (SendMessage) msg;
        if (session.getSessionUser() != null) {
            List<Long> participants = new ArrayList<>();
            boolean success = true;

            participants.add(session.getSessionUser().getId());
            for (String arg : chatCreateMsg.getMessage().split(",")) {
                Long id = Long.parseLong(arg);
                User user = userStore.getUserById(id);
                if (user == null) {
                    commandResult.appendNewLine("User " + id + " doesn't exist.");
                    success = false;
                } else if (!user.equals(session.getSessionUser())) {
                    // Защита от дурака, т.е. если среди id будет id пользователя
                    participants.add(id);
                }
            }

            if (success) {
                Chat chat = messageStore.addChat(participants);
                commandResult.setResponse("The chat was created");
                log.info("Success chat_create: {}", chat);
            }
        } else {
            commandResult.setStatus(CommandResult.Status.NOT_LOGGINED);
            log.info("User isn't logged in.");
        }

        return commandResult;
    }
}
