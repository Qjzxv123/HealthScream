package com.example.scream;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class PlayerMixin {
    @Inject(method = "setHealth", at = @At("HEAD"))
    private void onSetHealth(float health, CallbackInfo ci) {
        // Check if this entity is a player
        if ((Object) this instanceof PlayerEntity player) {
            // Only run on the client side to play sound
            if (player.getWorld().isClient) {
                float threshold = ScreamMod.config.triggerHearts;
                if (health <= threshold && player.getHealth() > threshold) {
                    // This is where you call your scream logic!
                    System.out.println("LOW HEALTH DETECTED: Playing Scream!");
                }
            }
        }
    }
}