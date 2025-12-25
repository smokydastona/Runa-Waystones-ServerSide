package com.example.waystoneinjectorserver.vault;

import javax.annotation.Nonnull;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

/**
 * Vanilla Container bridge backed by a Forge ItemStackHandler.
 * This allows opening a standard ChestMenu without any client mod.
 */
public class VaultContainer implements Container {

    private final VaultData data;

    public VaultContainer(VaultData data) {
        this.data = data;
    }

    @Override
    public int getContainerSize() {
        return VaultData.SIZE;
    }

    @Override
    public boolean isEmpty() {
        for (int i = 0; i < getContainerSize(); i++) {
            if (!data.items().getStackInSlot(i).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack getItem(int slot) {
        return data.items().getStackInSlot(slot);
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        ItemStack existing = data.items().getStackInSlot(slot);
        if (existing.isEmpty() || amount <= 0) {
            return ItemStack.EMPTY;
        }

        ItemStack split = existing.split(amount);
        data.items().setStackInSlot(slot, existing);
        setChanged();
        return split;
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        ItemStack existing = data.items().getStackInSlot(slot);
        if (existing.isEmpty()) {
            return ItemStack.EMPTY;
        }
        data.items().setStackInSlot(slot, ItemStack.EMPTY);
        return existing;
    }

    @Override
    public void setItem(int slot, @Nonnull ItemStack stack) {
        data.items().setStackInSlot(slot, stack);
        setChanged();
    }

    @Override
    public void setChanged() {
        // ItemStackHandler already marks itself dirty; nothing extra needed.
    }

    @Override
    public boolean stillValid(@Nonnull Player player) {
        return true;
    }

    @Override
    public void clearContent() {
        for (int i = 0; i < getContainerSize(); i++) {
            data.items().setStackInSlot(i, ItemStack.EMPTY);
        }
    }
}
