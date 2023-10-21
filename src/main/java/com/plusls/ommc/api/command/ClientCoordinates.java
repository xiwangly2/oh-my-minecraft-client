package com.plusls.ommc.api.command;

import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

// Modified from brigadier
public interface ClientCoordinates {
    Vec3 getPosition(FabricClientCommandSource commandSourceStack);

    Vec2 getRotation(FabricClientCommandSource commandSourceStack);

    default BlockPos getBlockPos(FabricClientCommandSource commandSourceStack) {
        //#if MC > 11903
        return BlockPos.containing(this.getPosition(commandSourceStack));
        //#else
        //$$ return new BlockPos(this.getPosition(commandSourceStack));
        //#endif
    }

    boolean isXRelative();

    boolean isYRelative();

    boolean isZRelative();
}
