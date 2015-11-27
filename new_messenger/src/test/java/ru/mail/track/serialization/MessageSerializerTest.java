package ru.mail.track.serialization;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import ru.mail.track.commands.base.CommandType;
import ru.mail.track.message.LoginMessage;
import ru.mail.track.message.Message;
import ru.mail.track.message.SendMessage;

import static org.junit.Assert.*;

/**
 *
 */
public class MessageSerializerTest {

    private final Map<CommandType, Message> messages = new HashMap<>();

    @Before
    public void setup() {
        LoginMessage login = new LoginMessage();
        login.setType(CommandType.USER_LOGIN);
        login.setArgType(LoginMessage.ArgType.LOGIN);
        login.setSender(123L);
        login.setLogin("Jack");
        login.setPass("qwerty");
        messages.put(CommandType.USER_LOGIN, login);

        SendMessage send = new SendMessage();
        send.setType(CommandType.CHAT_SEND);
        send.setSender(123L);
        send.setChatId(1L);
        send.setMessage("Hello world!");
        messages.put(CommandType.CHAT_SEND, send);

    }

    @Test
    public void encodeLogin() throws Exception {
        Message origin = messages.get(CommandType.USER_LOGIN);

        System.out.println(origin);

        Protocol protocol = new SerializationProtocol();
        byte[] data = protocol.encode(origin);
        Message copy = protocol.decode(data);

        System.out.println(copy);

        assertTrue(!copy.equals(origin));
    }


    @Test
    //@Ignore
    public void encodeSend() throws Exception {
        SendMessage origin = (SendMessage) messages.get(CommandType.CHAT_SEND);

        System.out.println(origin);

        Protocol protocol = new SerializationProtocol();
        byte[] data = protocol.encode(origin);
        SendMessage copy = (SendMessage) protocol.decode(data);

        System.out.println(copy);

        assertTrue(!copy.equals(origin));
    }
}
