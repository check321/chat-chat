package net.check321.chat.core.entity.packet;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import net.check321.chat.core.entity.Command;

@Data
@Builder
@AllArgsConstructor
public class MessageRespPacket extends Packet{

    private int originId;

    private String originName;

    private String content;

    @Override
    public byte getCommand() {
        return Command.MESSAGE_RESP;
    }
}
