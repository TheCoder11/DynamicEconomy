package com.somemone.dynamiceeconomy.command.de;

import com.somemone.dynamiceeconomy.command.SubCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class DEHelpSubcommand extends SubCommand {
    @Override
    public void onCommand(CommandSender sender, Command command, String[] args) {
        String returnString = ChatColor.AQUA + "List of subcommands for /de: \n" +
                ChatColor.GOLD + "/de buy <item> <amount> -- Buy amount of item\n" +
                "/de sell <item> <amount> -- Sell amount of item\n";
        if (sender.hasPermission("dynamiceconomy.admin")) {
            returnString += "/de index -- View AH/S for common items\n" +
                    "/de createstore <item> -- Create store for selected item";
        }

        sender.sendMessage(returnString);
    }

    @Override
    public String getPermission() {
        return "dynamiceconomy.help";
    }
}
