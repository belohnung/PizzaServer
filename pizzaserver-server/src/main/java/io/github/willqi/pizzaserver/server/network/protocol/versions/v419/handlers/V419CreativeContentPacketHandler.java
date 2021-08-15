package io.github.willqi.pizzaserver.server.network.protocol.versions.v419.handlers;

import com.nukkitx.network.VarInts;
import io.github.willqi.pizzaserver.server.item.Item;
import io.github.willqi.pizzaserver.server.network.protocol.packets.CreativeContentPacket;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BasePacketBuffer;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BasePacketHelper;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BaseProtocolPacketHandler;
import io.netty.buffer.ByteBuf;

public class V419CreativeContentPacketHandler extends BaseProtocolPacketHandler<CreativeContentPacket> {

    @Override
    public void encode(CreativeContentPacket packet, BasePacketBuffer buffer) {
        buffer.writeUnsignedVarInt(packet.getItems().size());
        for (Item item : packet.getItems()) {
            buffer.writeUnsignedVarInt(item.getId().ordinal());
            buffer.writeItem(item);
        }
    }

}
