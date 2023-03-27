package com.somemone.dynamiceeconomy.listener.api;

import com.somemone.dynamiceeconomy.db.SellerHandler;
import com.somemone.dynamiceeconomy.db.TransactionHandler;
import com.somemone.dynamiceeconomy.model.Seller;
import com.somemone.dynamiceeconomy.model.Transaction;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.wargamer2010.signshop.events.SSMoneyTransactionEvent;

import java.time.LocalDateTime;

public class SignShopListener implements Listener {

    @EventHandler
    public void handle(SSMoneyTransactionEvent event) {
        Seller seller = SellerHandler.findSeller(event.getShop().getOwner().getPlayer().getUniqueId().toString());
        if (seller == null) {
            seller = new Seller(event.getShop().getOwner().getPlayer().getUniqueId().toString(), false, true);
        }
        if (seller.isBanned()) return;

        Transaction transaction = new Transaction(
                event.getItems()[0].getType().name(),
                1,
                (float) event.getPrice(),
                seller,
                LocalDateTime.now()
        );

        TransactionHandler.writeTransaction(transaction);
    }

}
