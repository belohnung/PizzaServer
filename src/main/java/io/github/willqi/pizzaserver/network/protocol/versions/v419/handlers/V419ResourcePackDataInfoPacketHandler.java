package io.github.willqi.pizzaserver.network.protocol.versions.v419.handlers;

import io.github.willqi.pizzaserver.network.protocol.packets.ResourcePackDataInfoPacket;
import io.github.willqi.pizzaserver.network.protocol.versions.ProtocolPacketHandler;
import io.github.willqi.pizzaserver.network.utils.ByteBufUtility;
import io.netty.buffer.ByteBuf;

public class V419ResourcePackDataInfoPacketHandler implements ProtocolPacketHandler<ResourcePackDataInfoPacket> {
    @Override
    public ResourcePackDataInfoPacket decode(ByteBuf buffer) {
        return null;
    }

    @Override
    public void encode(ResourcePackDataInfoPacket packet, ByteBuf buffer) {
        ByteBufUtility.writeString(packet.getPackId() + "_" + packet.getVersion(), buffer);
        buffer.writeIntLE(packet.getMaxChunkSize());
        buffer.writeIntLE(packet.getChunkCount());
        buffer.writeLongLE(packet.getCompressedPackageSize());
        ByteBufUtility.writeByteArray(packet.getHash(), buffer);
        buffer.writeBoolean(packet.isPremium());
        buffer.writeByte(packet.getType().ordinal());
    }
}
