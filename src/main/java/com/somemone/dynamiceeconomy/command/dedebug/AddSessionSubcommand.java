package com.somemone.dynamiceeconomy.command.dedebug;

import com.somemone.dynamiceeconomy.DynamicEeconomy;
import com.somemone.dynamiceeconomy.command.SubCommand;
import com.somemone.dynamiceeconomy.db.SessionHandler;
import com.somemone.dynamiceeconomy.db.model.Session;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.time.LocalDateTime;
import java.util.UUID;

public class AddSessionSubcommand extends SubCommand {
    @Override
    public void onCommand(CommandSender sender, Command command, String[] args) { // /dedebug addsession <time>

        if (args.length != 1) {
            sender.sendMessage(DynamicEeconomy.getPrefix() + ChatColor.RED + "/dedebug addsession <time>");
            return;
        }

        long hours = 0;
        try {
            hours = Long.parseLong(args[0]);
        } catch (NumberFormatException e) {
            sender.sendMessage(DynamicEeconomy.getPrefix() + ChatColor.RED + "/dedebug addsession <time>");
            return;
        } finally {
            Session session = new Session( UUID.fromString("6c8f7913-3eb7-4d51-9597-54f8b4e1735a"), LocalDateTime.now());
            session.setEndTime(LocalDateTime.now().plusHours(hours));
            SessionHandler.writeSession(session);
        }

    }

    @Override
    public String getPermission() {
        return "dynamiceconomy.debug";
    }
}
