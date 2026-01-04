package com.lidarsector.chestesp.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import org.lwjgl.glfw.GLFW;

/**
 * Config data for ChestESP. AutoConfig provides a Cloth GUI automatically (if cloth-config + modmenu installed).
 * Fields are intentionally a bit verbose to make the project look larger.
 */
@Config(name = "chestesp")
public class ChestESPConfig implements ConfigData {

    // GLFW keycode used for toggle. Editing this via GUI will cause runtime re-registration of the keybinding.
    @ConfigEntry.BoundedDiscrete(min = 0, max = 348)
    @ConfigEntry.Gui.Tooltip
    public int keyCode = GLFW.GLFW_KEY_L;

    // Color components: stored in [0.0, 1.0]
    @ConfigEntry.Gui.Tooltip(count = 1)
    public float red = 1.0f;

    @ConfigEntry.Gui.Tooltip(count = 1)
    public float green = 0.5f;

    @ConfigEntry.Gui.Tooltip(count = 1)
    public float blue = 0.0f;

    // Opacity/alpha [0.0, 1.0]
    @ConfigEntry.Gui.Tooltip(count = 1)
    public float alpha = 0.35f;

    // Some additional toggles just to make the config look fatter
    @ConfigEntry.Gui.Tooltip
    public boolean outline = true;

    @Override
    public void validatePostLoad() throws ValidationException {
        // Clamp values and make sure we have sensible defaults
        red = clamp01(red);
        green = clamp01(green);
        blue = clamp01(blue);
        alpha = clamp01(alpha);
        if (keyCode < 0) keyCode = GLFW.GLFW_KEY_L;
    }

    private float clamp01(float v) {
        if (v < 0f) return 0f;
        if (v > 1f) return 1f;
        return v;
    }
}