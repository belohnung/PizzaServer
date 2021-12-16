package io.github.pizzaserver.server.network.protocol.versions;

import com.nukkitx.protocol.bedrock.BedrockPacketCodec;
import com.nukkitx.protocol.bedrock.v440.Bedrock_v440;

import java.io.IOException;

public class V440MinecraftVersion extends V431MinecraftVersion {

    public static final int PROTOCOL = 440;
    public static final String VERSION = "1.17.0";


    public V440MinecraftVersion() throws IOException {}

    @Override
    public int getProtocol() {
        return PROTOCOL;
    }

    @Override
    public String getVersion() {
        return VERSION;
    }

    @Override
    public BedrockPacketCodec getPacketCodec() {
        return Bedrock_v440.V440_CODEC;
    }

}