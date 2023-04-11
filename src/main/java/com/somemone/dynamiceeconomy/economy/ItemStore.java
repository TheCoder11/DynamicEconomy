package com.somemone.dynamiceeconomy.economy;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
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
    private APSType apsTrackingType;

    @Getter
    private boolean enabled;

    @Getter
    private boolean aps;

    public ItemStore(String name, float desiredQuantity, float price, boolean aps, String trackingType) {
        this.name = name;
        this.displayName = Material.getMaterial(name).getItemTranslationKey();
        this.desiredQuantity = desiredQuantity;
        this.price = price;
        this.enabled = true;
        this.aps = aps;
    }

    public ItemStore(String name, String displayName, float desiredQuantity, float price, boolean aps, APSType trackingType) {
        this.name = name;
        this.displayName = name;
        this.desiredQuantity = desiredQuantity;
        this.price = price;
        this.enabled = true;
        this.aps = aps;
        this.apsTrackingType = trackingType;
    }

    public ItemStore(ConfigurationSection section) {
        name = section.getString("name");
        displayName = section.getString("display_name");
        desiredQuantity = section.getLong("desired_quantity");
        price = section.getLong("price");
        enabled = section.getBoolean("enabled");
        aps = section.getBoolean("aps");
        apsTrackingType = APSType.valueOf(section.getString("tracking_type"));
    }

    public ConfigurationSection toSection() {
        ConfigurationSection section = new YamlConfiguration();
        section.set("name", name);
        section.set("display_name", displayName);
        section.set("desired_quantity", desiredQuantity);
        section.set("price", (double) price);
        section.set("enabled", enabled);
        section.set("aps", aps);
        section.set("tracking_type", apsTrackingType.name());

        return section;
    }

    public enum APSType {
        BUY,
        SELL;
    }


}
