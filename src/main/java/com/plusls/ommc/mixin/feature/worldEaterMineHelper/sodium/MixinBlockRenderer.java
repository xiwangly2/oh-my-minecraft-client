package com.plusls.ommc.mixin.feature.worldEaterMineHelper.sodium;

import com.plusls.ommc.feature.worldEaterMineHelper.BlockModelRendererContext;
import com.plusls.ommc.feature.worldEaterMineHelper.WorldEaterMineHelperUtil;
import com.plusls.ommc.mixin.accessor.AccessorBlockStateBase;
import me.jellysquid.mods.sodium.client.render.chunk.compile.buffers.ChunkModelBuilder;
import me.jellysquid.mods.sodium.client.render.chunk.compile.pipeline.BlockRenderContext;
import me.jellysquid.mods.sodium.client.render.chunk.compile.pipeline.BlockRenderer;
import net.minecraft.client.resources.model.BakedModel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.hendrixshen.magiclib.dependency.api.annotation.Dependencies;
import top.hendrixshen.magiclib.dependency.api.annotation.Dependency;

@Dependencies(and = @Dependency(value = "sodium", versionPredicate = ">0.4.8"))
@Mixin(value = BlockRenderer.class, remap = false)
public class MixinBlockRenderer {
    private final ThreadLocal<BlockModelRendererContext> ommcRenderContext = ThreadLocal.withInitial(BlockModelRendererContext::new);
    private final ThreadLocal<Integer> ommcOriginalLuminance = ThreadLocal.withInitial(() -> -1);

    @Inject(
            method = "renderModel",
            at = @At(
                    value = "HEAD"
            )
    )
    private void initRenderContext(BlockRenderContext ctx, ChunkModelBuilder buffers, CallbackInfoReturnable<Boolean> cir) {
        BlockModelRendererContext context = ommcRenderContext.get();
        context.pos = ctx.pos();
        context.state = ctx.state();
    }

    @ModifyArg(
            method = "renderModel",
            at = @At(
                    value = "INVOKE",
                    target = "Lme/jellysquid/mods/sodium/client/render/chunk/compile/pipeline/BlockRenderer;getLightingMode(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/client/resources/model/BakedModel;)Lme/jellysquid/mods/sodium/client/model/light/LightMode;"

            )
    )
    private BakedModel modifyBakedModel(BakedModel bakedModel) {
        BakedModel ret = this.ommc$cGetCustomModel();

        return ret == null ? bakedModel : ret;
    }

    @Inject(method = "renderModel", at = @At(value = "RETURN"))
    private void postRenderModel(BlockRenderContext ctx, ChunkModelBuilder buffers, CallbackInfoReturnable<Boolean> cir) {
        int originalLuminance = ommcOriginalLuminance.get();
        if (originalLuminance != -1) {
            ((AccessorBlockStateBase) ctx.state()).setLightEmission(originalLuminance);
            ommcOriginalLuminance.set(-1);
        }
    }

    @Redirect(
            method = "getGeometry",
            at = @At(
                    value = "INVOKE",
                    target = "Lme/jellysquid/mods/sodium/client/render/chunk/compile/pipeline/BlockRenderContext;model()Lnet/minecraft/client/resources/model/BakedModel;"
            )
    )
    private BakedModel postGetGeometry(@NotNull BlockRenderContext context) {
        BakedModel ret = this.ommc$cGetCustomModel();

        return ret == null ? context.model() : ret;
    }

    @Unique
    private @Nullable BakedModel ommc$cGetCustomModel() {
        BlockModelRendererContext context = ommcRenderContext.get();

        if (WorldEaterMineHelperUtil.shouldUseCustomModel(context.state, context.pos)) {
            BakedModel bakedModel = WorldEaterMineHelperUtil.customFullModels.get(context.state.getBlock());

            if (bakedModel != null) {
                ommcOriginalLuminance.set(((AccessorBlockStateBase) context.state).getLightEmission());
                ((AccessorBlockStateBase) context.state).setLightEmission(15);
                return bakedModel;
            }
        }

        return null;
    }
}
