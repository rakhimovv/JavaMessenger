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
public class UserInfoCommand implements Command {

    static Logger log = LoggerFactory.getLogger(UserInfoCommand.class);

    private UserStore userStore;
    private SessionManager sessionManager;
    private String answer;

    public UserInfoCommand(UserStore userStore, SessionManager sessionManager) {
        this.answer = new String();
        this.userStore = userStore;
        this.sessionManager = sessionManager;
    }


    @Override
    public void execute(Session session, Message msg) {
        LoginMessage userInfoMsg = (LoginMessage) msg;
        if(userInfoMsg.getArgType() == userInfoMsg.SELF_INFO) {
            if (session.getSessionUser() != null) {
                answer += "login: " + session.getSessionUser().getName() + "\n";
                answer += "password: " + session.getSessionUser().getPass() + "\n";
                log.info("Success self_info: {}", session.getSessionUser());
            } else {
                answer = "You are not logged in.";
                log.info("User isn't logged in.");
            }
        } else if (userInfoMsg.getArgType() == userInfoMsg.ID_INFO) {
            User user = userStore.getUserById(userInfoMsg.getUserId());
            if(user != null) {
                answer += "login: " + user.getName() + "\n";
                answer += "password: " + user.getPass() + "\n";
                log.info("Success id_info: {}", userStore.getUserById(userInfoMsg.getUserId()));
            } else {
                log.info("Wrong userId: {}", userInfoMsg.getUserId());
                answer = "Wrong user id.";
            }
        } else {
            log.info("Wrong argType: {}", userInfoMsg.getArgType());
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
