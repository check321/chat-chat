package net.check321.chat.core.entity.packet;

import lombok.Data;
import net.check321.chat.core.entity.Command;

@Data
public class EchoPacket extends Packet {

    private String content;

    @Override
    public byte getCommand() {
        return Command.ECHO;
    }
}
