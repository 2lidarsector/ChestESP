package com.lidarsector.chestesp;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

import java.util.Collection;

public class ChestESPMod implements ClientModInitializer {
    private static boolean enabled = false;
    private static KeyBinding toggleKey;

    @Override
    public void onInitializeClient() {
        // Register keybinding (L)
        toggleKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.chestesp.toggle",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_L,
                "category.chestesp"
        ));

        // Toggle on key press
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (toggleKey.wasPressed()) {
                enabled = !enabled;
                if (client.player != null) {
                    client.player.sendMessage(Text.of("ChestESP " + (enabled ? "enabled" : "disabled")), true);
                }
            }
        });

        // Render boxes around chests in world - client-side only
        WorldRenderEvents.END.register(context -> {
            if (!enabled) return;

            MinecraftClient mc = MinecraftClient.getInstance();
            if (mc.world == null) return;

            // Setup matrix / camera translation so boxes are drawn in world coordinates
            MatrixStack matrices = context.matrixStack();
            Camera camera = mc.gameRenderer.getCamera();
            Vec3d camPos = camera.getPos();
            matrices.push();
            matrices.translate(-camPos.x, -camPos.y, -camPos.z);

            VertexConsumerProvider.Immediate vcp = mc.getBufferBuilders().getEntityVertexConsumers();

            // Iterate block entities and draw simple translucent boxes for chests
            // (This is client-side-only and does not notify the server)
            Collection<BlockEntity> blockEntities = mc.world.blockEntities.values();
            for (BlockEntity be : blockEntities) {
                if (be instanceof ChestBlockEntity) {
                    BlockPos pos = be.getPos();
                    double x = pos.getX();
                    double y = pos.getY();
                    double z = pos.getZ();
                    Box box = new Box(x, y, z, x + 1.0, y + 1.0, z + 1.0);
                    // drawBox(MatrixStack, VertexConsumerProvider, Box, r,g,b,a)
                    WorldRenderer.drawBox(matrices, vcp, box, 1.0f, 0.5f, 0.0f, 0.35f);
                }
            }

            // flush
            vcp.draw();
            matrices.pop();
            RenderSystem.enableDepthTest();
        });
    }
}