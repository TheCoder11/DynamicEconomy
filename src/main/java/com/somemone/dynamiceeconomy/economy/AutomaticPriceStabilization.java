package com.somemone.dynamiceeconomy.economy;

import com.somemone.dynamiceeconomy.DynamicEeconomy;
import com.somemone.dynamiceeconomy.config.StoresConfig;
import com.somemone.dynamiceeconomy.db.MarketPositionHandler;
import com.somemone.dynamiceeconomy.db.SessionHandler;
import com.somemone.dynamiceeconomy.db.TransactionHandler;
import com.somemone.dynamiceeconomy.db.model.MarketPosition;
import com.somemone.dynamiceeconomy.db.model.Transaction;
import lombok.Getter;
import org.bukkit.Material;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class AutomaticPriceStabilization {

    /*



     */

    @Getter
    private List<MarketPosition> pastPositions;

    @Getter
    private ItemStore store;

    /**
     *  The desired quantity of items sold, expressed in Active Hours per Sale
     */
    @Getter
    private float desiredQuantity;

    @Getter
    private float activeHoursPerSale;

    @Getter
    private float currentPrice;

    @Getter
    private float newPrice;

    @Getter
    private Material itemMarket;

    @Getter
    private float slope;

    @Getter
    private boolean aps;

    @Getter
    private boolean established;

    public AutomaticPriceStabilization (String item) {

        StoresConfig instance = new StoresConfig();
        store = instance.getStore(item);

        this.desiredQuantity = store.getDesiredQuantity();
        this.currentPrice = store.getPrice();
        this.aps = store.isAps();
        this.pastPositions = MarketPositionHandler.getAllPositionsFromItem(itemMarket);
        int salesLastDay = 0;
        slope = 0f;

        List<Transaction> pastSales = TransactionHandler.getTransactionsWithItem(itemMarket);
        LocalDateTime timeDesired = LocalDateTime.now().minusDays(1l);
        for (Transaction sale : pastSales) {
            if (sale.getDatetime().isAfter(timeDesired) && !sale.getSeller().isPrivate()) {
                salesLastDay++;
            } else {
                break;
            }
        }

        float activeHours = SessionHandler.getHoursInDays(Duration.ofHours(1));
        activeHoursPerSale = activeHours / salesLastDay;

        established = true;
        if (pastPositions.size() == 0) {
            established = false;
        }
        for (MarketPosition pos : pastPositions) {
            if (!pos.isEstablished()) {
                established = false;
            }
        }

        if ( Math.abs(activeHoursPerSale - desiredQuantity) / ((activeHoursPerSale + desiredQuantity) / 2) < 0.1 ) {
            established = true;
        }

    }

    public float findBestPrice() {
        if (!aps) {
            newPrice = currentPrice;
            return currentPrice;
        }

        // If other values are present, find the line of best fit.
        if (pastPositions.size() > 0) {
            float count = pastPositions.size() + 1; // This includes the current position
            float sumQ = 0f;
            float sumP = 0f;
            float sumQ2 = 0f;
            float sumPQ = 0f;
            for (MarketPosition mp : pastPositions) {
                sumQ += mp.getAhs();
                sumP += mp.getPrice();
                sumQ2 += Math.pow(mp.getAhs(), 2);
                sumPQ += mp.getPrice() * mp.getAhs();
            }
            sumQ += activeHoursPerSale;
            sumP += currentPrice;
            sumQ2 += Math.pow(activeHoursPerSale, 2);
            sumPQ += activeHoursPerSale * currentPrice;

            float QMean = sumQ / count;
            float PMean = sumP / count;

            float slope = (sumPQ - sumQ * PMean) / (sumQ2 - sumQ * QMean);
            float pInt = PMean - slope * QMean;

            // Return result at desired quanitity on projected demand line
            newPrice = pInt + (slope * desiredQuantity);
            return newPrice;


        } else { // If none are present, raise or lower the price by a given amount.
            float percentage = DynamicEeconomy.getPluginConfig().getCorrectionPercent() / 100;
            if (activeHoursPerSale > desiredQuantity) { // Price will lower
                newPrice = (currentPrice * (1 - percentage));
                return newPrice;
            } else {
                newPrice = (currentPrice * (1 + percentage));
                return newPrice;
            }
        }
    }

    /**
     *  Commits MarketPosition to database
     */
    public void commitToDatabase() {
        MarketPosition marketPosition = new MarketPosition(itemMarket.name(), newPrice,
                activeHoursPerSale, LocalDateTime.now(), slope, established );

        StoresConfig config = new StoresConfig();
        store.setPrice(newPrice);
        config.putStore(store.getName(), store);
        config.saveConfig();

        MarketPositionHandler.writeMarketPosition(marketPosition);
    }



}
