package ru.mail.track.commands;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.mail.track.message.*;
import ru.mail.track.net.SessionManager;
import ru.mail.track.session.Session;

/**
 * Выполняем авторизацию по этой команде
 */
public class LoginCommand implements Command {

    static Logger log = LoggerFactory.getLogger(LoginCommand.class);

    //private AuthorizationService authService;
    private UserStore userStore;
    private SessionManager sessionManager;
    private String answer;
    BaseCommandResult commandResult;

    //public LoginCommand(AuthorizationService authService, SessionManager sessionManager) {
    public LoginCommand(UserStore userStore, SessionManager sessionManager) {
        //this.authService = authService;
        this.userStore = userStore;
        this.sessionManager = sessionManager;
        commandResult = new BaseCommandResult();
        commandResult.setStatus(CommandResult.Status.OK);
    }


    @Override
    public BaseCommandResult execute(Session session, Message msg) {

        if (session.getSessionUser() != null) {
            log.info("User {} already logged in.", session.getSessionUser());
            commandResult.setResponse("You have already logged in.");
        } else {
            // TODO: вынети логику в AuthorizationService
            LoginMessage loginMsg = (LoginMessage) msg;
            String name = loginMsg.getLogin();
            String password = loginMsg.getPass();
            switch (loginMsg.getArgType()) {
                case LOGIN:
                    if (userStore.isUserExist(name)) {
                        User user = userStore.getUser(name, password);
                        if (user != null) {
                            session.setSessionUser(user);
                            sessionManager.registerUser(user.getId(), session.getId());
                            log.info("Success login: {}", user);
                            UserInfoCommand userInfoCommand = new UserInfoCommand(userStore);
                            LoginMessage userInfoMessage = new LoginMessage();
                            userInfoMessage.setArgType(LoginMessage.ArgType.SELF_INFO);
                            return userInfoCommand.execute(session, userInfoMessage);
                        } else {
                            log.info("login: Wrong password.");
                            commandResult.setResponse("Wrong password.");
                        }
                    } else {
                        log.info("login: The user with this name doesn't exist.");
                        commandResult.setResponse("The user with this name doesn't exist.");
                    }
                    break;
                case CREAT_USER:
                    if (!userStore.isUserExist(name)) {
                        User user = new User(name, password);
                        userStore.addUser(user);
                        log.info("Success creat_user: {}", user);
                        commandResult.setResponse("The new user successfully was created.");
                    } else {
                        log.info("creatUser: The user with this name has already existed.");
                        commandResult.setResponse("The user with this name has already existed.");
                    }
                    break;
                default:
                    log.info("Wrong argType: {}", loginMsg.getArgType());
            }
        }

        return commandResult;
    }
}
