package com.example.waystoneinjectorserver.server;

import com.example.waystoneinjectorserver.WaystoneInjectorServerMod;
import com.example.waystoneinjectorserver.network.NetworkHandler;
import com.example.waystoneinjectorserver.network.ServerIconS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.PacketDistributor;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.file.Files;

public final class ServerIconService {

    private static final String ICON_FILENAME = "server-icon.png";
    private static final int EXPECTED_SIZE = 64;
    private static final int MAX_BYTES = 128 * 1024;

    private static volatile long cachedLastModified = -1L;
    private static volatile byte[] cachedPng;

    private ServerIconService() {
    }

    public static void sendServerIconIfPresent(ServerPlayer player) {
        MinecraftServer server = player.getServer();
        if (server == null) {
            return;
        }

        byte[] png = loadServerIconPng(server);
        if (png == null || png.length == 0) {
            return;
        }

        try {
            NetworkHandler.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new ServerIconS2CPacket(png));
        } catch (Exception e) {
            // If the channel isn't present client-side, Forge will already be unhappy.
            WaystoneInjectorServerMod.LOGGER.debug("Failed sending server icon to {}", player.getGameProfile().getName());
        }
    }

    private static byte[] loadServerIconPng(MinecraftServer server) {
        try {
            File iconFile = server.getFile(ICON_FILENAME);
            if (iconFile == null || !iconFile.isFile()) {
                return null;
            }

            long lastModified = iconFile.lastModified();
            byte[] cached = cachedPng;
            if (cached != null && cachedLastModified == lastModified) {
                return cached;
            }

            // Fast path: reject huge files.
            if (Files.size(iconFile.toPath()) > MAX_BYTES) {
                WaystoneInjectorServerMod.LOGGER.warn("{} is too large; ignoring", ICON_FILENAME);
                return null;
            }

            BufferedImage img = ImageIO.read(iconFile);
            if (img == null) {
                return null;
            }

            if (img.getWidth() != EXPECTED_SIZE || img.getHeight() != EXPECTED_SIZE) {
                WaystoneInjectorServerMod.LOGGER.warn("{} must be {}x{}; got {}x{}", ICON_FILENAME, EXPECTED_SIZE, EXPECTED_SIZE, img.getWidth(), img.getHeight());
                return null;
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ImageIO.write(img, "png", out);
            byte[] bytes = out.toByteArray();
            if (bytes.length > MAX_BYTES) {
                return null;
            }

            cachedLastModified = lastModified;
            cachedPng = bytes;
            return bytes;
        } catch (Exception e) {
            return null;
        }
    }
}
