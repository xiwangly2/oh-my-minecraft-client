package me.jellysquid.mods.sodium.client.render.chunk.compile.pipeline;


import me.jellysquid.mods.sodium.client.render.chunk.compile.ChunkBuildBuffers;
import me.jellysquid.mods.sodium.client.render.chunk.compile.buffers.ChunkModelBuilder;
import me.jellysquid.mods.sodium.client.render.chunk.data.ChunkRenderBounds;

public class BlockRenderer {
    public native void renderModel(BlockRenderContext ctx, ChunkBuildBuffers buffers);

    public native boolean renderModel(BlockRenderContext ctx, ChunkModelBuilder buffers);

    public native void renderModel(BlockRenderContext ctx, ChunkBuildBuffers buffers, ChunkRenderBounds.Builder bounds);
}
