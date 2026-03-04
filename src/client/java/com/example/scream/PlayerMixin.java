package com.example.scream;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import com.example.screammod.client.ScreamMod;

@Mixin(LivingEntity.class)
public abstract class PlayerMixin {

    @Inject(method = "setHealth", at = @At("HEAD"))
    private void onSetHealth(float health, CallbackInfo ci) {
        // We cast 'this' to LivingEntity to access vanilla methods
        LivingEntity entity = (LivingEntity) (Object) this;

        if (entity instanceof PlayerEntity player) {
            // Check if we are on the client side
            if (player.getEntityWorld().isClient()) {
                float threshold = ScreamMod.config.triggerHearts;
                
                // If new health is below threshold and old health was above
                if (health <= threshold && player.getHealth() > threshold) {
                    System.out.println("[ScreamMod] Threshold reached! Playing sound...");
                        entity.playSound(SoundEvents.ENTITY_GHAST_HURT, 2f, 0.7f);               
                     }
            }
        }
    }
} 