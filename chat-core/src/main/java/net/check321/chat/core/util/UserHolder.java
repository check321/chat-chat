package net.check321.chat.core.util;

import io.netty.channel.Channel;
import net.check321.chat.core.entity.User;
import net.check321.chat.core.entity.attribute.Attributes;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UserHolder {

    private static final Map<Integer, Channel> USER_HOLDER = new ConcurrentHashMap<>();

    public static void bindToChannel(User user, Channel channel) {
        USER_HOLDER.put(user.getUserId(), channel);
        channel.attr(Attributes.USER).set(user);
    }

    public static  void unbindFromChannel(Channel channel) {
        if(hasLogin(channel)){
            USER_HOLDER.remove(getUser(channel).getUserId());
            channel.attr(Attributes.USER).set(null);
        }
    }

    public static boolean hasLogin(Channel channel) {
        return channel.hasAttr(Attributes.USER);
    }

    public static User getUser(Channel channel) {
        return channel.attr(Attributes.USER).get();
    }

    public static Channel getChannel(Integer userId){
        return USER_HOLDER.get(userId);
    }

}
