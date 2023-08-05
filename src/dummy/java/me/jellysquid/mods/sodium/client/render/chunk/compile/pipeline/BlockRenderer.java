package me.jellysquid.mods.sodium.client.render.chunk.compile.pipeline;

import me.jellysquid.mods.sodium.client.render.chunk.compile.ChunkBuildBuffers;

//#if MC > 11904 || MC < 11904
import me.jellysquid.mods.sodium.client.render.chunk.compile.buffers.ChunkModelBuilder;
//#else
//$$ import me.jellysquid.mods.sodium.client.render.chunk.data.ChunkRenderBounds;
//#endif

public class BlockRenderer {
    public native void renderModel(BlockRenderContext ctx, ChunkBuildBuffers buffers);

    //#if MC > 11904 || MC < 11904
    public native boolean renderModel(BlockRenderContext ctx, ChunkModelBuilder buffers);
    //#else
    //$$ public native void renderModel(BlockRenderContext ctx, ChunkBuildBuffers buffers, ChunkRenderBounds.Builder bounds);
    //#endif
}
