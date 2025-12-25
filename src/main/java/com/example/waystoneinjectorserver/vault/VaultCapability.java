package com.example.waystoneinjectorserver.vault;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;

public final class VaultCapability {

    public static final Capability<VaultData> VAULT = CapabilityManager.get(new CapabilityToken<>() {});

    private VaultCapability() {
    }
}
