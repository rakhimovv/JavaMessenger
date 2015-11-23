package ru.mail.track.commands;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.mail.track.commands.base.Command;
import ru.mail.track.message.*;
import ru.mail.track.session.Session;

import java.util.List;

/**
 * Вывести историю сообщений чата
 */
public class ChatHistoryCommand extends Command {

    static Logger log = LoggerFactory.getLogger(ChatListCommand.class);

    private MessageStore messageStore;

    public ChatHistoryCommand() {
        super();
        name = "chat_history";
        description = "<chat_id> Список сообщений из указанного чата (только для залогиненных пользователей)";
    }

    public ChatHistoryCommand(MessageStore messageStore) {
        this();
        this.messageStore = messageStore;
    }


    @Override
    public CommandResultMessage execute(Session session, Message msg) {
        CommandResultMessage commandResult = new CommandResultMessage();
        commandResult.setStatus(CommandResultMessage.Status.OK);
        SendMessage chatHistoryMsg = (SendMessage) msg;
        if (session.getSessionUser() != null) {
            Long chatId = Long.parseLong(chatHistoryMsg.getMessage());
            Chat chat = messageStore.getChatById(chatId);
            if (chat == null) {
                commandResult.setResponse("This chat doesn't exist.");
            } else {
                commandResult.setResponse("");
                List<Long> messages = messageStore.getMessagesFromChat(chatId);
                for (Long id : messages) {
                    SendMessage chatMessage = (SendMessage) messageStore.getMessageById(id);
                    commandResult.appendResponse(chatMessage.getMessage());
                }
                log.info("Success chat_history: {}", commandResult.getResponse());
            }
        } else {
            commandResult.setStatus(CommandResultMessage.Status.NOT_LOGGINED);
            log.info("User isn't logged in.");
        }

        return commandResult;
    }
}
