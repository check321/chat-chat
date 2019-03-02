package net.check321.chat.core.serialize;

import com.alibaba.fastjson.JSON;
import net.check321.chat.core.constant.CommonArgs;
import org.springframework.stereotype.Component;

@Component(CommonArgs.SERIALIZER_JSON)
public class JSONSerializer implements Serializer{
    @Override
    public byte serializerMark() {
        return SerializerMark.JSON;
    }

    @Override
    public byte[] serialize(Object object) {
        return JSON.toJSONBytes(object);
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) {
        return JSON.parseObject(bytes,clazz);
    }
}
