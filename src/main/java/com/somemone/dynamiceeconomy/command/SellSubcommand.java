package com.somemone.dynamiceeconomy.command;

import com.somemone.dynamiceeconomy.DynamicEeconomy;
import com.somemone.dynamiceeconomy.db.MarketPositionHandler;
import com.somemone.dynamiceeconomy.model.MarketPosition;
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

public class SellSubcommand extends SubCommand{
    @Override
    void onCommand(CommandSender sender, Command command, String[] args) {
        if (!(sender instanceof Player)) return;
        Player player = (Player) sender;

        if (args.length > 4 || args.length < 2) {
            player.sendMessage(ChatColor.RED + "/de buy <item> (amount)");
            return;
        }

        Material material = Material.getMaterial(args[1]);
        if (material == null) {
            player.sendMessage(ChatColor.RED + "Cannot resolve item name. Use item namespace (\"oak_log\")");
            return;
        }

        int amount = 0;
        if (args.length == 3) {
            amount = Integer.parseInt(args[2]);
        }

        List<MarketPosition> mp = MarketPositionHandler.getAllPositionsFromItem(material);
        if (mp.size() == 0) {
            player.sendMessage(ChatColor.RED + "There isn't a market for this item.");
        }
        LocalDateTime latest = mp.get(0).getDatetime();
        MarketPosition latestPosition = mp.get(0);
        for (MarketPosition pos : mp) {
            if (pos.getDatetime().isAfter(latest)) {
                latest = pos.getDatetime();
                latestPosition = pos;
            }
        }

        List<ItemStack> carryedItems = new ArrayList<>();
        int userCarrying = 0;
        for (ItemStack inv : player.getInventory().getContents()) {
            if (inv.getType().equals(material)) {
                carryedItems.add(inv);
                userCarrying += inv.getAmount();
            }
        }
        if (amount == 0) amount = userCarrying;

        if (userCarrying < amount) {
            player.sendMessage(ChatColor.RED + "You do not have enough items in your inventory to meet the amount");
        }

        // Take item from inventory
        int tempAmount = amount;
        for (ItemStack item : carryedItems) {
            if (tempAmount >= 64) {
                item.setAmount(0);
                tempAmount -= 64;
            } else if (tempAmount == 0) {
                break;
            } else {
                item.setAmount( item.getAmount() - tempAmount );
                tempAmount = 0;
            }
        }

        // Give user money
        float deposit = amount * (latestPosition.getPrice() * DynamicEeconomy.getConfig().getSellMultiplier());
        DynamicEeconomy.getEcon().depositPlayer((OfflinePlayer) player, deposit);

        player.sendMessage(ChatColor.GREEN + "Transaction completed. $" + deposit + " of " + material.name() + " sold.");

    }

    @Override
    String getPermission() {
        return "dynamiceconomy.sell";
    }
}
