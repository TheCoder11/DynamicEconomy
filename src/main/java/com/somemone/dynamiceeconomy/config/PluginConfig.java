package com.somemone.dynamiceeconomy.config;

import com.somemone.dynamiceeconomy.DynamicEeconomy;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.List;

@AllArgsConstructor
public class PluginConfig {

    @Getter
    private List<String> materialsToIndex;

    /**
     * When making a automatic correction without previous results, APS will raise or lower the price by this percentage.
     */
    @Getter
    private int correctionPercent;

    /**
     * Multiplier when selling goods. A value of 0.8 would mean that values have 20% less selling price than buying price.
     */
    @Getter
    private float sellMultiplier;


    @Getter
    private float APSDays;




}
