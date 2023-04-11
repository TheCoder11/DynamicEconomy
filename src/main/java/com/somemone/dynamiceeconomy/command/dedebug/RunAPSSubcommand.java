package com.somemone.dynamiceeconomy.command.dedebug;

import com.somemone.dynamiceeconomy.DynamicEeconomy;
import com.somemone.dynamiceeconomy.command.SubCommand;
import com.somemone.dynamiceeconomy.config.StoresConfig;
import com.somemone.dynamiceeconomy.economy.AutomaticPriceStabilization;
import com.somemone.dynamiceeconomy.economy.ItemStore;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class RunAPSSubcommand extends SubCommand {
    @Override
    public void onCommand(CommandSender sender, Command command, String[] args) {

        StoresConfig ins = new StoresConfig();
        List<ItemStore> storesToAPS = new ArrayList<>();
        if (args.length == 2) {
            if (!ins.storeExists(args[1])) {
                sender.sendMessage(ChatColor.RED + "This item has no active store.");
                return;
            }
            storesToAPS.add(ins.getStore(args[1]));
        } else if (args.length == 1) {
            storesToAPS = ins.getStores();
        }


        int numberAPS = 0;
        int numberSkipped = 0;

        for (ItemStore store : storesToAPS) {
            AutomaticPriceStabilization aps = new AutomaticPriceStabilization(store.getName());
            aps.findBestPrice();
            aps.commitToDatabase();

            if (aps.isAps()) numberAPS++;
        }

        if (storesToAPS.size() == 0) {
            sender.sendMessage(DynamicEeconomy.getPrefix() + ChatColor.GREEN + "No APS preformed, no stores are open");
        } else if (numberAPS == 0) {
            sender.sendMessage(DynamicEeconomy.getPrefix() + ChatColor.GREEN + "No APS preformed because no stores are configured for APS");
        } else {
            sender.sendMessage(DynamicEeconomy.getPrefix() + ChatColor.GREEN + numberAPS + " APS Performed, " + numberSkipped + " skipped due to store settings");
        }

    }

    @Override
    public String getPermission() {
        return "dynamiceconomy.debug";
    }
}
