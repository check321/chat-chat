package net.check321.chat.server.bootstrap;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;
import net.check321.chat.core.entity.packet.PacketDecoder;
import net.check321.chat.core.entity.packet.PacketEncoder;
import net.check321.chat.server.handler.EchoServiceHandler;
import net.check321.chat.server.handler.LoginServiceHandler;
import net.check321.chat.server.handler.MessageServiceHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Slf4j
@Component
public class ChatBootstrap {


    @Value("${chat.server.port}")
    private int serverPort;

    @PostConstruct
    public void start() {
        NioEventLoopGroup group = new NioEventLoopGroup();

        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(group)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 1024)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel channel) throws Exception {
                        channel.pipeline().addLast(new PacketDecoder());
                        channel.pipeline().addLast(new EchoServiceHandler());
                        channel.pipeline().addLast(new LoginServiceHandler());
                        channel.pipeline().addLast(new MessageServiceHandler());
                        channel.pipeline().addLast(new PacketEncoder());
                    }
                });

        bootstrap.bind(serverPort).addListener(future -> {
            if (future.isSuccess()) {
                log.info("Chat-Chat server bind port[{}].", serverPort);
            } else {
                log.error("Chat-Chat server bind port failed.");
            }
        });

    }
}