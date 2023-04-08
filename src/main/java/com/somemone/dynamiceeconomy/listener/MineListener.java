package com.somemone.dynamiceeconomy.listener;

import com.somemone.dynamiceeconomy.DynamicEeconomy;
import com.somemone.dynamiceeconomy.db.ObtainHandler;
import com.somemone.dynamiceeconomy.db.model.ObtainMaterial;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class MineListener implements Listener {

    @EventHandler
    public void handle(BlockBreakEvent event) {

        Material material = event.getBlock().getBlockData().getMaterial();
        if (DynamicEeconomy.getPluginConfig().getMaterialsToIndex().contains(material)) {
            ObtainHandler.writeObtainal(new ObtainMaterial(material, event.getPlayer().getUniqueId()));
        }
    }

}
