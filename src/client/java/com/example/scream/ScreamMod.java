package com.example.scream;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;

public class ScreamMod implements ClientModInitializer {
    // Static so ModMenuIntegration can access it
    public static final ModConfig config = new ModConfig();
    private boolean hasScreamed = false;

    @Override
    public void onInitializeClient() {
        // Load settings from the .properties file on startup
        config.load();

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player != null) {
                float health = client.player.getHealth();
                // Minecraft health is 20 points (10 hearts). 
                // We multiply the config hearts by 2 to get the internal value.
                float threshold = config.triggerHearts * 2.0f;

                if (health < threshold && !hasScreamed) {
                    // Trigger the scream
                    client.player.playSound(SoundEvents.ENTITY_GHAST_SCREAM, 1.0f, 1.0f);
                    
                    // Optional: Overlay message
                    client.player.sendMessage(Text.of("§4❤ LOW HEALTH SCREAM! §4❤"), true);
                    
                    hasScreamed = true;
                } 
                // Reset the trigger once you heal back above the threshold
                else if (health >= threshold) {
                    hasScreamed = false;
                }
            }
        });
    }
}