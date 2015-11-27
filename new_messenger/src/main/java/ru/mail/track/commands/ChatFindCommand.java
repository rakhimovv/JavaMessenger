package ru.mail.track.commands;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.mail.track.commands.base.Command;
import ru.mail.track.commands.base.CommandResultState;
import ru.mail.track.message.*;
import ru.mail.track.message.result.*;
import ru.mail.track.session.Session;

import java.util.ArrayList;
import java.util.List;

/**
 * Найти подстроку в чате
 */
public class ChatFindCommand extends Command {

    static Logger log = LoggerFactory.getLogger(ChatListCommand.class);

    private UserStore userStore;
    private MessageStore messageStore;

    public ChatFindCommand() {
        super();
        name = "chat_find";
        description = "<chat_id> <regex> Поиск в чате подстроки, " +
                "соответсвующей регулярному выражению (только для залогиненных пользователей)";
    }

    public ChatFindCommand(UserStore userStore, MessageStore messageStore) {
        this();
        this.userStore = userStore;
        this.messageStore = messageStore;
    }

    @Override
    public Message execute(Session session, Message msg) {
        if (session.getSessionUser() == null) {
            log.info("User isn't logged in.");
            return new CommandResultMessage(CommandResultState.NOT_LOGGED, "You need to login.");
        }

        SendMessage chatFindMsg = (SendMessage) msg;

        String[] args = chatFindMsg.getMessage().split(">");
        Long chatId = Long.parseLong(args[0]);
        Chat chat = messageStore.getChatById(chatId);

        if (chat != null) {
            List<String> messages = new ArrayList<>();

            for (long msgId : chat.getMessageIds()) {
                SendMessage chatMsg = (SendMessage) messageStore.getMessageById(msgId);
                if (chatMsg.getMessage().contains(args[1])) {
                    messages.add(userStore.getUserById(chatMsg.getSender()).getName() + ": " +
                            chatMsg.getMessage());
                }
            }
            if (messages.isEmpty()) {
                return new CommandResultMessage(CommandResultState.FAILED, "Can't find any messages.");
            }
            log.info("Success chat_find: {}", chat);
            return new ChatFindResultMessage(messages);
        }

        return new CommandResultMessage(CommandResultState.FAILED, "Chat isn't exists.");
    }
}
