package ru.mail.track.commands;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.mail.track.message.*;
import ru.mail.track.session.Session;

import java.io.IOException;
import java.util.List;

/**
 * Вывести историю сообщений чата
 */
public class ChatHistoryCommand implements Command {

    static Logger log = LoggerFactory.getLogger(ChatListCommand.class);

    private MessageStore messageStore;
    private BaseCommandResult commandResult;

    public ChatHistoryCommand(MessageStore messageStore) {
        this.messageStore = messageStore;
        commandResult = new BaseCommandResult();
        commandResult.setStatus(CommandResult.Status.OK);
    }


    @Override
    public BaseCommandResult execute(Session session, Message msg) {
        SendMessage chatHistoryMsg = (SendMessage) msg;
        if (session.getSessionUser() != null) {
            Long chatId = Long.parseLong(chatHistoryMsg.getMessage());
            Chat chat = messageStore.getChatById(chatId);
            if (chat == null) {
                commandResult.setResponse("This chat doesn't exist.");
            } else {
                List<Long> messages = messageStore.getMessagesFromChat(chatId);
                for (Long id : messages) {
                    SendMessage chatMessage = (SendMessage) messageStore.getMessageById(id);
                    commandResult.appendNewLine(chatMessage.getMessage());
                }
                log.info("Success chat_history: {}", chat);
            }
        } else {
            commandResult.setStatus(CommandResult.Status.NOT_LOGGINED);
            log.info("User isn't logged in.");
        }

        return commandResult;
    }
}
