package com.example.screammod.config;

import java.io.*;
import java.util.Properties;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.sound.SoundEvent;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

public class ModConfig {
    // Saves to .minecraft/config/scream_mod.properties
    private File CONFIG_FILE;
    public float triggerHearts = 14.0f;
    public long screamInterval = 500L; // milliseconds
    public String soundId = "minecraft:entity.ghast.scream";

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
            screamInterval = Long.parseLong(prop.getProperty("screamInterval", "500"));
            soundId = prop.getProperty("soundId", "minecraft:entity.ghast.scream");
        } catch (IOException | NumberFormatException e) {
            System.err.println("[ScreamMod] Failed to load config!");
        }
    }

    public void save() {
        try (OutputStream output = new FileOutputStream(CONFIG_FILE)) {
            Properties prop = new Properties();
            prop.setProperty("triggerHearts", String.valueOf(triggerHearts));
            prop.setProperty("screamInterval", String.valueOf(screamInterval));
            prop.setProperty("soundId", soundId);
            prop.store(output, "Scream Mod Config");
        } catch (IOException e) {
            System.err.println("[ScreamMod] Failed to save config!");
        }
    }

    public SoundEvent getSoundEvent() {
        try {
            Identifier id = Identifier.tryParse(soundId);
            if (id != null) {
                SoundEvent sound = Registries.SOUND_EVENT.get(id);
                if (sound != null) {
                    return sound;
                }
            }
        } catch (Exception e) {
            System.err.println("[ScreamMod] Failed to get sound: " + soundId);
        }
        // Fallback to default sound
        return Registries.SOUND_EVENT.get(Identifier.of("minecraft", "entity.ghast.scream"));
    }
}
