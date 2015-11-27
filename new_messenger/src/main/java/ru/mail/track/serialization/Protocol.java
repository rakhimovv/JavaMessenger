package ru.mail.track.serialization;


import ru.mail.track.message.Message;

/**
 *
 */
public interface Protocol {

    byte[] encode(Message msg) throws ProtocolException;

    Message decode(byte[] data) throws ProtocolException;
}
