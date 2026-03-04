package com.example.screammod.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import com.example.screammod.config.ModConfig;

public class ScreamMod implements ClientModInitializer {
    // Static so ModMenuIntegration can access it
    public static final ModConfig config = new ModConfig();
    private long lastScreamTime = 0;

    @Override
    public void onInitializeClient() {
        // Load settings from the .properties file on startup
        config.load();

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player != null) {
                float health = client.player.getHealth();
                // Threshold is directly in health points (1-20)
                float threshold = config.triggerHearts;
                long currentTime = System.currentTimeMillis();

                if (health < threshold) {
                    // Keep screaming at intervals while below threshold
                    if (currentTime - lastScreamTime >= config.screamInterval) {
                        client.player.playSound(config.getSoundEvent(), 1.0f, 1.0f);
                        lastScreamTime = currentTime;
                    }
                } 
                // Reset the timer once you heal back above the threshold
                else {
                    lastScreamTime = 0;
                }
            }
        });
    }
}
