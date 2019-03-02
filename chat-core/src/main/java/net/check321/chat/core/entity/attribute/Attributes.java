package net.check321.chat.core.entity.attribute;

import io.netty.util.AttributeKey;
import net.check321.chat.core.entity.User;

public interface Attributes {

    AttributeKey<User> USER = AttributeKey.newInstance("user");
}
