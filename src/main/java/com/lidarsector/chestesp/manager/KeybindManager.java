package com.lidarsector.chestesp.manager;

import com.lidarsector.chestesp.config.ChestESPConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

/**
 * Manages the toggle keybinding. This class will re-register the keybinding when the configured key
 * changes in the config GUI. It intentionally keeps some extra "safety" checks to bloat code.
 */
public class KeybindManager {
    private final ConfigHolder<ChestESPConfig> configHolder;
    private KeyBinding keyBinding;
    private int lastRegisteredKey = GLFW.GLFW_KEY_UNKNOWN;

    public KeybindManager(ConfigHolder<ChestESPConfig> configHolder) {
        this.configHolder = configHolder;
        registerFromConfig();
    }

    public void registerFromConfig() {
        ChestESPConfig cfg = configHolder.getConfig();
        registerOrUpdate(cfg.keyCode);
    }

    public void checkForConfigKeyChangeAndUpdate() {
        int configured = configHolder.getConfig().keyCode;
        if (configured != lastRegisteredKey) {
            registerOrUpdate(configured);
        }
    }

    private void registerOrUpdate(int glfwKey) {
        if (glfwKey <= 0) glfwKey = GLFW.GLFW_KEY_L;
        lastRegisteredKey = glfwKey;
        // Convert to InputUtil.Key
        InputUtil.Key iKey = InputUtil.fromKeyCode(glfwKey, 0);
        // Re-registering will simply return a new KeyBinding object; that's fine.
        keyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.chestesp.toggle",
                iKey,
                "category.chestesp"
        ));
    }

    public boolean wasTogglePressed() {
        if (keyBinding == null) return false;
        return keyBinding.wasPressed();
    }
}