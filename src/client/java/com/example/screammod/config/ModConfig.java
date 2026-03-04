package com.example.screammod.config;

import java.io.*;
import java.util.Properties;
import net.fabricmc.loader.api.FabricLoader;

public class ModConfig {
    // Saves to .minecraft/config/scream_mod.properties
    private File CONFIG_FILE;
    public float triggerHearts = 7.0f;

    public ModConfig() {
        CONFIG_FILE = new File(FabricLoader.getInstance().getConfigDir().toFile(), "scream_mod.properties");
    } 

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
