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
        return userStore.getUser(name, password);
    }

    public User creatUser(String name, String password) {
        if (!userStore.isUserExist(name)) {
            return userStore.addUser(new User(name, password));
        }
        return null;
    }

    public UserStore getUserStore() {
        return userStore;
    }
}
