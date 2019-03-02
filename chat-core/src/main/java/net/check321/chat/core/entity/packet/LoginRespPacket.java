package net.check321.chat.core.entity.packet;

import lombok.Data;
import net.check321.chat.core.entity.Command;

@Data
public class LoginRespPacket extends Packet{

    private String cause;

    private boolean success;

    private int userId;

    private String userName;

    @Override
    public byte getCommand() {
        return Command.LOGIN_RESP;
    }
}
