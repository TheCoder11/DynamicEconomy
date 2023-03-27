package com.somemone.dynamiceeconomy.command;

import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class DynamicEconomyCommand implements CommandExecutor {

    @Getter
    private Map<String, SubCommand> subcommands = new HashMap<>();

    public DynamicEconomyCommand() {
        subcommands.put("buy", new BuySubcommand());
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (args.length == 0) {
            // maybe version?
            return true;
        }

        for (String alias : subcommands.keySet()) {
            if (alias.equals(args[0])) {
                subcommands.get(alias).onCommand(commandSender, command, args);
                return true;
            }
        }

        commandSender.sendMessage(ChatColor.RED + "Unknown subcommand. Use /de help for a list of sub-commands");
        return true;
    }

    public void registerCommand(String cmd, SubCommand subCommand) {
        subcommands.put(cmd, subCommand);
    }

}
