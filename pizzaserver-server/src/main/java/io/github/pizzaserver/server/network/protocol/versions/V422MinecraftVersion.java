package io.github.pizzaserver.server.network.protocol.versions;

import com.nukkitx.protocol.bedrock.BedrockPacketCodec;
import com.nukkitx.protocol.bedrock.v422.Bedrock_v422;

import java.io.IOException;

public class V422MinecraftVersion extends V419MinecraftVersion {

    public static final int PROTOCOL = 422;
    public static final String VERSION = "1.16.200";


    public V422MinecraftVersion() throws IOException {}

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
        return Bedrock_v422.V422_CODEC;
    }

}