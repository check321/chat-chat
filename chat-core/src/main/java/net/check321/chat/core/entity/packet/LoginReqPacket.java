package net.check321.chat.core.entity.packet;

import lombok.Data;
import net.check321.chat.core.entity.Command;

@Data
public class LoginReqPacket extends Packet {

    private String userName;

    private String pwd;

    @Override
    public byte getCommand() {
        return Command.LOGIN_REQ;
    }
}
