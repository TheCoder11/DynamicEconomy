package com.somemone.dynamiceeconomy.command.dedebug;

import com.somemone.dynamiceeconomy.command.SubCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class DEDebugHelpSubcommand extends SubCommand {
    @Override
    public void onCommand(CommandSender sender, Command command, String[] args) {
        sender.sendMessage(ChatColor.AQUA + "List of subcommands for /dedebug: \n" +
                ChatColor.GOLD + "/dedebug addtransaction <item> <amount> [price] -- Adds transaction(s) to register\n" +
                "/dedebug addsession <hours> -- Adds session of gameplay in hours");
    }

    @Override
    public String getPermission() {
        return "dynamiceconomy.debug";
    }
}
