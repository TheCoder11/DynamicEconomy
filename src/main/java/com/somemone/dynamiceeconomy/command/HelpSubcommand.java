package com.somemone.dynamiceeconomy.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class HelpSubcommand extends SubCommand{
    @Override
    void onCommand(CommandSender sender, Command command, String[] args) {

    }

    @Override
    String getPermission() {
        return null;
    }
}
