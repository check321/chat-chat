package net.check321.chat.core.serialize;

public interface Serializer {

    byte serializerMark();

    byte[] serialize(Object object);

    <T> T deserialize(byte[] bytes,Class<T> clazz);
}
