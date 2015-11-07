package ru.mail.track.comands;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.mail.track.AuthorizationService;
import ru.mail.track.message.LoginMessage;
import ru.mail.track.message.Message;
import ru.mail.track.message.User;
import ru.mail.track.message.UserStore;
import ru.mail.track.net.SessionManager;
import ru.mail.track.session.Session;

/**
 * Выполняем авторизацию по этой команде
 */
public class LoginCommand implements Command {

    static Logger log = LoggerFactory.getLogger(LoginCommand.class);

    private AuthorizationService authService;
    private SessionManager sessionManager;

    public LoginCommand(AuthorizationService authService, SessionManager sessionManager) {
        this.authService = authService;
        this.sessionManager = sessionManager;
    }


    @Override
    public void execute(Session session, Message msg) {

        if (session.getSessionUser() != null) {
            log.info("User {} already logged in.", session.getSessionUser());
            return;
        } else {
            LoginMessage loginMsg = (LoginMessage) msg;
            if (loginMsg.getArgType() == loginMsg.LOGIN) {
                User user = authService.login(loginMsg.getLogin(), loginMsg.getPass());
                if (user != null) {
                    session.setSessionUser(user);
                    sessionManager.registerUser(user.getId(), session.getId());
                    log.info("Success login: {}", user);
                }
            } else if (loginMsg.getArgType() == loginMsg.CREAT_USER) {
                User user = authService.creatUser(loginMsg.getLogin(), loginMsg.getPass());
                if (user != null) {
                    log.info("Success creatUser: {}", user);
                }
            } else {
                log.info("Wrong argType: {}", loginMsg.getArgType());
            }
        }
        /*
        А эта часть у нас уже реализована
        1 проверим, есть ли у нас уже юзер сессии
        2 посмотрим на аргументы команды
        3 пойдем в authorizationService и попробуем получить юзера
         */
    }
}
