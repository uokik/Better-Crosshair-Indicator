package net.szokk.bettercrosshairindicator.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class CrosshairConfig {
    private static CrosshairConfig INSTANCE;
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final File CONFIG_FILE = new File(
            FabricLoader.getInstance().getConfigDir().toFile(),
            "bettercrosshairindicator.json"
    );

    private int red = 255;
    private int green = 255;
    private int blue = 255;
    private int alpha = 255;
    private int size = 4;


    private int topLeftOffsetX = 0;
    private int topLeftOffsetY = 0;
    private int topRightOffsetX = 0;
    private int topRightOffsetY = 0;
    private int bottomLeftOffsetX = 0;
    private int bottomLeftOffsetY = 0;
    private int bottomRightOffsetX = 0;
    private int bottomRightOffsetY = 0;

    private CrosshairConfig() {}

    public static CrosshairConfig getInstance() {
        if (INSTANCE == null) {
            INSTANCE = load();
        }
        return INSTANCE;
    }

    public int getTopLeftOffsetX() { return topLeftOffsetX; }
    public void setTopLeftOffsetX(int offset) {
        this.topLeftOffsetX = Math.max(-10, Math.min(10, offset));
        save();
    }

    public int getTopLeftOffsetY() { return topLeftOffsetY; }
    public void setTopLeftOffsetY(int offset) {
        this.topLeftOffsetY = Math.max(-10, Math.min(10, offset));
        save();
    }

    public int getTopRightOffsetX() { return topRightOffsetX; }
    public void setTopRightOffsetX(int offset) {
        this.topRightOffsetX = Math.max(-10, Math.min(10, offset));
        save();
    }

    public int getTopRightOffsetY() { return topRightOffsetY; }
    public void setTopRightOffsetY(int offset) {
        this.topRightOffsetY = Math.max(-10, Math.min(10, offset));
        save();
    }

    public int getBottomLeftOffsetX() { return bottomLeftOffsetX; }
    public void setBottomLeftOffsetX(int offset) {
        this.bottomLeftOffsetX = Math.max(-10, Math.min(10, offset));
        save();
    }

    public int getBottomLeftOffsetY() { return bottomLeftOffsetY; }
    public void setBottomLeftOffsetY(int offset) {
        this.bottomLeftOffsetY = Math.max(-10, Math.min(10, offset));
        save();
    }

    public int getBottomRightOffsetX() { return bottomRightOffsetX; }
    public void setBottomRightOffsetX(int offset) {
        this.bottomRightOffsetX = Math.max(-10, Math.min(10, offset));
        save();
    }

    public int getBottomRightOffsetY() { return bottomRightOffsetY; }
    public void setBottomRightOffsetY(int offset) {
        this.bottomRightOffsetY = Math.max(-10, Math.min(10, offset));
        save();
    }

    public void resetOffsets() {
        this.topLeftOffsetX = 0;
        this.topLeftOffsetY = 0;
        this.topRightOffsetX = 0;
        this.topRightOffsetY = 0;
        this.bottomLeftOffsetX = 0;
        this.bottomLeftOffsetY = 0;
        this.bottomRightOffsetX = 0;
        this.bottomRightOffsetY = 0;
        save();
    }

    private static CrosshairConfig load() {
        if (CONFIG_FILE.exists()) {
            try (FileReader reader = new FileReader(CONFIG_FILE)) {
                CrosshairConfig config = GSON.fromJson(reader, CrosshairConfig.class);
                if (config != null) {
                    return config;
                }
            } catch (IOException e) {
                System.err.println("[BetterCrosshairIndicator] failed to load cfg: " + e.getMessage());
            }
        }
        return new CrosshairConfig();
    }

    public void save() {
        try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
            GSON.toJson(this, writer);
        } catch (IOException e) {
            System.err.println("[BetterCrosshairIndicator] failed to save cfg: " + e.getMessage());
        }
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = Math.max(1, Math.min(50, size));
        save();
    }

    public int getRed() { return red; }
    public int getGreen() { return green; }
    public int getBlue() { return blue; }
    public int getAlpha() { return alpha; }

    public void setRed(int red) {
        this.red = Math.max(0, Math.min(255, red));
        save();
    }

    public void setGreen(int green) {
        this.green = Math.max(0, Math.min(255, green));
        save();
    }

    public void setBlue(int blue) {
        this.blue = Math.max(0, Math.min(255, blue));
        save();
    }

    public void setAlpha(int alpha) {
        this.alpha = Math.max(0, Math.min(255, alpha));
        save();
    }

    public void setColor(int red, int green, int blue, int alpha) {
        this.red = Math.max(0, Math.min(255, red));
        this.green = Math.max(0, Math.min(255, green));
        this.blue = Math.max(0, Math.min(255, blue));
        this.alpha = Math.max(0, Math.min(255, alpha));
        save();
    }

    public void setColor(int red, int green, int blue) {
        setColor(red, green, blue, 255);
    }

    public int getColorARGB() {
        return (alpha << 24) | (red << 16) | (green << 8) | blue;
    }
}
