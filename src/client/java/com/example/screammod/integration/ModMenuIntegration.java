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
            general.addEntry(entryBuilder.startFloatField(Text.of("Scream at Hearts"), ScreamMod.config.triggerHearts)
                .setDefaultValue(7.0f)
                .setMin(0.5f)
                .setMax(20.0f)
                .setTooltip(Text.of("The mod will scream when you fall below this many hearts."))
                .setSaveConsumer(newValue -> ScreamMod.config.triggerHearts = newValue)
                .build());

            // Save the file when the user clicks "Save and Quit"
            builder.setSavingRunnable(() -> {
                ScreamMod.config.save();
            });

            return builder.build();
        };
    }
}
