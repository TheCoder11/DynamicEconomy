package com.somemone.dynamiceeconomy.config;

import com.somemone.dynamiceeconomy.DynamicEeconomy;
import com.somemone.dynamiceeconomy.economy.ItemStore;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Item;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;

public class StoresConfig {

    private File storesDataFile;

    private FileConfiguration storesConfig;

    private CompletableFuture<FileConfiguration> promise;

    public StoresConfig() {
        storesDataFile = new File(DynamicEeconomy.getInstance().getDataFolder(), "stores.yml");
        Bukkit.getLogger().log(Level.SEVERE, storesDataFile.getPath().toString());
        if (!storesDataFile.exists()) {
            try {
                storesDataFile.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        try {
            promise = CompletableFuture.supplyAsync(() -> {
                return YamlConfiguration.loadConfiguration(storesDataFile);
            });
            storesConfig = promise.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }

    public boolean storeExists (String item) {

        while (!promise.isDone()) {}
        return storesConfig.contains(item);
    }

    public ItemStore getStore (String item) {

        while (!promise.isDone()) {} // Waits until search is finished
        return new ItemStore(storesConfig.getConfigurationSection(item));

    }

    public List<ItemStore> getStores () {
        while (!promise.isDone()) {}

        List<ItemStore> stores = new ArrayList<>();

        @NotNull Map<String, Object> items = storesConfig.getValues(false);
        for (Object store : items.values()) {
            if (store instanceof ConfigurationSection) {
                stores.add( new ItemStore((ConfigurationSection) store));
            }
        }

        return stores;

    }

    public void putStore(String item, ItemStore store) {
        storesConfig.set(item, store.toSection());
    }

    public void saveConfig() {
        try {
            storesConfig.save(storesDataFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
