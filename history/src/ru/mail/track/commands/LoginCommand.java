package ru.mail.track.commands;

import ru.mail.track.authorization.AuthorizationService;
import ru.mail.track.authorization.UserAlreadyExistsException;
import ru.mail.track.session.Session;

import java.io.IOException;

/**
 * Выполняем авторизацию по этой команде
 */
public class LoginCommand implements Command {

    private AuthorizationService service;

    public LoginCommand(AuthorizationService service) {
        this.service = service;
    }

    @Override
    public void execute(Session session, String[] args) throws UserAlreadyExistsException, WrongArgumentsException, IOException{
        /*
        А эта часть у нас уже реализована
        1 проверим, есть ли у нас уже юзер сессии
        2 посмотрим на аргументы команды
        3 пойдем в authorizationService и попробуем получить юзера
         */

        if (args.length == 1) {
            service.creatUser();
        } else if (args.length == 3) {
            if (session.getSessionUser() == null) {
                session.setSessionUser(service.login(args[1], args[2]));
                if (session.getSessionUser() != null)
                    System.out.println("Hello, " + session.getSessionUser().getNickname() + "!");
            } else {
                System.out.println(session.getSessionUser().getNickname() + ", you've already logined.");
            }
        } else {
            throw new WrongArgumentsException();
        }
    }
}
