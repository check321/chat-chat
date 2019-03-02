package net.check321.chat.client.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import net.check321.chat.core.entity.packet.EchoPacket;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@ChannelHandler.Sharable
public class EchoRespHandler extends SimpleChannelInboundHandler<EchoPacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, EchoPacket echoPacket) throws Exception {
        String content = echoPacket.getContent();
        log.info("Received message from server: [{}]",content);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        log.error("Echo-Client error: {}",cause);
        ctx.close();
    }
}
