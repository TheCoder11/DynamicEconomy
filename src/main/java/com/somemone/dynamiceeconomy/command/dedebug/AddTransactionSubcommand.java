package com.somemone.dynamiceeconomy.command.dedebug;

import com.somemone.dynamiceeconomy.DynamicEeconomy;
import com.somemone.dynamiceeconomy.command.SubCommand;
import com.somemone.dynamiceeconomy.config.StoresConfig;
import com.somemone.dynamiceeconomy.db.MarketPositionHandler;
import com.somemone.dynamiceeconomy.db.TransactionHandler;
import com.somemone.dynamiceeconomy.db.model.Seller;
import com.somemone.dynamiceeconomy.db.model.Transaction;
import com.somemone.dynamiceeconomy.economy.ItemStore;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.time.LocalDateTime;

public class AddTransactionSubcommand extends SubCommand {

    @Override
    public void onCommand(CommandSender sender, Command command, String[] args) {

        if (args.length < 4) {
            sender.sendMessage(DynamicEeconomy.getPrefix() + ChatColor.RED + "/dedebug addtransaction <item> <amount> <type> [price]");
            return;
        }

        try {
            Material.getMaterial(args[1]);
            Integer.parseInt(args[2]);
            ItemStore.APSType.valueOf(args[3].toUpperCase());
            if (args.length == 5) Float.parseFloat(args[4]);
        } catch (Exception e) {
            sender.sendMessage(DynamicEeconomy.getPrefix() + ChatColor.RED + "/dedebug addtransaction <item> <amount> <type> [price]");
            return;
        }

        Material material = Material.getMaterial(args[1]);
        int amount = Integer.parseInt(args[2]);
        ItemStore.APSType type = ItemStore.APSType.valueOf(args[3].toUpperCase());
        float price;

        StoresConfig ins = new StoresConfig();
        if (!ins.storeExists(material.name())) {

            sender.sendMessage(DynamicEeconomy.getPrefix() + ChatColor.RED + "Chosen item has no open market");
            return;
        }

        if (args.length == 5) {
            price = Float.parseFloat(args[4]);
        } else {
            sender.sendMessage(material.name());
            price = ins.getStore(material.name()).getPrice();
        }

        Transaction transaction = new Transaction(material.name(), amount, price, type, Seller.getServerSeller(), LocalDateTime.now());
        TransactionHandler.writeTransaction(transaction);

    }

    @Override
    public String getPermission() {
        return "dynamiceconomy.debug";
    }
}
