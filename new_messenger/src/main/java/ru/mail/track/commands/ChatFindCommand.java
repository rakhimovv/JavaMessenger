package ru.mail.track.commands;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.mail.track.message.Chat;
import ru.mail.track.message.Message;
import ru.mail.track.message.MessageStore;
import ru.mail.track.message.SendMessage;
import ru.mail.track.session.Session;

/**
 * Найти подстроку в чате
 */
public class ChatFindCommand implements Command {

    static Logger log = LoggerFactory.getLogger(ChatListCommand.class);

    private MessageStore messageStore;
    private BaseCommandResult commandResult;

    public ChatFindCommand(MessageStore messageStore) {
        this.messageStore = messageStore;
        commandResult = new BaseCommandResult();
        commandResult.setStatus(CommandResult.Status.OK);
    }


    @Override
    public BaseCommandResult execute(Session session, Message msg) {
        SendMessage chatFindMsg = (SendMessage) msg;
        if (session.getSessionUser() != null) {
            String[] args = chatFindMsg.getMessage().split(">");
            Long chatId = Long.parseLong(args[0]);
            Chat chat = messageStore.getChatById(chatId);

            for (long msgId : chat.getMessageIds()) {
                SendMessage chatMsg = (SendMessage) messageStore.getMessageById(msgId);
                if (chatMsg.getMessage().contains(args[1])) {
                    commandResult.appendNewLine(chatMsg.getMessage());
                }
            }
            if (commandResult.getResponse().isEmpty()) {
                commandResult.setResponse("Cant' find any message.");
            }
            log.info("Success chat_find: {}", chat);
        } else {
            commandResult.setStatus(CommandResult.Status.NOT_LOGGINED);
            log.info("User isn't logged in.");
        }

        return commandResult;
    }
}
