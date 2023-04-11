package com.somemone.dynamiceeconomy.listener.api;

import com.somemone.dynamiceeconomy.db.SellerHandler;
import com.somemone.dynamiceeconomy.db.TransactionHandler;
import com.somemone.dynamiceeconomy.db.model.Seller;
import com.somemone.dynamiceeconomy.db.model.Transaction;
import com.somemone.dynamiceeconomy.economy.ItemStore;
import de.epiceric.shopchest.event.ShopBuySellEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.time.LocalDateTime;

public class ShopChestListener implements Listener {

    @EventHandler
    public void handle (ShopBuySellEvent event) {
        Seller seller = SellerHandler.findSeller( event.getShop().getVendor().getUniqueId().toString() );
        if (seller == null) {
            seller = new Seller( event.getShop().getVendor().getUniqueId().toString(), false, true );
        }
        if (seller.isBanned()) return;

        ItemStore.APSType type = ItemStore.APSType.valueOf(event.getType().name());

        Transaction transaction = new Transaction(
                event.getShop().getProduct().getType().name(),
                event.getNewAmount(),
                (float) event.getNewPrice(),
                type,
                seller,
                LocalDateTime.now()
        );

        TransactionHandler.writeTransaction(transaction);
    }
}
