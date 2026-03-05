package com.example.screammod.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import com.example.screammod.config.ModConfig;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import java.util.Collection;

public class ScreamMod implements ClientModInitializer {
    // Static so ModMenuIntegration can access it
    public static final ModConfig config = new ModConfig();
    private long lastScreamTime = 0;
    private long lastEffectWarningTime = 0;

    @Override
    public void onInitializeClient() {
        // Register custom sound events
        Registry.register(Registries.SOUND_EVENT, Identifier.of("scream-mod", "default"), SoundEvent.of(Identifier.of("scream-mod", "default")));
        Registry.register(Registries.SOUND_EVENT, Identifier.of("scream-mod", "potup"), SoundEvent.of(Identifier.of("scream-mod", "potup")));

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
                int thresholdTicks = config.effectThresholdSeconds * 20;
                Collection<StatusEffectInstance> effects = client.player.getStatusEffects();
                
                boolean warningNeeded = false;
                for (StatusEffectInstance inst : effects) {
                    // Get the RegistryEntry directly without calling .value()
                    RegistryEntry<StatusEffect> effectEntry = inst.getEffectType();
                    int duration = inst.getDuration();
                    
                    // Compare RegistryEntry to RegistryEntry using .equals()
                    boolean isTargetEffect = (effectEntry.equals(StatusEffects.FIRE_RESISTANCE) ||
                                              effectEntry.equals(StatusEffects.SPEED) ||
                                              effectEntry.equals(StatusEffects.STRENGTH));
                                              
                    boolean isLowDuration = duration > 0 && duration <= thresholdTicks;
                    
                    if (isTargetEffect && isLowDuration) {
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