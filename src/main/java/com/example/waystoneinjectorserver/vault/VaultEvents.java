package com.example.waystoneinjectorserver.vault;

import com.example.waystoneinjectorserver.WaystoneInjectorServerMod;
import com.example.waystoneinjectorserver.server.ServerIconService;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class VaultEvents {

    private static final ResourceLocation VAULT_ID = new ResourceLocation(WaystoneInjectorServerMod.MODID, "vault");

    @SubscribeEvent
    public static void onAttachCapabilities(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player) {
            event.addCapability(VAULT_ID, new VaultProvider((Player) event.getObject()));
        }
    }

    @SubscribeEvent
    public static void onClone(PlayerEvent.Clone event) {
        // Keep vault on respawn.
        if (!event.isWasDeath()) {
            return;
        }

        Player original = event.getOriginal();
        Player clone = event.getEntity();

        LazyOptional<VaultData> originalVault = original.getCapability(VaultCapability.VAULT);
        LazyOptional<VaultData> cloneVault = clone.getCapability(VaultCapability.VAULT);

        if (originalVault.isPresent() && cloneVault.isPresent()) {
            VaultData from = originalVault.orElseThrow(IllegalStateException::new);
            VaultData to = cloneVault.orElseThrow(IllegalStateException::new);
            CompoundTag tag = from.serializeNBT();
            to.deserializeNBT(tag);

            // If PlayerSync is installed, keep the persistent copy in sync as well.
            VaultPersistentStorage.saveToPlayer(clone, to);
        }
    }

    @SubscribeEvent
    public static void onPlayerLogin(PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof net.minecraft.server.level.ServerPlayer sp) {
            ServerIconService.sendServerIconIfPresent(sp);
        }
    }
}
