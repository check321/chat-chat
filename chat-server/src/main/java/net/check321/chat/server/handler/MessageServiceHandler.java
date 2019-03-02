package net.check321.chat.server.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import net.check321.chat.core.entity.User;
import net.check321.chat.core.entity.packet.MessageReqPacket;
import net.check321.chat.core.entity.packet.MessageRespPacket;
import net.check321.chat.core.util.UserHolder;

@Slf4j
@ChannelHandler.Sharable
public class MessageServiceHandler extends SimpleChannelInboundHandler<MessageReqPacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageReqPacket msg) throws Exception {
        // current user.
        User originUser = UserHolder.getUser(ctx.channel());

        // build target packet.
        MessageRespPacket resp = MessageRespPacket.builder()
                .originId(originUser.getUserId())
                .originName(originUser.getUserName())
                .content(msg.getContent())
                .build();

        // send to target.
        Channel targetChannel = UserHolder.getChannel(msg.getTargetID());
        if(null == targetChannel || !UserHolder.hasLogin(targetChannel)){
            log.info("target user[{}] not online.",msg.getTargetID());
            return;
        }

        targetChannel.writeAndFlush(resp);
    }

}
