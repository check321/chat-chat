package net.check321.chat.server.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import net.check321.chat.core.entity.packet.EchoPacket;

@Slf4j
@ChannelHandler.Sharable
public class EchoServiceHandler extends SimpleChannelInboundHandler<EchoPacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, EchoPacket echoPacket) throws Exception {
        log.info("Received message from server: [{}]",echoPacket.getContent());
        echoPacket.setContent("Got it.");
        ctx.channel().writeAndFlush(echoPacket);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx,Throwable cause){
        log.info("echo server error. {}",cause);
    }
}
