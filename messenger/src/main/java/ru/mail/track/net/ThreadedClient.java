package ru.mail.track.net;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.mail.track.commands.CommandType;
import ru.mail.track.message.*;
import ru.mail.track.session.Session;

import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;


/**
 * Клиентская часть
 */
public class ThreadedClient implements MessageListener {

    public static final int PORT = 19000;
    public static final String HOST = "localhost";
    static Logger log = LoggerFactory.getLogger(ThreadedClient.class);
    ConnectionHandler handler;
    Protocol protocol;

    public ThreadedClient(Protocol protocol) {
        this.protocol = protocol;
        try {
            Socket socket = new Socket(HOST, PORT);
            Session session = new Session();
            handler = new SocketConnectionHandler(protocol, session, socket);

            // Этот класс будет получать уведомления от socket handler
            handler.addListener(this);

            Thread socketHandler = new Thread(handler);
            socketHandler.start();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
            // exit, failed to open socket
        }
    }

    public static void main(String[] args) throws Exception {
        Protocol protocol = new StringProtocol();
        ThreadedClient client = new ThreadedClient(protocol);

        Scanner scanner = new Scanner(System.in);
        System.out.println("$");
        while (true) {
            String input = scanner.nextLine();
            if ("q".equals(input)) {
                return;
            }
            client.processInput(input);
        }
    }

    public void processInput(String line) throws IOException {
        String[] tokens = line.split(" ");
        //log.info("Tokens: {}", Arrays.toString(tokens));
        String cmdType = tokens[0];
        switch (cmdType) {
            case "login":
                LoginMessage loginMessage = new LoginMessage();
                loginMessage.setType(CommandType.USER_LOGIN);
                switch (tokens.length) {
                    case 3:
                        loginMessage.setArgType(LoginMessage.ArgType.LOGIN);
                        loginMessage.setLogin(tokens[1]);
                        loginMessage.setPass(tokens[2]);
                        handler.send(loginMessage);
                        break;
                    case 1:
                        loginMessage.setArgType(LoginMessage.ArgType.CREAT_USER);
                        System.out.println("Write your new login and password:");
                        Scanner scanner = new Scanner(System.in);
                        String[] args = scanner.nextLine().split(" ");
                        loginMessage.setLogin(args[0]);
                        loginMessage.setPass(args[1]);
                        handler.send(loginMessage);
                        break;
                    default:
                        System.out.println("Wrong amount of arguments. Try <help>");
                }
                break;
            case "send":
                SendMessage sendMessage = new SendMessage();
                sendMessage.setType(CommandType.MSG_SEND);
                sendMessage.setChatId(Long.valueOf(tokens[1]));
                sendMessage.setMessage(tokens[2]);
                handler.send(sendMessage);
                break;
            case "help":
                SendMessage helpMessage = new SendMessage();
                helpMessage.setType(CommandType.USER_HELP);
                handler.send(helpMessage);
                break;
            case "user_info":
                LoginMessage userInfoMessage = new LoginMessage();
                userInfoMessage.setType(CommandType.USER_INFO);
                switch (tokens.length) {
                    case 1:
                        userInfoMessage.setArgType(LoginMessage.ArgType.SELF_INFO);
                        userInfoMessage.setUserId(0L);
                        handler.send(userInfoMessage);
                        break;
                    case 2:
                        userInfoMessage.setArgType(LoginMessage.ArgType.ID_INFO);
                        userInfoMessage.setUserId(Long.parseLong(tokens[1]));
                        handler.send(userInfoMessage);
                        break;
                    default:
                        System.out.println("Wrong amount of arguments. Try <help>");
                }
                break;
            case "user_pass":
                SendMessage userPassMessage = new SendMessage();
                userPassMessage.setType(CommandType.USER_PASS);
                switch (tokens.length) {
                    case 3:
                        userPassMessage.setMessage(tokens[1] + ">" + tokens[2]);
                        handler.send(userPassMessage);
                        break;
                    default:
                        System.out.println("Wrong amount of arguments. Try <help>");
                }
                break;
            case "chat_list":
                SendMessage chatListMessage = new SendMessage();
                chatListMessage.setType(CommandType.CHAT_LIST);
                handler.send(chatListMessage);
                break;
            case "chat_create":
                SendMessage chatCreateMessage = new SendMessage();
                chatCreateMessage.setType(CommandType.CHAT_CREATE);
                switch (tokens.length) {
                    case 2:
                        chatCreateMessage.setMessage(tokens[1]);
                        handler.send(chatCreateMessage);
                        break;
                    default:
                        System.out.println("Wrong amount of arguments. Try <help>");
                }
                break;
            case "chat_history":
                SendMessage chatHistoryMessage = new SendMessage();
                chatHistoryMessage.setType(CommandType.CHAT_HISTORY);
                switch (tokens.length) {
                    case 2:
                        chatHistoryMessage.setMessage(tokens[1]);
                        handler.send(chatHistoryMessage);
                        break;
                    default:
                        System.out.println("Wrong amount of arguments. Try <help>");
                }
                break;
            case "chat_find":
                SendMessage chatFindMessage = new SendMessage();
                chatFindMessage.setType(CommandType.CHAT_FIND);
                switch (tokens.length) {
                    case 3:
                        chatFindMessage.setMessage(tokens[1] + ">" + tokens[2]);
                        handler.send(chatFindMessage);
                        break;
                    default:
                        System.out.println("Wrong amount of arguments. Try <help>");
                }
            default:
                System.out.println("Invalid input: " + line);
        }


    }

    /**
     * Получено сообщение из handler, как обрабатывать
     */
    @Override
    public void onMessage(Session session, Message msg) {
        System.out.printf("%s", ((SendMessage) msg).getMessage());
    }

}
