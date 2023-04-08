package com.somemone.dynamiceeconomy.economy;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

public class ItemStore {

    @Getter
    private String name;

    @Getter
    private String displayName;

    @Getter
    @Setter
    private float price;

    @Getter
    @Setter
    private float desiredQuantity;

    @Getter
    private boolean enabled;

    @Getter
    private boolean aps;

    public ItemStore(String name, float desiredQuantity, float price, boolean aps) {
        this.name = name;
        this.displayName = name;
        this.desiredQuantity = desiredQuantity;
        this.price = price;
        this.enabled = true;
        this.aps = aps;
    }

    public ItemStore(String name, String displayName, float desiredQuantity, float price, boolean aps) {
        this.name = name;
        this.displayName = name;
        this.desiredQuantity = desiredQuantity;
        this.price = price;
        this.enabled = true;
        this.aps = aps;
    }

    public ItemStore(ConfigurationSection section) {
        name = section.getString("name");
        displayName = section.getString("display_name");
        desiredQuantity = section.getLong("desired_quantity");
        price = section.getLong("price");
        enabled = section.getBoolean("enabled");
        aps = section.getBoolean("aps");
    }

    public ConfigurationSection toSection() {
        ConfigurationSection section = new YamlConfiguration();
        section.set("name", name);
        section.set("display_name", displayName);
        section.set("desired_quantity", desiredQuantity);
        section.get("price", price);
        section.get("enabled", enabled);
        section.get("aps", aps);

        return section;
    }


}
