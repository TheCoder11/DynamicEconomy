package com.somemone.dynamiceeconomy.event;

import com.somemone.dynamiceeconomy.db.model.Transaction;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class TransactionEvent extends Event {

    @Getter
    private static final HandlerList HANDLERS = new HandlerList();

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    @Getter
    private Transaction transaction;

    @Getter
    private UUID player;

    public TransactionEvent(Transaction transaction, UUID player) {
        this.transaction = transaction;
        this.player = player;
    }


}
