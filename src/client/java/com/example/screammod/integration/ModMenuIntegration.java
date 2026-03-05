package com.example.screammod.integration;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.text.Text;
import com.example.screammod.client.ScreamMod;

public class ModMenuIntegration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> {
            // Build the Cloth Config Screen
            ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Text.of("Scream Mod Settings"));

            ConfigCategory general = builder.getOrCreateCategory(Text.of("General"));
            ConfigEntryBuilder entryBuilder = builder.entryBuilder();

            // Add the Heart Threshold Slider/Field
            general.addEntry(entryBuilder.startFloatField(Text.of("Scream at Hearts(1-20)"), ScreamMod.config.triggerHearts)
                .setDefaultValue(7.0f)
                .setMin(0.5f)
                .setMax(20.0f)
                .setTooltip(Text.of("The mod will scream when you fall below this many hearts."))
                .setSaveConsumer(newValue -> ScreamMod.config.triggerHearts = newValue)
                .build());

            // Add the Scream Interval Slider/Field
            general.addEntry(entryBuilder.startLongField(Text.of("Scream Interval (ms)"), ScreamMod.config.screamInterval)
                .setDefaultValue(500L)
                .setMin(100L)
                .setMax(5000L)
                .setTooltip(Text.of("Milliseconds between screams while below the heart threshold."))
                .setSaveConsumer(newValue -> ScreamMod.config.screamInterval = newValue)
                .build());

            // Add the Sound Selector
            general.addEntry(entryBuilder.startStrField(Text.of("Scream Sound"), ScreamMod.config.soundId)
                .setDefaultValue("scream-mod:default")
                .setTooltip(Text.of("Enter Minecraft sound ID (e.g., minecraft:entity.ghast.scream)"))
                .setSaveConsumer(newValue -> ScreamMod.config.soundId = newValue)
                .build());

            // Effect warning settings
            general.addEntry(entryBuilder.startStrField(Text.of("Effect Warning Sound"), ScreamMod.config.effectSoundId)
                .setDefaultValue("scream-mod:default")
                .setTooltip(Text.of("Sound played when fire resistance/speed/strength is about to expire"))
                .setSaveConsumer(newValue -> ScreamMod.config.effectSoundId = newValue)
                .build());

            general.addEntry(entryBuilder.startIntField(Text.of("Effect Warning Threshold (s)"), ScreamMod.config.effectThresholdSeconds)
                .setDefaultValue(5)
                .setMin(1)
                .setMax(60)
                .setTooltip(Text.of("Number of seconds remaining on the potion effect when the warning should start"))
                .setSaveConsumer(newValue -> ScreamMod.config.effectThresholdSeconds = newValue)
                .build());

            general.addEntry(entryBuilder.startLongField(Text.of("Effect Warning Interval (ms)"), ScreamMod.config.effectInterval)
                .setDefaultValue(500L)
                .setMin(100L)
                .setMax(5000L)
                .setTooltip(Text.of("Milliseconds between repeated warning sounds while effect is still near expiry"))
                .setSaveConsumer(newValue -> ScreamMod.config.effectInterval = newValue)
                .build());

            // Save the file when the user clicks "Save and Quit"
            builder.setSavingRunnable(() -> {
                ScreamMod.config.save();
            });

            return builder.build();
        };
    }
}
