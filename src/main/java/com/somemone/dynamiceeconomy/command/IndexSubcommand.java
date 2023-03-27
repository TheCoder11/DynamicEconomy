package com.somemone.dynamiceeconomy.command;

import com.somemone.dynamiceeconomy.DynamicEeconomy;
import com.somemone.dynamiceeconomy.economy.StapleIndex;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.awt.*;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IndexSubcommand extends SubCommand{
    @Override
    void onCommand(CommandSender sender, Command command, String[] args) {

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
        for (Material material : DynamicEeconomy.getConfig().getMaterialsToIndex()) {
            StapleIndex index = new StapleIndex(material, Duration.ofDays(days));
            stapleValues.put(material.name(), index.getStapleIndexAHS());
        }

        String message = ChatColor.GREEN + "Active Hours per item obtained in the past " + days + " days: \n";
        for (Map.Entry<String, Float> entry : stapleValues.entrySet()) {
            message += ChatColor.GOLD + entry.getKey() + ": " + ChatColor.AQUA + entry.getValue() + "\n";
        }

        sender.sendMessage(message);

    }

    @Override
    String getPermission() {
        return "dynamiceconomy.indexes";
    }
}
