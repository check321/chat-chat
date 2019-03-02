package net.check321.chat.core.entity.packet;

import lombok.Data;

@Data
public abstract class Packet {

    private byte version = 1;

    public abstract byte getCommand();


}
