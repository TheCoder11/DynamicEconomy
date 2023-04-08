package com.somemone.dynamiceeconomy.chat;

import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class ChatSessionManager {

    @Getter
    private Map<Player, ChatSession> sessions;

    public ChatSessionManager () {
        sessions = new HashMap<>();
    }

}
