package ru.mail.track.commands;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.mail.track.message.*;
import ru.mail.track.session.Session;

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
            // TODO: перенести данную логику в MessageStore и исключить двойное попадения себя в чат, если юезр ошибся
            Chat chat = new Chat();
            chat.addParticipant(session.getSessionUser().getId());

            boolean success = true;
            for (String arg : chatCreateMsg.getMessage().split(",")) {
                Long id = Long.parseLong(arg);
                if (userStore.getUserById(id) == null) {
                    // TODO: вернуть список несуществующих пользователей, а не только одного
                    commandResult.setResponse("User " + id + " doesn't exist.");
                    success = false;
                    break;
                } else {
                    chat.addParticipant(id);
                }
            }

            if (success) {
                messageStore.addChat(chat);
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
