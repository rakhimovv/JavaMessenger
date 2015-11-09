package ru.mail.track.comands;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.mail.track.AuthorizationService;
import ru.mail.track.message.*;
import ru.mail.track.net.SessionManager;
import ru.mail.track.session.Session;

import java.io.IOException;

/**
 * Выполняем авторизацию по этой команде
 */
public class UserPassCommand implements Command {

    static Logger log = LoggerFactory.getLogger(UserInfoCommand.class);

    private UserStore userStore;
    private SessionManager sessionManager;
    private String answer;


    @Override
    public void execute(Session session, Message msg) {
        LoginMessage userPassMsg = (LoginMessage) msg;
        User user;
        if ((user = session.getSessionUser()) != null) {
            user.setPass(userPassMsg.getPass());
            answer = "The password changed.";
            log.info("Success set_pass: {}", user);
        } else {
            answer = "You are not logged in.";
            log.info("User isn't logged in.");
        }

        try {
            SendMessage sendMessage = new SendMessage();
            sendMessage.setType(CommandType.MSG_SEND);
            sendMessage.setChatId(0L);
            sendMessage.setMessage(answer + "\n");
            session.getConnectionHandler().send(sendMessage);
            answer = "";
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
