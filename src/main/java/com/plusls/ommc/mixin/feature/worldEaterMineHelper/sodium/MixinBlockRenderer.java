package com.plusls.ommc.mixin.feature.worldEaterMineHelper.sodium;

import com.plusls.ommc.feature.worldEaterMineHelper.WorldEaterMineHelperUtil;
import com.plusls.ommc.mixin.accessor.AccessorBlockRenderContext;
import com.plusls.ommc.mixin.accessor.AccessorBlockStateBase;
import me.jellysquid.mods.sodium.client.render.chunk.compile.buffers.ChunkModelBuilder;
import me.jellysquid.mods.sodium.client.render.chunk.compile.pipeline.BlockRenderContext;
import me.jellysquid.mods.sodium.client.render.chunk.compile.pipeline.BlockRenderer;
import net.minecraft.client.resources.model.BakedModel;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.hendrixshen.magiclib.dependency.api.annotation.Dependencies;
import top.hendrixshen.magiclib.dependency.api.annotation.Dependency;

//#if MC == 11904
//$$ import me.jellysquid.mods.sodium.client.render.chunk.compile.ChunkBuildBuffers;
//$$ import me.jellysquid.mods.sodium.client.render.chunk.data.ChunkRenderBounds;
//$$ import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//#endif

@Dependencies(and = @Dependency(value = "sodium", versionPredicate = ">0.4.8"))
@Mixin(value = BlockRenderer.class, remap = false)
public abstract class MixinBlockRenderer {
    @Shadow(remap = false)
    //#if MC > 11904 || MC < 11904
    public abstract boolean renderModel(BlockRenderContext ctx, ChunkModelBuilder buffers);
    //#else
    //$$ public abstract void renderModel(BlockRenderContext ctx, ChunkBuildBuffers buffers, ChunkRenderBounds.Builder bounds);
    //#endif

    @Unique
    private final ThreadLocal<Boolean> ommc$renderTag = ThreadLocal.withInitial(() -> false);

    @Inject(
            method = "renderModel",
            at = @At(
                    value = "RETURN"
            )
    )
    private void postRenderModel(
            @NotNull BlockRenderContext ctx,
            //#if MC > 11904 || MC < 11904
            ChunkModelBuilder buffers,
            CallbackInfoReturnable<Boolean> cir
            //#else
            //$$ ChunkBuildBuffers buffers,
            //$$ ChunkRenderBounds.Builder bounds,
            //$$ CallbackInfo ci
            //#endif
    ) {
        if (WorldEaterMineHelperUtil.shouldUseCustomModel(ctx.state(), ctx.pos()) && !this.ommc$renderTag.get()) {
            BakedModel customModel = WorldEaterMineHelperUtil.customModels.get(ctx.state().getBlock());

            if (customModel != null) {
                this.ommc$renderTag.set(true);
                // This impl will break light system, so disable it.
                // int originalLightEmission = ctx.state().getLightEmission();
                BakedModel originalModel = ctx.model();
                // ((AccessorBlockStateBase) ctx.state()).setLightEmission(15);
                ((AccessorBlockRenderContext) ctx).setModel(customModel);
                //#if MC > 11904 || MC < 11904
                this.renderModel(ctx, buffers);
                //#else
                //$$ this.renderModel(ctx, buffers, bounds);
                //#endif
                ((AccessorBlockRenderContext) ctx).setModel(originalModel);
                // ((AccessorBlockStateBase) ctx.state()).setLightEmission(originalLightEmission);
                this.ommc$renderTag.set(false);
            }
        }
    }
}
