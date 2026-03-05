package com.example.screammod.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import com.example.screammod.config.ModConfig;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.effect.StatusEffect;

public class ScreamMod implements ClientModInitializer {
    // Static so ModMenuIntegration can access it
    public static final ModConfig config = new ModConfig();
    private long lastScreamTime = 0;
    private long lastEffectWarningTime = 0;

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

                // --- effect expiration warning ---
                // check fire resistance, speed, strength for low remaining duration
                int thresholdTicks = config.effectThresholdSeconds * 20;
                boolean warningNeeded = false;
                for (StatusEffectInstance inst : client.player.getStatusEffects()) {
                    // convert registry entry to actual effect
                    StatusEffect effect = inst.getEffectType().value();
                    if ((effect == StatusEffects.FIRE_RESISTANCE ||
                         effect == StatusEffects.SPEED ||
                         effect == StatusEffects.STRENGTH) &&
                        inst.getDuration() > 0 &&
                        inst.getDuration() <= thresholdTicks) {
                        warningNeeded = true;
                        break;
                    }
                }
                if (warningNeeded) {
                    if (currentTime - lastEffectWarningTime >= config.effectInterval) {
                        client.player.playSound(config.getEffectSoundEvent(), 1.0f, 1.0f);
                        lastEffectWarningTime = currentTime;
                    }
                } else {
                    // reset the timer when nothing is close to expiring
                    lastEffectWarningTime = 0;
                }
            }
        });
    }
}
