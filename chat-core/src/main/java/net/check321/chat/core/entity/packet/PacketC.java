package net.check321.chat.core.entity.packet;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import lombok.extern.slf4j.Slf4j;
import net.check321.chat.core.handler.strategy.SerializerStrategy;
import net.check321.chat.core.serialize.Serializer;
import net.check321.chat.core.entity.Command;
import net.check321.chat.core.serialize.JSONSerializer;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

/**
 * @author fyang
 * @title Protocol : MagicNumber(4bytes) + ProtocolVersion(1byte) + SerializerMark(1byte) + Command(1byte) + DataLength(4bytes) + Data(N-bytes)
 * @description
 * @date 2019/2/15 17:25
 */
//@Component
@Slf4j
public class PacketC {

    private static final int MAGIC_NUM = 0x37213721;

    private static final Serializer DEFAULT_SERIALIZER = new JSONSerializer();

    private final SerializerStrategy serializerStrategy;

    private final static Map<Byte, Class<? extends Packet>> PACKET_HOLDER = new HashMap<>();

    @Autowired
    public PacketC(SerializerStrategy serializerStrategy) {
        this.serializerStrategy = serializerStrategy;
    }

    //    @PostConstruct
    static {
        PACKET_HOLDER.put(Command.MESSAGE_REQ, MessageReqPacket.class);
        PACKET_HOLDER.put(Command.MESSAGE_RESP, MessageRespPacket.class);
        PACKET_HOLDER.put(Command.LOGIN_REQ, LoginReqPacket.class);
        PACKET_HOLDER.put(Command.LOGIN_RESP, LoginRespPacket.class);
        PACKET_HOLDER.put(Command.ECHO, EchoPacket.class);

        log.info("PacketC init.");
    }


    public static PacketC INSTANCE() {
        return PacketInstance.instance;
    }

    private static class PacketInstance {
        static PacketC instance = new PacketC(new SerializerStrategy());
    }

    public ByteBuf encode(ByteBufAllocator allocator, Packet packet) {

        // allocate direct memory.
        ByteBuf byteBuf = allocator.buffer();
        byte[] bytes = DEFAULT_SERIALIZER.serialize(packet);

        byteBuf.writeInt(MAGIC_NUM);
        byteBuf.writeByte(packet.getVersion());
        byteBuf.writeByte(DEFAULT_SERIALIZER.serializerMark());
        byteBuf.writeByte(packet.getCommand());
        byteBuf.writeInt(bytes.length);
        byteBuf.writeBytes(bytes);

        return byteBuf;
    }

    public ByteBuf encode(ByteBuf byteBuf, Packet packet) {
        byte[] bytes = DEFAULT_SERIALIZER.serialize(packet);

        byteBuf.writeInt(MAGIC_NUM);
        byteBuf.writeByte(packet.getVersion());
        byteBuf.writeByte(DEFAULT_SERIALIZER.serializerMark());
        byteBuf.writeByte(packet.getCommand());
        byteBuf.writeInt(bytes.length);
        byteBuf.writeBytes(bytes);

        return byteBuf;
    }

    public Packet decode(ByteBuf byteBuf) {

        // skip magic-number.
        byteBuf.skipBytes(4);

        // skip protocol version.
        byteBuf.skipBytes(1);

        byte serializerMark = byteBuf.readByte();

        byte command = byteBuf.readByte();

        int dataLength = byteBuf.readInt();

        byte[] bytes = new byte[dataLength];
        byteBuf.readBytes(bytes);

        Class<? extends Packet> packetType = getPacketType(command);
        Serializer serializer = serializerStrategy.getStrategy(serializerMark);

        if (null == packetType || null == serializer) {
            return null;
        }

        return serializer.deserialize(bytes, packetType);
    }

    private Class<? extends Packet> getPacketType(byte command) {
        return PACKET_HOLDER.get(command);
    }
}
