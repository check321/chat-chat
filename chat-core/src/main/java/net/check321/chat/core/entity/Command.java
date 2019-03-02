package net.check321.chat.core.entity;

public interface Command {

    byte LOGIN_REQ = 1;

    byte LOGIN_RESP = 2;

    byte MESSAGE_REQ = 7;

    byte MESSAGE_RESP = 8;

    byte ECHO = 88;
}
