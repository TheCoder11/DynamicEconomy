package com.somemone.dynamiceeconomy.chat;

import com.somemone.dynamiceeconomy.config.StoresConfig;
import com.somemone.dynamiceeconomy.economy.ItemStore;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class MarketCreationWizard implements ChatSession {

    private List<String> stimuli;
    private Player player;
    private int place;

    private Material material;

    private float startingPrice;
    private float desiredQuantity;

    private boolean aps;


    public MarketCreationWizard(Player player, Material material) {
        this.player = player;
        this.place = 0;
        this.material = material;

        this.stimuli = Arrays.asList(ChatColor.AQUA + "Enter the desired quantity of products sold:",
                ChatColor.AQUA + "Enter a seed price for the price to start at (this price will be adjusted automatically):",
                ChatColor.AQUA + "Do you want Automatic Price Stabilization (APS) to be performed on this item? (Y/N):");
    }

    @Override
    public void start() {
        player.sendMessage(stimuli.get(0));
    }

    @Override
    public String nextResponse(String input) {
        if (place == 0 || place == 1) {
            // Check if input is float
            try {
                Float.parseFloat(input);
            } catch (NumberFormatException e) {
                return ChatColor.RED + "Answer not accepted, enter a number\n" + stimuli.get(place);
            }
            float number = Float.parseFloat(input);

            if (place == 0) {
                desiredQuantity = number;
            } else if (place == 1) {
                startingPrice = number;
            }
        } else if (place == 2) {
            switch (input) {
                case "Y":
                    aps = true;
                    break;
                case "N":
                    aps = false;
                    break;
                default:
                    return ChatColor.RED + "Enter \"Y\" for yes, enter \"N\" for no\n" + stimuli.get(place);
            }
        }

        place++;
        if (place >= stimuli.size()) {
            whenDone();
            return ChatColor.GREEN + "Store created";
        } else {
            return stimuli.get(place);
        }
    }

    @Override
    public void whenDone() {

        ItemStore store = new ItemStore(material.name(), material.getItemTranslationKey(), desiredQuantity, startingPrice, aps);
        StoresConfig instance = new StoresConfig();
        instance.putStore(material.name(), store);
        instance.saveConfig();

    }

    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public List<String> getStimuli() {
        return stimuli;
    }
}
