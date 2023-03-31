package com.somemone.dynamiceeconomy.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Material;

import java.util.List;

@AllArgsConstructor
public class PluginConfig {

    @Getter
    private List<Material> materialsToIndex;

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
