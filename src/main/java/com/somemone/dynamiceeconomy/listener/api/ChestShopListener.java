package com.somemone.dynamiceeconomy.listener.api;

import com.Acrobot.ChestShop.Events.TransactionEvent;
import com.somemone.dynamiceeconomy.db.SellerHandler;
import com.somemone.dynamiceeconomy.db.TransactionHandler;
import com.somemone.dynamiceeconomy.db.model.Seller;
import com.somemone.dynamiceeconomy.db.model.Transaction;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.time.LocalDateTime;

public class ChestShopListener implements Listener {

    @EventHandler
    public void handle(TransactionEvent event) {
        Seller seller = SellerHandler.findSeller( event.getOwnerAccount().getUuid().toString() );
        if (seller == null) {
            seller = new Seller(event.getOwnerAccount().getUuid().toString(), false, true);
        }
        String type = "";
        if (event.getTransactionType().equals(TransactionEvent.TransactionType.BUY)) {
            type = "buy";
        } else {
            type = "sell";
        }

        if (seller.isBanned()) return;
        Transaction transaction = new Transaction(event.getStock()[0].getType().name(),
                1,
                event.getExactPrice().floatValue(),
                type,
                seller,
                LocalDateTime.now());

        TransactionHandler.writeTransaction(transaction);
    }

}
