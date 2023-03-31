package com.somemone.dynamiceeconomy.chat;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class MarketCreationWizard implements ChatSession {

    private List<String> stimuli;
    private Player player;
    private int place;

    private float startingPrice;
    private float desiredQuantity;


    public MarketCreationWizard(Player player, Material material) {
        this.player = player;
        this.place = 0;

        this.stimuli = Arrays.asList(new String[]{
                "Enter the desired quantity of products sold:",
                "Enter a seed price for the price to start at (this price will be adjusted automatically):",
                });
    }

    @Override
    public String nextResponse(String input) {
        return null;
    }

    @Override
    public void whenDone() {

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
