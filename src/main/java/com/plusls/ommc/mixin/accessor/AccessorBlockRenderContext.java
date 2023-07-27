package com.plusls.ommc.mixin.accessor;

import me.jellysquid.mods.sodium.client.render.chunk.compile.pipeline.BlockRenderContext;
import net.minecraft.client.resources.model.BakedModel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import top.hendrixshen.magiclib.dependency.api.annotation.Dependencies;
import top.hendrixshen.magiclib.dependency.api.annotation.Dependency;

@Dependencies(and = @Dependency(value = "sodium", versionPredicate = ">0.4.8"))
@Mixin(BlockRenderContext.class)
public interface AccessorBlockRenderContext {
    @Accessor
    void setModel(BakedModel model);
}
