package net.check321.chat.client.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import net.check321.chat.core.entity.packet.MessageRespPacket;

@ChannelHandler.Sharable
@Slf4j
public class MessageRespHandler extends SimpleChannelInboundHandler<MessageRespPacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageRespPacket msg) throws Exception {
        log.info("{}[{}] : {}",msg.getOriginName(),msg.getOriginId(),msg.getContent());
    }
}
