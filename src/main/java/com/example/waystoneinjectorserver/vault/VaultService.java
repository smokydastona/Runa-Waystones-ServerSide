package com.example.waystoneinjectorserver.vault;

import com.example.waystoneinjectorserver.WaystoneInjectorServerMod;
import com.example.waystoneinjectorserver.server.ServerIconService;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.inventory.ChestMenu;

public final class VaultService {

    private VaultService() {
    }

    @SuppressWarnings("null")
    public static void openVault(ServerPlayer player) {
        player.getCapability(VaultCapability.VAULT).resolve().ifPresentOrElse(vault -> {
            ServerIconService.sendServerIconIfPresent(player);

            VaultContainer container = new VaultContainer(vault);

            MenuProvider provider = new SimpleMenuProvider(
                (containerId, inv, p) -> ChestMenu.sixRows(containerId, inv, container),
                Component.literal("Ze Voidrobe")
            );

            WaystoneInjectorServerMod.LOGGER.info("Opening vault for {}", player.getGameProfile().getName());
            player.openMenu(provider);
        }, () -> player.sendSystemMessage(Component.literal("Vault capability missing")));
    }

    public static boolean clearVault(ServerPlayer player) {
        return player.getCapability(VaultCapability.VAULT).resolve().map(vault -> {
            for (int i = 0; i < VaultData.SIZE; i++) {
                vault.items().setStackInSlot(i, net.minecraft.world.item.ItemStack.EMPTY);
            }
            return true;
        }).orElse(false);
    }
}
