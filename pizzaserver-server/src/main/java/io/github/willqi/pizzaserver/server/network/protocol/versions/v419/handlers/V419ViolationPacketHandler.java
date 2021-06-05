package io.github.willqi.pizzaserver.server.network.protocol.versions.v419.handlers;

import com.nukkitx.network.VarInts;
import io.github.willqi.pizzaserver.server.network.protocol.packets.ViolationPacket;
import io.github.willqi.pizzaserver.server.network.protocol.versions.ProtocolPacketHandler;
import io.github.willqi.pizzaserver.server.network.utils.ByteBufUtility;
import io.netty.buffer.ByteBuf;

public class V419ViolationPacketHandler extends ProtocolPacketHandler<ViolationPacket> {

    @Override
    public ViolationPacket decode(ByteBuf buffer) {
        ViolationPacket packet = new ViolationPacket();
        packet.setType(VarInts.readInt(buffer));
        packet.setSeverity(VarInts.readInt(buffer));
        packet.setPacketId(VarInts.readInt(buffer));
        packet.setMessage(ByteBufUtility.readString(buffer));
        return packet;
    }

}