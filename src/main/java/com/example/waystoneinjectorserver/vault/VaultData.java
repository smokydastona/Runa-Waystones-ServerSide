package com.example.waystoneinjectorserver.vault;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.items.ItemStackHandler;

public class VaultData {

    public static final int SIZE = 54;

    private final ItemStackHandler items;

    public VaultData() {
        this(null);
    }

    public VaultData(Player owner) {
        this.items = new ItemStackHandler(SIZE) {
            @Override
            protected void onContentsChanged(int slot) {
                VaultPersistentStorage.saveToPlayer(owner, VaultData.this);
            }
        };
    }

    public ItemStackHandler items() {
        return items;
    }

    public CompoundTag serializeNBT() {
        return items.serializeNBT();
    }

    public void deserializeNBT(CompoundTag tag) {
        items.deserializeNBT(tag);
    }
}
