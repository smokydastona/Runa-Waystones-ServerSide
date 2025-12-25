package com.example.waystoneinjectorserver.vault;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.MenuType;

public class VaultCommands {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
            Commands.literal("vault")
                .requires(src -> src.getEntity() instanceof ServerPlayer)
                .executes(ctx -> {
                    ServerPlayer player = ctx.getSource().getPlayerOrException();
                    openVault(player);
                    return 1;
                })
        );
    }

    private static void openVault(ServerPlayer player) {
        player.getCapability(VaultCapability.VAULT).ifPresentOrElse(vault -> {
            VaultContainer container = new VaultContainer(vault);

            MenuProvider provider = new SimpleMenuProvider(
                (containerId, inv, p) -> ChestMenu.sixRows(containerId, inv, container),
                Component.literal("Vault")
            );

            player.openMenu(provider);
        }, () -> player.sendSystemMessage(Component.literal("Vault capability missing")));
    }
}
