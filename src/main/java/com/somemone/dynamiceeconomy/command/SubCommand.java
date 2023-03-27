package com.somemone.dynamiceeconomy.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class SubCommand {

    abstract void onCommand(CommandSender sender, Command command, String[] args);
    abstract String getPermission();

}
