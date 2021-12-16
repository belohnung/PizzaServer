package io.github.pizzaserver.server.network.protocol.versions;

import com.nukkitx.protocol.bedrock.BedrockPacketCodec;
import com.nukkitx.protocol.bedrock.v428.Bedrock_v428;

import java.io.IOException;

public class V428MinecraftVersion extends V422MinecraftVersion {

    public static final int PROTOCOL = 428;
    public static final String VERSION = "1.16.210";


    public V428MinecraftVersion() throws IOException {}

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
        return Bedrock_v428.V428_CODEC;
    }

}