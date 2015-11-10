package ru.mail.track.net;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.mail.track.commands.CommandType;
import ru.mail.track.message.*;

/**
 *
 */
public class StringProtocol implements Protocol {

    static Logger log = LoggerFactory.getLogger(StringProtocol.class);

    public static final String DELIMITER = ";";

    @Override
    public Message decode(byte[] bytes) {
        String str = new String(bytes);
        //log.info("decoded: {}", str);
        String[] tokens = str.split(DELIMITER);
        CommandType type = CommandType.valueOf(tokens[0]);
        switch (type) {
            case USER_HELP:
                SendMessage helpMessage = new SendMessage();
                helpMessage.setType(CommandType.USER_HELP);
                return helpMessage;
            case CHAT_LIST:
                SendMessage chatListMessage = new SendMessage();
                chatListMessage.setType(CommandType.CHAT_LIST);
                return chatListMessage;
            case CHAT_HISTORY:
                SendMessage chatHistoryMessage = new SendMessage();
                chatHistoryMessage.setType(CommandType.CHAT_HISTORY);
                chatHistoryMessage.setMessage(tokens[1]);
                return chatHistoryMessage;
            case CHAT_FIND:
                SendMessage chatFindMessage = new SendMessage();
                chatFindMessage.setType(CommandType.CHAT_FIND);
                chatFindMessage.setMessage(tokens[1]);
                return chatFindMessage;
            case CHAT_CREATE:
                SendMessage chatCreateMessage = new SendMessage();
                chatCreateMessage.setType(CommandType.CHAT_CREATE);
                chatCreateMessage.setMessage(tokens[1]);
                return chatCreateMessage;
            case USER_PASS:
                SendMessage userPassMessage = new SendMessage();
                userPassMessage.setType(CommandType.USER_PASS);
                userPassMessage.setMessage(tokens[1]);
                return userPassMessage;
            case USER_INFO:
                LoginMessage userInfoMessage = new LoginMessage();
                userInfoMessage.setType(CommandType.USER_INFO);
                userInfoMessage.setArgType(LoginMessage.ArgType.valueOf(tokens[1]));
                userInfoMessage.setUserId(Long.parseLong(tokens[2]));
                return userInfoMessage;
            case USER_LOGIN:
                LoginMessage loginMessage = new LoginMessage();
                loginMessage.setArgType(LoginMessage.ArgType.valueOf(tokens[1]));
                loginMessage.setLogin(tokens[2]);
                loginMessage.setPass(tokens[3]);
                return loginMessage;
            case MSG_SEND:
                SendMessage sendMessage = new SendMessage();
                sendMessage.setChatId(Long.valueOf(tokens[1]));
                sendMessage.setMessage(tokens[2]);
                return sendMessage;
            default:
                throw new RuntimeException("Invalid type: " + type);
        }
    }

    @Override
    public byte[] encode(Message msg) {
        StringBuilder builder = new StringBuilder();
        CommandType type = msg.getType();
        builder.append(type).append(DELIMITER);
        switch (type) {
            case USER_HELP:
                break;
            case CHAT_LIST:
                break;
            case CHAT_CREATE:
                SendMessage chatCreateMessage = (SendMessage) msg;
                builder.append(chatCreateMessage.getMessage()).append(DELIMITER);
                break;
            case CHAT_HISTORY:
                SendMessage chatHistoryMessage = (SendMessage) msg;
                builder.append(chatHistoryMessage.getMessage()).append(DELIMITER);
                break;
            case CHAT_FIND:
                SendMessage chatFindMessage = (SendMessage) msg;
                builder.append(chatFindMessage.getMessage()).append(DELIMITER);
                break;
            case USER_PASS:
                SendMessage userPassMessage = (SendMessage) msg;
                builder.append(userPassMessage.getMessage()).append(DELIMITER);
                break;
            case USER_INFO:
                LoginMessage userInfoMessage = (LoginMessage) msg;
                builder.append(userInfoMessage.getArgType()).append(DELIMITER);
                builder.append(userInfoMessage.getUserId()).append(DELIMITER);
                break;
            case USER_LOGIN:
                LoginMessage loginMessage = (LoginMessage) msg;
                builder.append(loginMessage.getArgType()).append(DELIMITER);
                builder.append(loginMessage.getLogin()).append(DELIMITER);
                builder.append(loginMessage.getPass()).append(DELIMITER);
                break;
            case MSG_SEND:
                SendMessage sendMessage = (SendMessage) msg;
                builder.append(sendMessage.getChatId()).append(DELIMITER);
                builder.append(sendMessage.getMessage()).append(DELIMITER);
                break;
            default:
                throw new RuntimeException("Invalid type: " + type);
        }
        //log.info("encoded: {}", builder.toString());
        return builder.toString().getBytes();
    }


}
