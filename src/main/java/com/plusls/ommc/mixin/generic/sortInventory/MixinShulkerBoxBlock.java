package com.plusls.ommc.mixin.generic.sortInventory;

import com.plusls.ommc.api.sortInventory.IDyeBlock;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ShulkerBoxBlock.class)
public class MixinShulkerBoxBlock implements IDyeBlock {
    @Shadow
    @Final
    private @Nullable DyeColor color;

    @Override
    public DyeColor ommc$getColor() {
        return this.color;
    }
}
