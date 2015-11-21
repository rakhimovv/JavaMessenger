package ru.mail.track.net;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.mail.track.commands.CommandType;
import ru.mail.track.message.SendMessage;

import static org.junit.Assert.*;

/**
 *
 */

public class StringProtocolTest {

    SendMessage msg;
    StringProtocol protocol;

    @Before
    public void setUp() throws Exception {
        msg = new SendMessage();
        protocol = new StringProtocol();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testDecode() throws Exception {
        System.out.println("testDecode");
        msg.setType(CommandType.CHAT_HISTORY);
        msg.setMessage("HELLO/HI/GOOD_BUY");

        String encodeMsg = "CHAT_HISTORY;HELLO/HI/GOOD_BUY;";
        String str = new String(protocol.encode(msg));

        assertEquals(encodeMsg, str);
    }

    @Test
    public void testEncode() throws Exception {
        System.out.println("testEncode");
        msg.setType(CommandType.CHAT_HISTORY);
        msg.setMessage("HELLO/HI/GOOD_BUY");

        String encodeMsg = "CHAT_HISTORY;HELLO/HI/GOOD_BUY;";
        SendMessage sendMessage = (SendMessage) protocol.decode(encodeMsg.getBytes());

        assertEquals(msg, sendMessage);
    }
}