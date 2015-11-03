package ru.mail.track.commands;

import ru.mail.track.authorization.UserStore;
import ru.mail.track.session.Session;

/**
 * Выполняем авторизацию по этой команде
 */
public class UserCommand implements Command {

    private UserStore userStore;

    public UserCommand(UserStore userStore) {
        this.userStore = userStore;
    }

    @Override
    public void execute(Session session, String[] args) throws WrongArgumentsException {
        //System.out.println("Executing login");
        /*
        А эта часть у нас уже реализована
        1 проверим, есть ли у нас уже юзер сессии
        2 посмотрим на аргументы команды
        3 пойдем в authorizationService и попробуем получить юзера
         */

        if (args.length == 2) {
            if (session.getSessionUser() != null) {
                userStore.addUserNickname(session.getSessionUser(), args[1]);
            }
        } else {
            throw new WrongArgumentsException();
        }
    }
}
