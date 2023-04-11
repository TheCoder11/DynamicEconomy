package com.somemone.dynamiceeconomy;

import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.somemone.dynamiceeconomy.chat.ChatSessionManager;
import com.somemone.dynamiceeconomy.command.DebugCommand;
import com.somemone.dynamiceeconomy.command.DynamicEconomyCommand;
import com.somemone.dynamiceeconomy.config.APIPresence;
import com.somemone.dynamiceeconomy.config.PluginConfig;
import com.somemone.dynamiceeconomy.db.MarketPositionHandler;
import com.somemone.dynamiceeconomy.db.SessionHandler;
import com.somemone.dynamiceeconomy.db.TransactionHandler;
import com.somemone.dynamiceeconomy.db.model.Transaction;
import com.somemone.dynamiceeconomy.economy.APSScheduler;
import com.somemone.dynamiceeconomy.listener.ChatListener;
import com.somemone.dynamiceeconomy.listener.JoinListener;
import com.somemone.dynamiceeconomy.listener.MineListener;
import com.somemone.dynamiceeconomy.listener.TransactionListener;
import com.somemone.dynamiceeconomy.listener.api.ChestShopListener;
import com.somemone.dynamiceeconomy.listener.api.QuickShopListener;
import com.somemone.dynamiceeconomy.listener.api.ShopChestListener;
import com.somemone.dynamiceeconomy.listener.api.SignShopListener;
import com.somemone.dynamiceeconomy.db.model.Session;
import lombok.Getter;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.UUID;

public final class DynamicEeconomy extends JavaPlugin {

    @Getter
    private static ConnectionSource connectionSource;

    @Getter
    private static HashMap<UUID, Session> activeSessions;

    @Getter
    private static ChatSessionManager chatSessionManager;

    @Getter
    private static PluginConfig pluginConfig;

    @Getter
    private static APIPresence apiPresence;

    @Getter
    private static Economy econ = null;

    @Getter
    private static Permission perms = null;

    @Getter
    private static Chat chat = null;

    @Getter
    private static JavaPlugin instance;

    @Override
    public void onEnable() {
        try {
            connectionSource = new JdbcPooledConnectionSource("jdbc:mysql://localhost:3306/server?user=root&password=johnil89");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        this.saveDefaultConfig();

        activeSessions = new HashMap<>();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        if (getConfig().getString("nextAPS").equals("aa")) {
            getConfig().set("nextAPS", formatter.format( LocalDateTime.now().plusDays(1L) )); // Sets config to one day later to initialized.
            saveConfig();
        }

        pluginConfig = new PluginConfig(getConfig().getStringList("materialsToIndex"),
                getConfig().getInt("correctionPercent"),
                (float) getConfig().getDouble( "sellMultiplier"),
                (float) getConfig().getInt("APSDays"),
                LocalDateTime.parse( getConfig().getString("nextAPS"), formatter ),
                (float) getConfig().getDouble("establishPercent"));

        loadAPIs();

        if (!setupEconomy() ) {
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        instance = this;

        chatSessionManager = new ChatSessionManager();

        Bukkit.getPluginManager().registerEvents(new ChatListener(), this);
        Bukkit.getPluginManager().registerEvents(new JoinListener(), this);
        Bukkit.getPluginManager().registerEvents(new MineListener(), this);

        this.getCommand("dynamiceconomy").setExecutor(new DynamicEconomyCommand());
        this.getCommand("dedebug").setExecutor(new DebugCommand());

        TransactionHandler.initializeTables();

        for (Player player : Bukkit.getOnlinePlayers()) {
            Session session = new Session(player, LocalDateTime.now());
            activeSessions.put(player.getUniqueId(), session);
        }

        new APSScheduler(this).runTaskTimer(this, 1200L, 1200L);


    }

    @Override
    public void onDisable() {

        for (Session session : activeSessions.values()) {
            session.setEndTime(Timestamp.valueOf(LocalDateTime.now()));
            SessionHandler.writeSession(session);
        }

        try {
            connectionSource.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    public void loadAPIs() {
        Plugin quickShop = Bukkit.getPluginManager().getPlugin("QuickShop");
        Plugin chestShop = Bukkit.getPluginManager().getPlugin("ChestShop");
        Plugin shopChest = Bukkit.getPluginManager().getPlugin("ShopChest");
        Plugin signShop = Bukkit.getPluginManager().getPlugin("SignShop");

        apiPresence = new APIPresence(quickShop != null, chestShop != null,
                shopChest != null, signShop != null);

        if (apiPresence.isQuickShop()) {
            Bukkit.getPluginManager().registerEvents(new QuickShopListener(), this);
        }
        if (apiPresence.isChestShop()) {
            Bukkit.getPluginManager().registerEvents(new ChestShopListener(), this);
        }
        if (apiPresence.isShopChest()) {
            Bukkit.getPluginManager().registerEvents(new ShopChestListener(), this);
        }
        if (apiPresence.isSignShop()) {
            Bukkit.getPluginManager().registerEvents(new SignShopListener(), this);
        }
    }

    public static String getPrefix() {
        return ChatColor.GOLD + "[" + ChatColor.YELLOW + "DynamicEconomy" + ChatColor.GOLD + "] " + ChatColor.RESET;
    }
}
