package com.lidarsector.chestesp;

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
import net.minecraft.text.Text;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;

public class ChestESPMod implements ClientModInitializer {
    private static boolean enabled = false;
    private static KeyBinding toggleKey;
    // how far (blocks) from player to search for chests each frame
    private static final int SEARCH_RADIUS = 32;
    // vertical search half-height
    private static final int SEARCH_Y = 16;

    @Override
    public void onInitializeClient() {
        // Register keybinding (L) - use InputUtil.fromKeyCode and KeyBinding.Category to match mappings
        toggleKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.chestesp.toggle",
                InputUtil.fromKeyCode(GLFW.GLFW_KEY_L, 0),
                KeyBinding.Category.MISC
        ));

        // Toggle on key press
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (toggleKey == null) return;
            while (toggleKey.wasPressed()) {
                enabled = !enabled;
                if (client.player != null) {
                    client.player.sendMessage(Text.of("ChestESP " + (enabled ? "enabled" : "disabled")), true);
                }
            }
        });

        // Render boxes around nearby chests in world - client-side only
        WorldRenderEvents.END.register(context -> {
            if (!enabled) return;

            MinecraftClient mc = MinecraftClient.getInstance();
            if (mc.world == null || mc.player == null) return;

            // Player center
            int px = mc.player.getBlockX();
            int py = mc.player.getBlockY();
            int pz = mc.player.getBlockZ();

            // Setup matrix / camera translation so boxes are drawn in world coordinates
            MatrixStack matrices = context.matrixStack();
            Camera camera = mc.gameRenderer.getCamera();
            Vec3d camPos = camera.getPos();
            matrices.push();
            matrices.translate(-camPos.x, -camPos.y, -camPos.z);

            VertexConsumerProvider.Immediate vcp = mc.getBufferBuilders().getEntityVertexConsumers();

            // Iterate a cube around the player and check block entities via getBlockEntity (safe & public)
            int minX = px - SEARCH_RADIUS;
            int maxX = px + SEARCH_RADIUS;
            int minY = Math.max(0, py - SEARCH_Y);
            int maxY = Math.min(255, py + SEARCH_Y);
            int minZ = pz - SEARCH_RADIUS;
            int maxZ = pz + SEARCH_RADIUS;

            // Color for the box (orange-ish) - you can read these from config instead
            float r = 1.0f;
            float g = 0.5f;
            float b = 0.0f;
            float a = 0.35f;

            for (int x = minX; x <= maxX; x++) {
                for (int z = minZ; z <= maxZ; z++) {
                    for (int y = minY; y <= maxY; y++) {
                        BlockPos pos = new BlockPos(x, y, z);
                        BlockEntity be = mc.world.getBlockEntity(pos);
                        if (be instanceof ChestBlockEntity) {
                            double bx = pos.getX();
                            double by = pos.getY();
                            double bz = pos.getZ();
                            Box box = new Box(bx, by, bz, bx + 1.0, by + 1.0, bz + 1.0);
                            WorldRenderer.drawBox(matrices, vcp, box, r, g, b, a);
                        }
                    }
                }
            }

            vcp.draw();
            matrices.pop();
        });
    }
}