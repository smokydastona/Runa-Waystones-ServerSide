package com.example.waystoneinjectorserver.network;

import com.example.waystoneinjectorserver.config.ServerConfig;
import com.example.waystoneinjectorserver.vault.VaultService;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class OpenVaultC2SPacket {

    public static void encode(OpenVaultC2SPacket msg, net.minecraft.network.FriendlyByteBuf buf) {
        // no payload
    }

    public static OpenVaultC2SPacket decode(net.minecraft.network.FriendlyByteBuf buf) {
        return new OpenVaultC2SPacket();
    }

    public static void handle(OpenVaultC2SPacket msg, Supplier<NetworkEvent.Context> ctx) {
        NetworkEvent.Context context = ctx.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player == null) return;
            if (!ServerConfig.VAULT_ENABLED.get()) return;
            if (ServerConfig.VAULT_REQUIRE_OP.get() && !player.hasPermissions(2)) return;
            VaultService.openVault(player);
        });
        context.setPacketHandled(true);
    }
}
