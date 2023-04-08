package com.somemone.dynamiceeconomy.chat;

import org.bukkit.entity.Player;

import java.util.List;

public interface ChatSession {

    public String nextResponse(String input);

    public void start();

    public void whenDone();

    public Player getPlayer();

    public List<String> getStimuli();

}
