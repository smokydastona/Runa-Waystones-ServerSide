package com.example.waystoneinjectorserver.network;

import com.example.waystoneinjectorserver.WaystoneInjectorServerMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public final class NetworkHandler {

    private static final String PROTOCOL = "1";

    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
        new ResourceLocation(WaystoneInjectorServerMod.MODID, "main"),
        () -> PROTOCOL,
        PROTOCOL::equals,
        PROTOCOL::equals
    );

    private static int nextId = 0;

    private NetworkHandler() {
    }

    public static void init() {
        CHANNEL.messageBuilder(OpenVaultC2SPacket.class, nextId++)
            .encoder(OpenVaultC2SPacket::encode)
            .decoder(OpenVaultC2SPacket::decode)
            .consumerMainThread(OpenVaultC2SPacket::handle)
            .add();

        CHANNEL.messageBuilder(ServerIconS2CPacket.class, nextId++)
            .encoder(ServerIconS2CPacket::encode)
            .decoder(ServerIconS2CPacket::decode)
            .consumerMainThread(ServerIconS2CPacket::handle)
            .add();
    }
}
