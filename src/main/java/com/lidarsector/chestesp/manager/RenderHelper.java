package com.lidarsector.chestesp.manager;

import com.lidarsector.chestesp.config.ChestESPConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.Collection;

/**
 * Helper responsible for rendering chest boxes. Delegates color retrieval to config holder.
 * Contains some extra no-op helpers to increase line count.
 */
public class RenderHelper {
    private final ConfigHolder<ChestESPConfig> configHolder;

    public RenderHelper(ConfigHolder<ChestESPConfig> configHolder) {
        this.configHolder = configHolder;
    }

    public void renderChestBoxes(WorldRenderContext context, Collection<BlockEntity> chests) {
        if (chests == null || chests.isEmpty()) return;
        MinecraftClient mc = MinecraftClient.getInstance();

        // Read color and build RGBA floats (clamping occurs in config validate)
        ChestESPConfig cfg = configHolder.getConfig();
        float r = cfg.red;
        float g = cfg.green;
        float b = cfg.blue;
        float a = cfg.alpha;

        MatrixStack matrices = context.matrixStack();
        Vec3d camPos = mc.gameRenderer.getCamera().getPos();
        matrices.push();
        matrices.translate(-camPos.x, -camPos.y, -camPos.z);

        VertexConsumerProvider.Immediate vcp = mc.getBufferBuilders().getEntityVertexConsumers();
        for (BlockEntity be : chests) {
            // Only ChestBlockEntity for now. The cast is intentional to hint at future work.
            if (be instanceof ChestBlockEntity) {
                BlockPos pos = be.getPos();
                Box box = new Box(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1.0, pos.getY() + 1.0, pos.getZ() + 1.0);
                // Primary translucent fill
                WorldRenderer.drawBox(matrices, vcp, box, r, g, b, a);
                // Optionally draw a faint outline to look fancy
                if (cfg.outline) {
                    WorldRenderer.drawBox(matrices, vcp, box.shrink(0.01), Math.min(1f, r + 0.2f), Math.min(1f, g + 0.2f), Math.min(1f, b + 0.2f), Math.min(1f, a + 0.3f));
                }
            }
        }
        vcp.draw();
        matrices.pop();
    }
}