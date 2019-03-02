package net.check321.chat.core.constant;

import net.check321.chat.core.serialize.SerializerMark;

public class CommonArgs {

    public static final String SERIALIZER_PREFIX = "serializer_";

    public static final String SERIALIZER_JSON = SERIALIZER_PREFIX + SerializerMark.JSON;

    public static final String SERIALIZER_PROTOCOL_BUFFER = SERIALIZER_PREFIX +SerializerMark.PROTOCO_BUF;
}
