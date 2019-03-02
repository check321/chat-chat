package net.check321.chat.client.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import net.check321.chat.core.entity.User;
import net.check321.chat.core.entity.packet.LoginRespPacket;
import net.check321.chat.core.util.UserHolder;

@Slf4j
@ChannelHandler.Sharable
public class LoginRespHandler extends SimpleChannelInboundHandler<LoginRespPacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginRespPacket loginRespPacket) throws Exception {

        String name = loginRespPacket.getUserName();

        if(loginRespPacket.isSuccess()){
            UserHolder.bindToChannel(new User(name), ctx.channel());
            log.info("{}[{}] login success.",name,loginRespPacket.getUserId());
        }else{
            log.info("[{}] login denied.cause: {}",name,loginRespPacket.getCause());
        }
    }


    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        log.info("User[{}] has disconnected.", UserHolder.getUser(ctx.channel()).getUserName());
        UserHolder.unbindFromChannel(ctx.channel());
    }
}
