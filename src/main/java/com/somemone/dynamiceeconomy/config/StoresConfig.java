package com.somemone.dynamiceeconomy.config;

import com.somemone.dynamiceeconomy.DynamicEeconomy;
import com.somemone.dynamiceeconomy.economy.ItemStore;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Item;

import java.io.File;
import java.io.IOException;

public class StoresConfig {

    private File storesDataFile;
    private FileConfiguration storesConfig;

    public StoresConfig() {
        storesDataFile = new File(DynamicEeconomy.getInstance().getDataFolder(), "stores.yml");
        if (!storesDataFile.exists()) {
            try {
                storesDataFile.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        storesConfig = new YamlConfiguration();
        try {
            storesConfig.load(storesDataFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

    }

    public boolean storeExists (String item) {
        return storesConfig.contains(item);
    }

    public ItemStore getStore (String item) {
        return new ItemStore( storesConfig.getConfigurationSection("item") );
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
