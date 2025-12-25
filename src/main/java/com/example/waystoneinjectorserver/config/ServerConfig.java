package com.example.waystoneinjectorserver.config;

import net.minecraftforge.common.ForgeConfigSpec;

import java.util.List;

public final class ServerConfig {

    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.BooleanValue VAULT_ENABLED;
    public static final ForgeConfigSpec.BooleanValue VAULT_REQUIRE_OP;
    public static final ForgeConfigSpec.BooleanValue VAULT_ALLOW_SPECTATOR;
    public static final ForgeConfigSpec.BooleanValue VAULT_ALLOW_WHILE_DEAD;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> VAULT_ALLOWED_DIMENSIONS;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

        builder.push("vault");
        builder.comment(
            "Vault (personal 54-slot storage)",
            "- Stored per-player (capability), not tied to the world.",
            "- Separate from vanilla Ender Chest.",
            "- Open with /vault."
        );

        VAULT_ENABLED = builder
            .comment("Enable the /vault feature")
            .define("enabled", true);

        VAULT_REQUIRE_OP = builder
            .comment("If true, only permission level 2+ can use /vault")
            .define("requireOp", false);

        VAULT_ALLOW_SPECTATOR = builder
            .comment("If false, spectators cannot open the vault")
            .define("allowSpectator", false);

        VAULT_ALLOW_WHILE_DEAD = builder
            .comment("If false, dead players cannot open the vault")
            .define("allowWhileDead", false);

        VAULT_ALLOWED_DIMENSIONS = builder
            .comment(
                "Optional allowlist of dimensions where /vault can be used.",
                "Use full ids like 'minecraft:overworld'.",
                "Empty list = allowed everywhere."
            )
            .defineListAllowEmpty(
                "allowedDimensions",
                List.of(),
                o -> o instanceof String s && s.contains(":")
            );

        builder.pop();

        SPEC = builder.build();
    }

    private ServerConfig() {
    }
}
