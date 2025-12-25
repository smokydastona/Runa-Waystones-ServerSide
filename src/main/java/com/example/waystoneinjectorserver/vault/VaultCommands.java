package com.example.waystoneinjectorserver.vault;

import com.example.waystoneinjectorserver.WaystoneInjectorServerMod;
import com.example.waystoneinjectorserver.config.ServerConfig;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.List;

public class VaultCommands {

    @SuppressWarnings("null")
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
            Commands.literal("vault")
                .requires(src -> {
                    if (!(src.getEntity() instanceof ServerPlayer)) return false;
                    if (!ServerConfig.VAULT_ENABLED.get()) return false;
                    return !ServerConfig.VAULT_REQUIRE_OP.get() || src.hasPermission(2);
                })
                .executes(ctx -> {
                    ServerPlayer player = ctx.getSource().getPlayerOrException();
                    if (!canOpenVault(player)) {
                        return 0;
                    }
                    VaultService.openVault(player);
                    return 1;
                })
        );

        dispatcher.register(
            Commands.literal("vaultclear")
                .requires(src -> src.hasPermission(2))
                .then(Commands.argument("player", EntityArgument.player())
                    .executes(ctx -> {
                        ServerPlayer target = EntityArgument.getPlayer(ctx, "player");
                        boolean ok = VaultService.clearVault(target);
                        if (ok) {
                            ctx.getSource().sendSuccess(() -> Component.literal("Cleared vault for " + target.getGameProfile().getName()), true);
                            WaystoneInjectorServerMod.LOGGER.info("Vault cleared for {}", target.getGameProfile().getName());
                            return 1;
                        }
                        ctx.getSource().sendFailure(Component.literal("Failed to clear vault for " + target.getGameProfile().getName()));
                        return 0;
                    })
                )
        );
    }

    @SuppressWarnings("null")
    private static boolean canOpenVault(ServerPlayer player) {
        if (!ServerConfig.VAULT_ALLOW_SPECTATOR.get() && player.isSpectator()) {
            player.sendSystemMessage(Component.literal("You can't open the vault in spectator mode."));
            return false;
        }
        if (!ServerConfig.VAULT_ALLOW_WHILE_DEAD.get() && !player.isAlive()) {
            player.sendSystemMessage(Component.literal("You can't open the vault while dead."));
            return false;
        }

        List<? extends String> allowed = ServerConfig.VAULT_ALLOWED_DIMENSIONS.get();
        if (allowed != null && !allowed.isEmpty()) {
            String dim = player.level().dimension().location().toString();
            if (!allowed.contains(dim)) {
                player.sendSystemMessage(Component.literal("Vault is not allowed in this dimension."));
                return false;
            }
        }

        return true;
    }
}
