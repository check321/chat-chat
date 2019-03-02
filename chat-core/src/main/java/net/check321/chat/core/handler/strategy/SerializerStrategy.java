package net.check321.chat.core.handler.strategy;

import net.check321.chat.core.config.ChatApplicationContext;
import net.check321.chat.core.constant.CommonArgs;
import net.check321.chat.core.serialize.Serializer;
import org.springframework.stereotype.Component;

@Component
public class SerializerStrategy extends ChatApplicationContext implements Strategy<Byte, Serializer> {

    @Override
    public Serializer getStrategy(Byte mark) {
        String beanName = CommonArgs.SERIALIZER_PREFIX + mark;
        return getApplicationContext().getBean(beanName, Serializer.class);
    }


}
