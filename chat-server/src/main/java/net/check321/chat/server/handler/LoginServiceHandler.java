package net.check321.chat.server.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import net.check321.chat.core.entity.User;
import net.check321.chat.core.entity.packet.LoginReqPacket;
import net.check321.chat.core.entity.packet.LoginRespPacket;
import net.check321.chat.core.util.UserHolder;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@ChannelHandler.Sharable
public class LoginServiceHandler extends SimpleChannelInboundHandler<LoginReqPacket> {

    private static Map<String, String> MOCK_DATABASE = new ConcurrentHashMap<>();

    private final static int L_RANGE = 10000;

    private final static int R_RANGE = 99999;

    static {
        MOCK_DATABASE.put("Wu-Kong", "123456");
        MOCK_DATABASE.put("Majin-Buu", "123456");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginReqPacket req) throws Exception {

        LoginRespPacket resp = new LoginRespPacket();
        resp.setUserName(req.getUserName());

        if (!loginMock(req)) {
            resp.setCause("invalid username or password.");
            resp.setSuccess(false);
        } else {
            resp.setSuccess(true);
            int randomId = generateRandomId();
            resp.setUserId(randomId);
            UserHolder.bindToChannel(new User(randomId,req.getUserName()), ctx.channel());
            resp.setCause("login success.");
        }

        ctx.channel().writeAndFlush(resp);
    }

    private boolean loginMock(LoginReqPacket loginReqPacket) {
        String userName = loginReqPacket.getUserName();
        String pwd = loginReqPacket.getPwd();

        if (StringUtils.isEmpty(userName) || StringUtils.isEmpty(pwd)) {
            return false;
        }

        return pwd.equals(MOCK_DATABASE.get(userName));
    }

    private int generateRandomId() {
        return ThreadLocalRandom.current().nextInt(L_RANGE, R_RANGE);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        log.info("User[{}] has disconnected.", UserHolder.getUser(ctx.channel()).getUserName());
        UserHolder.unbindFromChannel(ctx.channel());
    }
}
