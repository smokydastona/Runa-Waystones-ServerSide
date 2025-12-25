package com.example.waystoneinjectorserver.network;

import com.example.waystoneinjectorserver.WaystoneInjectorServerMod;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import java.util.function.Supplier;

/**
 * Sends the server icon to modded clients.
 *
 * Notes:
 * - This only works when the client also has this mod installed.
 * - Vanilla clients cannot receive arbitrary data like this.
 */
public record ServerIconS2CPacket(byte[] pngBytes) {

    private static final int MAX_BYTES = 128 * 1024;

    public static void encode(ServerIconS2CPacket msg, FriendlyByteBuf buf) {
        byte[] bytes = msg.pngBytes == null ? new byte[0] : msg.pngBytes;
        if (bytes.length > MAX_BYTES) {
            // Truncate defensively; we don't want to blow up the Netty buffer.
            byte[] truncated = new byte[MAX_BYTES];
            System.arraycopy(bytes, 0, truncated, 0, MAX_BYTES);
            bytes = truncated;
        }
        buf.writeVarInt(bytes.length);
        buf.writeByteArray(bytes);
    }

    public static ServerIconS2CPacket decode(FriendlyByteBuf buf) {
        int len = buf.readVarInt();
        if (len <= 0 || len > MAX_BYTES) {
            return new ServerIconS2CPacket(new byte[0]);
        }
        return new ServerIconS2CPacket(buf.readByteArray(len));
    }

    public static void handle(ServerIconS2CPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if (msg.pngBytes == null || msg.pngBytes.length == 0) {
                return;
            }
            ServerIconClientCache.set(msg.pngBytes);
            WaystoneInjectorServerMod.LOGGER.debug("Received server icon bytes: {}", msg.pngBytes.length);
        });
        ctx.get().setPacketHandled(true);
    }

    /**
     * Client-only cache used as a bridge for future UI code.
     * Keeping it inside this packet class avoids adding new client packages for now.
     */
    private static final class ServerIconClientCache {
        private static volatile byte[] last;

        private static void set(byte[] bytes) {
            last = bytes;
        }

        @SuppressWarnings("unused")
        private static byte[] get() {
            return last;
        }
    }
}
