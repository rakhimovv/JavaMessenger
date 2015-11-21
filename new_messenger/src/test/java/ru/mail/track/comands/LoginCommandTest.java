package ru.mail.track.comands;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import ru.mail.track.AuthorizationService;
import ru.mail.track.commands.LoginCommand;
import ru.mail.track.message.LoginMessage;
import ru.mail.track.message.User;
import ru.mail.track.message.UserStore;
import ru.mail.track.net.SessionManager;
import ru.mail.track.session.Session;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 *
 */
public class LoginCommandTest {

    UserStore userStore;
    SessionManager sessionManager;
    User defaultUser = new User("Jack", "qwerty");

    @Before
    public void setup() {
        sessionManager = new SessionManager();
        userStore =  Mockito.mock(UserStore.class);
        when(userStore.getUser("Jack", "qwerty")).thenReturn(defaultUser);

    }

    @Test
    public void successLogin() throws Exception {

        AuthorizationService authService = new AuthorizationService(userStore);
        LoginCommand loginCommand = new LoginCommand(authService, sessionManager);
        LoginMessage login = new LoginMessage();
        login.setSender(123L);
        login.setLogin("Jack");
        login.setPass("qwerty");

        Session session = new Session();
        loginCommand.execute(session, login);
        assertEquals(session.getSessionUser(), defaultUser);
    }

    @Test
    public void badLogin() throws Exception {

        AuthorizationService authService = new AuthorizationService(userStore);
        LoginCommand loginCommand = new LoginCommand(authService, sessionManager);
        LoginMessage login = new LoginMessage();
        login.setSender(123L);
        login.setLogin("Jack");
        login.setPass("*****");

        Session session = new Session();
        loginCommand.execute(session, login);
        assertNull(session.getSessionUser());
    }


}
