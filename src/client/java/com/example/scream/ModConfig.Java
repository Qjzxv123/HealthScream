package com.example.scream;

import net.fabricmc.loader.api.FabricLoader;
import java.io.*;
import java.util.Properties;

public class ModConfig {
    // Saves to .minecraft/config/scream_mod.properties
    private static final File CONFIG_FILE = FabricLoader.getInstance().getConfigDir().resolve("scream_mod.properties").toFile();
    public float triggerHearts = 7.0f; 

    public void load() {
        if (!CONFIG_FILE.exists()) {
            save(); // Create default file if it doesn't exist
            return;
        }
        try (InputStream input = new FileInputStream(CONFIG_FILE)) {
            Properties prop = new Properties();
            prop.load(input);
            triggerHearts = Float.parseFloat(prop.getProperty("triggerHearts", "7.0"));
        } catch (IOException | NumberFormatException e) {
            System.err.println("[ScreamMod] Failed to load config!");
        }
    }

    public void save() {
        try (OutputStream output = new FileOutputStream(CONFIG_FILE)) {
            Properties prop = new Properties();
            prop.setProperty("triggerHearts", String.valueOf(triggerHearts));
            prop.store(output, "Scream Mod Config");
        } catch (IOException e) {
            System.err.println("[ScreamMod] Failed to save config!");
        }
    }
}