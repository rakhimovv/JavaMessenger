package ru.mail.track.net;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.mail.track.comands.CommandType;
import ru.mail.track.message.HelpMessage;
import ru.mail.track.message.LoginMessage;
import ru.mail.track.message.Message;
import ru.mail.track.message.SendMessage;
import ru.mail.track.session.Session;

import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;
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
        log.info("Tokens: {}", Arrays.toString(tokens));
        String cmdType = tokens[0];
        switch (cmdType) {
            case "login":
                LoginMessage loginMessage = new LoginMessage();
                loginMessage.setType(CommandType.USER_LOGIN);
                switch (tokens.length) {
                    case 3:
                        loginMessage.setArgType(loginMessage.LOGIN);
                        loginMessage.setLogin(tokens[1]);
                        loginMessage.setPass(tokens[2]);
                        handler.send(loginMessage);
                        break;
                    case 1:
                        loginMessage.setArgType(loginMessage.CREAT_USER);
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
                HelpMessage helpMessage = new HelpMessage();
                handler.send(helpMessage);
                break;
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