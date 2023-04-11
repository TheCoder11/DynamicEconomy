package com.somemone.dynamiceeconomy.command.dedebug;

import com.somemone.dynamiceeconomy.DynamicEeconomy;
import com.somemone.dynamiceeconomy.command.SubCommand;
import com.somemone.dynamiceeconomy.config.StoresConfig;
import com.somemone.dynamiceeconomy.db.MarketPositionHandler;
import com.somemone.dynamiceeconomy.db.SessionHandler;
import com.somemone.dynamiceeconomy.db.TransactionHandler;
import com.somemone.dynamiceeconomy.db.model.MarketPosition;
import com.somemone.dynamiceeconomy.db.model.Transaction;
import com.somemone.dynamiceeconomy.economy.ItemStore;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class StatsSubcommand extends SubCommand {
    @Override
    public void onCommand(CommandSender sender, Command command, String[] args) {

        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "/dedebug stats <item> [mp]");
            return;
        }

        Material material = Material.getMaterial(args[1]);
        StoresConfig instance = new StoresConfig();
        if (material == null || !instance.storeExists(material.name())) {
            sender.sendMessage(ChatColor.RED + "Item has no open market");
            return;
        }

        ItemStore store = instance.getStore(material.name());

        List<MarketPosition> pastPositions = MarketPositionHandler.getAllPositionsFromItem(material);
        int salesLastDay = 0;

        List<Transaction> pastSales = TransactionHandler.getTransactionsWithItem(material);

        LocalDateTime timeDesired = LocalDateTime.now();
        for (MarketPosition pos : pastPositions)
            if (pos.getStarttime().isBefore(timeDesired))
                timeDesired = pos.getStarttime();


        for (Transaction sale : pastSales) {
            ItemStore.APSType type = sale.getType();

            if (sale.getDatetime().isAfter(timeDesired) && !sale.getSeller().isPrivate() && store.getApsTrackingType().equals(type)) {
                salesLastDay++;
            } else {
                break;
            }
        }

        Duration dur = Duration.between(timeDesired, LocalDateTime.now());
        if (dur.isZero()) dur = ChronoUnit.FOREVER.getDuration();

        float activeHours = SessionHandler.getHoursInDays(dur);
        float salesPerActiveHour = activeHours / salesLastDay;

        sender.sendMessage(DynamicEeconomy.getPrefix() + ChatColor.AQUA + "The market " + args[1] + "\n" +
                " - Active Session Hours since last APS: " + activeHours + "\n" +
                " - " + store.getApsTrackingType().name().toLowerCase() + "s since last APS: " + salesLastDay + "\n" +
                " - " + "Current SAH: " + salesPerActiveHour);

        if (pastPositions.size() > 10) {
            for (int i = 0; i < 10; i++) {
                MarketPosition pos = pastPositions.get(i);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                sender.sendMessage(ChatColor.AQUA + "Price: " + pos.getPrice() + ", SAH: " + pos.getSah() + ", Date/Time: " + formatter.format(pos.getStarttime()));
            }
        } else {
            for (MarketPosition pos : pastPositions) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                sender.sendMessage(ChatColor.AQUA + "Price: " + pos.getPrice() + ", SAH: " + pos.getSah() + ", Date/Time: " + formatter.format(pos.getStarttime()));
            }
        }

    }

    @Override
    public String getPermission() {
        return null;
    }
}
