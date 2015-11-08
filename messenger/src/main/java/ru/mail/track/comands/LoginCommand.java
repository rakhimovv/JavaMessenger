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
public class LoginCommand implements Command {

    static Logger log = LoggerFactory.getLogger(LoginCommand.class);

    //private AuthorizationService authService;
    private UserStore userStore;
    private SessionManager sessionManager;
    private String answer;

    //public LoginCommand(AuthorizationService authService, SessionManager sessionManager) {
    public LoginCommand(UserStore userStore, SessionManager sessionManager) {
        this.answer = new String();
        //this.authService = authService;
        this.userStore = userStore;
        this.sessionManager = sessionManager;
    }


    @Override
    public void execute(Session session, Message msg) {

        if (session.getSessionUser() != null) {
            log.info("User {} already logged in.", session.getSessionUser());
            answer = "You have already logged in.";
        } else {
            LoginMessage loginMsg = (LoginMessage) msg;
            String name = loginMsg.getLogin();
            String password = loginMsg.getPass();
            if (loginMsg.getArgType() == loginMsg.LOGIN) {
                if (userStore.isUserExist(name)) {
                    User user = userStore.getUser(name, password);
                    if (user != null) {
                        session.setSessionUser(user);
                        sessionManager.registerUser(user.getId(), session.getId());
                        log.info("Success login: {}", user);
                    } else {
                        log.info("login: Wrong password.");
                        answer = "Wrong password.";
                    }
                } else {
                    log.info("login: The user with this name doesn't exist.");
                    answer = "The user with this name doesn't exist.";
                }
            } else if (loginMsg.getArgType() == loginMsg.CREAT_USER) {
                if (userStore.isUserExist(name) == false) {
                    User user = new User(name, password);
                    userStore.addUser(user);
                    log.info("Success creat_user: {}", user);
                    answer = "The new user successfully was created.";
                } else {
                    log.info("creatUser: The user with this name has already existed.");
                    answer = "The user with this name has already existed.";
                }
            } else {
                log.info("Wrong argType: {}", loginMsg.getArgType());
            }
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
        /*
        А эта часть у нас уже реализована
        1 проверим, есть ли у нас уже юзер сессии
        2 посмотрим на аргументы команды
        3 пойдем в authorizationService и попробуем получить юзера
         */
    }
}
