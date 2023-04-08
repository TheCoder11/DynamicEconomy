package com.somemone.dynamiceeconomy.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class SubCommand {

    public abstract void onCommand(CommandSender sender, Command command, String[] args);
    public abstract String getPermission();

}
