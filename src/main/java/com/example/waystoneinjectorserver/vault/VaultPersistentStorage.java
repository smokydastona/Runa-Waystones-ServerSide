package com.example.waystoneinjectorserver.vault;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fml.ModList;

final class VaultPersistentStorage {

    private static final String PERSIST_KEY = "WaystoneInjectorServer_Vault";

    private VaultPersistentStorage() {
    }

    static boolean isPlayerSyncPresent() {
        // PlayerSync-Plus requires PlayerSync, but we also accept either being present.
        return ModList.get().isLoaded("playersync") || ModList.get().isLoaded("playersync_performance_plus");
    }

    static void loadFromPlayer(Player player, VaultData data) {
        if (player == null) return;

        CompoundTag persistent = player.getPersistentData();
        if (persistent != null && persistent.contains(PERSIST_KEY, Tag.TAG_COMPOUND)) {
            data.deserializeNBT(persistent.getCompound(PERSIST_KEY));
        }
    }

    @SuppressWarnings("null")
    static void saveToPlayer(Player player, VaultData data) {
        if (player == null) return;
        if (!isPlayerSyncPresent()) return;

        CompoundTag persistent = player.getPersistentData();
        if (persistent == null) return;

        persistent.put(PERSIST_KEY, data.serializeNBT());
    }
}
