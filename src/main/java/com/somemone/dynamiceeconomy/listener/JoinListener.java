package com.somemone.dynamiceeconomy.listener;

import com.somemone.dynamiceeconomy.DynamicEeconomy;
import com.somemone.dynamiceeconomy.db.SessionHandler;
import com.somemone.dynamiceeconomy.db.model.Session;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class JoinListener implements Listener {

    @EventHandler
    public void handle(PlayerJoinEvent event) {
        Session session = new Session(event.getPlayer(), LocalDateTime.now());
        DynamicEeconomy.getActiveSessions().put(event.getPlayer().getUniqueId(), session);
    }

    @EventHandler
    public void handle(PlayerQuitEvent event) {
        Session session = DynamicEeconomy.getActiveSessions().get(event.getPlayer().getUniqueId());
        session.setEndTime(Timestamp.valueOf( LocalDateTime.now() ));
        if (session != null) DynamicEeconomy.getActiveSessions().remove(event.getPlayer().getUniqueId());

        SessionHandler.writeSession(session);
    }

}
