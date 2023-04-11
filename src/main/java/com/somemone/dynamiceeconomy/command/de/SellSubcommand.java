package com.somemone.dynamiceeconomy.command.de;

import com.somemone.dynamiceeconomy.DynamicEeconomy;
import com.somemone.dynamiceeconomy.command.SubCommand;
import com.somemone.dynamiceeconomy.config.StoresConfig;
import com.somemone.dynamiceeconomy.db.TransactionHandler;
import com.somemone.dynamiceeconomy.db.model.Seller;
import com.somemone.dynamiceeconomy.db.model.Transaction;
import com.somemone.dynamiceeconomy.economy.ItemStore;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SellSubcommand extends SubCommand {
    @Override
    public void onCommand(CommandSender sender, Command command, String[] args) {
        if (!(sender instanceof Player)) return;
        Player player = (Player) sender;

        if (args.length > 4 || args.length < 2) {
            player.sendMessage(ChatColor.RED + "/de buy <item> (amount)");
            return;
        }

        Material material = Material.getMaterial(args[1].toUpperCase());
        if (material == null) {
            player.sendMessage(ChatColor.RED + "Cannot resolve item name. Use item namespace (\"oak_log\")");
            return;
        }

        int amount = 0;
        if (args.length == 3) {
            amount = Integer.parseInt(args[2]);
        }

        StoresConfig instance = new StoresConfig();
        if (!instance.storeExists(material.name())) {
            player.sendMessage(ChatColor.RED + "There isn't a market for this item.");
            return;
        }

        ItemStore store = instance.getStore(material.name());

        List<ItemStack> carryedItems = new ArrayList<>();
        int userCarrying = 0;
        for (ItemStack inv : player.getInventory().getContents()) {
            if (inv != null && inv.getType().equals(material)) {
                carryedItems.add(inv);
                userCarrying += inv.getAmount();
            }
        }
        if (amount == 0) amount = userCarrying;

        if (userCarrying < amount) {
            player.sendMessage(ChatColor.RED + "You do not have enough items in your inventory to meet the amount");
            return;
        }

        // Take item from inventory
        int tempAmount = amount;
        for (ItemStack item : carryedItems) {
            if (tempAmount > item.getAmount()) {
                tempAmount -= item.getAmount();
                item.setAmount(0);
            } else {
                item.setAmount(item.getAmount() - tempAmount);
                break;
            }
        }

        // Give user money
        float deposit = amount * (store.getPrice() * DynamicEeconomy.getPluginConfig().getSellMultiplier());
        DynamicEeconomy.getEcon().depositPlayer((OfflinePlayer) player, deposit);

        Transaction transaction = new Transaction(material.name(), amount, deposit, ItemStore.APSType.SELL, Seller.getServerSeller(), LocalDateTime.now());
        TransactionHandler.writeTransaction(transaction);

        player.sendMessage(ChatColor.GREEN + "Transaction completed. $" + deposit + " of " + material.name() + " sold.");

    }

    @Override
    public String getPermission() {
        return "dynamiceconomy.sell";
    }
}
