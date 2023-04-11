package com.somemone.dynamiceeconomy.economy;

import com.somemone.dynamiceeconomy.DynamicEeconomy;
import com.somemone.dynamiceeconomy.config.StoresConfig;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.time.LocalDateTime;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.logging.Level;

public class APSScheduler extends BukkitRunnable {

    private final JavaPlugin plugin;

    public APSScheduler(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        LocalDateTime datetime = DynamicEeconomy.getPluginConfig().getNextAPS();
        if (Math.abs( ChronoUnit.MINUTES.between(datetime, LocalDateTime.now()) ) > 1) return;

        StoresConfig ins = new StoresConfig();
        List<ItemStore> storesToAPS = ins.getStores();

        for (ItemStore store : storesToAPS) {
            AutomaticPriceStabilization aps = new AutomaticPriceStabilization(store.getName());
            aps.findBestPrice();
            aps.commitToDatabase();
        }

        LocalDateTime next = LocalDateTime.now().plusDays(1L);
        DynamicEeconomy.getPluginConfig().setNextAPS(next);
        DynamicEeconomy.getPluginConfig().updateConfig();
    }
}
