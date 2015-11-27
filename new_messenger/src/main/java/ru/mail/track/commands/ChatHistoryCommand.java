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
 * Вывести историю сообщений чата
 */
public class ChatHistoryCommand extends Command {

    static Logger log = LoggerFactory.getLogger(ChatListCommand.class);

    private MessageStore messageStore;
    private UserStore userStore;

    public ChatHistoryCommand() {
        super();
        name = "chat_history";
        description = "<chat_id> Список сообщений из указанного чата (только для залогиненных пользователей)";
    }

    public ChatHistoryCommand(UserStore userStore, MessageStore messageStore) {
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

        SendMessage chatHistoryMsg = (SendMessage) msg;
        Long chatId = Long.parseLong(chatHistoryMsg.getMessage());
        Chat chat = messageStore.getChatById(chatId);

        if (chat == null) {
            return new CommandResultMessage(CommandResultState.FAILED, "Chat isn't exists.");
        }

        if (!chat.hasParticipant(chatHistoryMsg.getSender())) {
            return new CommandResultMessage(CommandResultState.FAILED,
                    "You don't belong to participants of this chat.");
        }

        List<String> messages = new ArrayList<>();
        List<Long> msgs = messageStore.getMessagesFromChat(chatId);
        for (Long id : msgs) {
            SendMessage chatMessage = (SendMessage) messageStore.getMessageById(id);
            messages.add(userStore.getUserById(chatMessage.getSender()).getName() + ": " +
                    chatMessage.getMessage());
        }
        if (!messages.isEmpty()) {
            log.info("Success chat_history: {}", chat);
            return new ChatHistoryResultMessage(messages);
        }
        return new CommandResultMessage(CommandResultState.OK, "Chat is empty.");
    }
}
