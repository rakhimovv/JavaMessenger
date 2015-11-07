package ru.mail.track;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.mail.track.message.User;
import ru.mail.track.message.UserStore;

public class AuthorizationService {

    static Logger log = LoggerFactory.getLogger(AuthorizationService.class);

    private UserStore userStore;

    public AuthorizationService(UserStore userStore) {
        this.userStore = userStore;
    }

    public User login(String name, String password) {
        if (userStore.isUserExist(name)) {
            User user = userStore.getUser(name, password);
            if (user != null) {
                return user;
            } else {
                log.info("login: Wrong password.");
            }
        }
        log.info("login: The user with this name doesn't exist.");
        return null;
    }

    public User creatUser(String name, String password) {
        if (userStore.isUserExist(name) == false) {
            User user = new User(name, password);
            userStore.addUser(user);
            return user;
        }
        log.info("creatUser: The user with this name has already existed.");
        return null;
    }
}
