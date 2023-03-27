package com.somemone.dynamiceeconomy.listener.api;

import com.somemone.dynamiceeconomy.db.SellerHandler;
import com.somemone.dynamiceeconomy.db.TransactionHandler;
import com.somemone.dynamiceeconomy.model.Seller;
import com.somemone.dynamiceeconomy.model.Transaction;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.maxgamer.quickshop.api.event.ShopPurchaseEvent;

import java.time.LocalDateTime;

public class QuickShopListener implements Listener {

    @EventHandler
    public void handle (ShopPurchaseEvent event) {
        Seller seller = SellerHandler.findSeller(event.getShop().getOwner().toString());
        if (seller == null) { // Create new seller
            seller = new Seller(event.getShop().getOwner().toString(), false, true);
        }
        if (seller.isBanned()) return;

        Transaction transaction = new Transaction(event.getShop().getItem().getType().name(),
                event.getAmount(),
                (float) event.getTotal(),
                seller,
                LocalDateTime.now());

        TransactionHandler.writeTransaction(transaction);
    }
}
