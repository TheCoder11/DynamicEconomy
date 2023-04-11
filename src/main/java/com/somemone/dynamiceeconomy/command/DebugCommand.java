package com.somemone.dynamiceeconomy.command;

import com.somemone.dynamiceeconomy.command.dedebug.*;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class DebugCommand implements CommandExecutor {

    private Map<String, SubCommand> subcommands = new HashMap<>();

    public DebugCommand() {
        registerCommand("addtransaction", new AddTransactionSubcommand());
        registerCommand("addsession", new AddSessionSubcommand());
        registerCommand("help", new DEDebugHelpSubcommand());
        registerCommand("runaps", new RunAPSSubcommand());
        registerCommand("stats", new StatsSubcommand());
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
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

        commandSender.sendMessage(ChatColor.RED + "Unknown subcommand. Use /dedebug help for a list of sub-commands");
        return true;
    }

    public void registerCommand (String name, SubCommand subcommand) {
        subcommands.put(name, subcommand);
    }
}
