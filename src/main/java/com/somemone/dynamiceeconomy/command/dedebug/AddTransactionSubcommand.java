package com.somemone.dynamiceeconomy.command.dedebug;

import com.somemone.dynamiceeconomy.DynamicEeconomy;
import com.somemone.dynamiceeconomy.command.SubCommand;
import com.somemone.dynamiceeconomy.db.MarketPositionHandler;
import com.somemone.dynamiceeconomy.db.TransactionHandler;
import com.somemone.dynamiceeconomy.db.model.Seller;
import com.somemone.dynamiceeconomy.db.model.Transaction;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.time.LocalDateTime;

public class AddTransactionSubcommand extends SubCommand {

    @Override
    public void onCommand(CommandSender sender, Command command, String[] args) {

        if (args.length < 2) {
            sender.sendMessage(DynamicEeconomy.getPrefix() + ChatColor.RED + "/dedebug addtransaction <item> <amount> [price]");
            return;
        }

        try {
            Material.getMaterial(args[0]);
            Integer.parseInt(args[1]);
            if (args.length == 3) Float.parseFloat(args[2]);
        } catch (Exception e) {
            sender.sendMessage(DynamicEeconomy.getPrefix() + ChatColor.RED + "/dedebug addtransaction <item> <amount> [price]");
            return;
        }

        Material material = Material.getMaterial(args[0]);
        int amount = Integer.parseInt(args[1]);
        float price;

        if (MarketPositionHandler.getAllPositionsFromItem(material).size() == 0) {
            sender.sendMessage(DynamicEeconomy.getPrefix() + ChatColor.RED + "Chosen item has no open market");
            return;
        }

        if (args.length == 3) {
            price = Float.parseFloat(args[2]);
        } else {
            price = MarketPositionHandler.getExactPosition(LocalDateTime.now(), material).getPrice();
        }

        Transaction transaction = new Transaction(material.name(), amount, price, "buy", Seller.getServerSeller(), LocalDateTime.now());
        TransactionHandler.writeTransaction(transaction);

    }

    @Override
    public String getPermission() {
        return "dynamiceconomy.debug";
    }
}
