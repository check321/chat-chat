package net.check321.chat.client.bootstrap;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;
import net.check321.chat.client.config.ClientConfiguration;
import net.check321.chat.client.handler.EchoRespHandler;
import net.check321.chat.client.handler.LoginRespHandler;
import net.check321.chat.client.handler.MessageRespHandler;
import net.check321.chat.core.entity.packet.*;
import net.check321.chat.core.util.UserHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class ClientBootstrap {

    private final ClientConfiguration clientConfig;

    private final int RETRY;


    @Autowired
    public ClientBootstrap(ClientConfiguration clientConfig) {
        this.clientConfig = clientConfig;
        this.RETRY = clientConfig.getMaxRetry();
    }


    @PostConstruct
    public void init() {
        NioEventLoopGroup group = new NioEventLoopGroup();

        Bootstrap boot = new Bootstrap();
        boot.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new PacketDecoder());
                        ch.pipeline().addLast(new EchoRespHandler());
                        ch.pipeline().addLast(new LoginRespHandler());
                        ch.pipeline().addLast(new MessageRespHandler());
                        ch.pipeline().addLast(new PacketEncoder());
                    }
                });

        try {
            connect(boot, clientConfig.getMaxRetry());
        } catch (Exception e) {
            log.error("Client connecting error: ", e);
        }

    }


    /**
     * Connect by automatic.
     *
     * @param boot
     * @param retry
     */
    private void connect(Bootstrap boot, int retry) throws InterruptedException {
        boot.connect(clientConfig.getServerHost(), clientConfig.getServerPort())
                .addListener(future -> {
                    if (future.isSuccess()) {
                        log.info("Client has connected to server[{}:{}].", clientConfig.getServerHost(), clientConfig.getServerPort());
                        Channel channel = ((ChannelFuture) future).channel();
                        this.testCall(channel);
                        this.consoleRunning(channel);
                    } else if (RETRY == 0) {
                        log.info("Client failed to server.");
                    } else {
                        int count = (clientConfig.getMaxRetry() - retry) + 1;
                        int delay = 1 << count;
                        log.info("Client try to reconnected. [{}] times", count);

                        // reconnect.
                        boot.config().group().schedule(() -> {
                            try {
                                connect(boot, retry - 1);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }, delay, TimeUnit.SECONDS);
                    }
                });
    }

    private void consoleRunning(Channel channel) {
        Scanner sc = new Scanner(System.in);
        new Thread(() -> {
            while (!Thread.interrupted()) {
                if (!UserHolder.hasLogin(channel)) {
                    log.info("[user_name] [password] to login.");
                    this.loginScanner(sc, channel);
                    // wait for a second.
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        log.error("Thread error: {}", e);
                    }
                } else {
                    chatScanner(sc,channel);
                }
            }
        }).start();

    }

    private void loginScanner(Scanner sc, Channel channel) {
        String loginCommand = sc.nextLine();
        String[] splitCmd = loginCommand.split(" ");

        if (splitCmd.length < 2) {
            log.info("[user_name] [password] to login.");
        } else {
            LoginReqPacket loginReq = new LoginReqPacket();
            loginReq.setUserName(splitCmd[0]);
            loginReq.setPwd(splitCmd[1]);
            channel.writeAndFlush(loginReq);
        }
    }

    private void chatScanner(Scanner sc, Channel channel) {
        String chatCommand = sc.nextLine();
        String[] splitCmd = chatCommand.split(" ");

        if (splitCmd.length < 2) {
            log.info("[user_id] [message] to chat.");
        } else {
            MessageReqPacket req = new MessageReqPacket();
            req.setTargetID(Integer.valueOf(splitCmd[0]));
            req.setContent(splitCmd[1]);
            channel.writeAndFlush(req);
        }

    }

    private void testCall(Channel channel) {
        EchoPacket echoPacket = new EchoPacket();
        echoPacket.setContent("Hola!");

        channel.writeAndFlush(echoPacket);
    }

}
