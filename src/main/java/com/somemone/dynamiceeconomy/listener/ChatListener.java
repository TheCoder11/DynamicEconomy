package com.somemone.dynamiceeconomy.listener;

import com.somemone.dynamiceeconomy.DynamicEeconomy;
import com.somemone.dynamiceeconomy.chat.ChatSession;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {

    @EventHandler
    public void handle (AsyncPlayerChatEvent event) {

        if (!DynamicEeconomy.getChatSessionManager().getSessions().containsKey(event.getPlayer())) return;

        ChatSession session = DynamicEeconomy.getChatSessionManager().getSessions().get(event.getPlayer());
        String response = session.nextResponse(event.getMessage());

        event.getPlayer().sendMessage(DynamicEeconomy.getPrefix() + response);
        event.setCancelled(true);

    }

}
