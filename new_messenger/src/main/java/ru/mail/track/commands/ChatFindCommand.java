package ru.mail.track.commands;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.mail.track.commands.base.Command;
import ru.mail.track.message.*;
import ru.mail.track.session.Session;

/**
 * Найти подстроку в чате
 */
public class ChatFindCommand extends Command {

    static Logger log = LoggerFactory.getLogger(ChatListCommand.class);

    private MessageStore messageStore;

    public ChatFindCommand() {
        super();
        name = "chat_find";
        description = "<chat_id> <regex> Поиск в чате подстроки, " +
                "соответсвующей регулярному выражению (только для залогиненных пользователей)";
    }

    public ChatFindCommand(MessageStore messageStore) {
        this();
        this.messageStore = messageStore;
    }

    @Override
    public CommandResultMessage execute(Session session, Message msg) {
        SendMessage chatFindMsg = (SendMessage) msg;

        CommandResultMessage commandResult = new CommandResultMessage();
        commandResult.setStatus(CommandResultMessage.Status.OK);

        if (session.getSessionUser() != null) {
            String[] args = chatFindMsg.getMessage().split(">");
            Long chatId = Long.parseLong(args[0]);
            Chat chat = messageStore.getChatById(chatId);

            for (long msgId : chat.getMessageIds()) {
                SendMessage chatMsg = (SendMessage) messageStore.getMessageById(msgId);
                if (chatMsg.getMessage().contains(args[1])) {
                    commandResult.appendResponse(chatMsg.getMessage());
                }
            }
            if (commandResult.getResponse().isEmpty()) {
                commandResult.setResponse("Cant' find any message.");
            }
            log.info("Success chat_find: {}", chat);
        } else {
            commandResult.setStatus(CommandResultMessage.Status.NOT_LOGGINED);
            log.info("User isn't logged in.");
        }

        return commandResult;
    }
}
