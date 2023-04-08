package com.somemone.dynamiceeconomy.command.de;

import com.somemone.dynamiceeconomy.DynamicEeconomy;
import com.somemone.dynamiceeconomy.chat.MarketCreationWizard;
import com.somemone.dynamiceeconomy.command.SubCommand;
import com.somemone.dynamiceeconomy.config.StoresConfig;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CreateStoreSubcommand extends SubCommand {
    @Override
    public void onCommand(CommandSender sender, Command command, String[] args) {
        if (!(sender instanceof Player)) return;
        Player player = (Player) sender;

        if (args.length != 1) {
            player.sendMessage(DynamicEeconomy.getPrefix() + ChatColor.RED + "/de createstore <item>");
            return;
        }
        Material material = Material.getMaterial(args[0]);
        if (material == null) {
            player.sendMessage(DynamicEeconomy.getPrefix() + ChatColor.RED + "/de createstore <item>");
            return;
        }

        MarketCreationWizard wizard = new MarketCreationWizard(player, material);
        DynamicEeconomy.getChatSessionManager().getSessions().put(player, wizard);
        wizard.start();
    }

    @Override
    public String getPermission() {
        return "dynamiceconomy.admin.create";
    }
}
