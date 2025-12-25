package com.example.waystoneinjectorserver.vault;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.items.ItemStackHandler;

public class VaultData {

    public static final int SIZE = 54;

    private final ItemStackHandler items = new ItemStackHandler(SIZE);

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
