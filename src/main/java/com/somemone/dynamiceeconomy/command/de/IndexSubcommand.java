package com.somemone.dynamiceeconomy.command.de;

import com.somemone.dynamiceeconomy.DynamicEeconomy;
import com.somemone.dynamiceeconomy.command.SubCommand;
import com.somemone.dynamiceeconomy.economy.StapleIndex;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class IndexSubcommand extends SubCommand {
    @Override
    public void onCommand(CommandSender sender, Command command, String[] args) {

        long days = 3l;
        if (args.length == 2) {
            try {
                days = Long.parseLong(args[1]);
            } catch (Exception e) {
                sender.sendMessage(ChatColor.RED + "/de indexes <days>");
                return;
            }
        }


        Map<String, Float> stapleValues = new HashMap<>();
        for (String material : DynamicEeconomy.getPluginConfig().getMaterialsToIndex()) {
            StapleIndex index = new StapleIndex(material, Duration.ofDays(days));
            stapleValues.put(material, index.getStapleIndexSAH());
        }

        String message = ChatColor.GREEN + "Active Hours per item obtained in the past " + days + " days: \n";
        for (Map.Entry<String, Float> entry : stapleValues.entrySet()) {
            message += ChatColor.GOLD + entry.getKey() + ": " + ChatColor.AQUA + entry.getValue() + "\n";
        }

        sender.sendMessage(message);

    }

    @Override
    public String getPermission() {
        return "dynamiceconomy.admin.indexes";
    }
}
