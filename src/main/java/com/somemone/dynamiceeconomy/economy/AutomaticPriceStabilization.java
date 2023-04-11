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
import java.time.temporal.ChronoUnit;
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
    private float salesPerActiveHour;

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
        itemMarket = Material.matchMaterial(item);

        this.desiredQuantity = store.getDesiredQuantity();
        this.currentPrice = store.getPrice();
        this.aps = store.isAps();
        this.pastPositions = MarketPositionHandler.getAllPositionsFromItem(itemMarket);
        int salesLastDay = 0;
        slope = 0f;

        List<Transaction> pastSales = TransactionHandler.getTransactionsWithItem(itemMarket);

        LocalDateTime timeDesired = LocalDateTime.MIN;
        for (MarketPosition pos : pastPositions)
            if (pos.getStarttime().isAfter(timeDesired))
                timeDesired = pos.getStarttime();


        for (Transaction sale : pastSales) {
            ItemStore.APSType type = sale.getType();

            if (sale.getDatetime().isAfter(timeDesired) && !sale.getSeller().isPrivate() && store.getApsTrackingType().equals(type)) {
                salesLastDay += sale.getAmount();
            }
        }

        Duration dur = Duration.between(timeDesired, LocalDateTime.now());
        if (dur.isZero()) dur = ChronoUnit.FOREVER.getDuration();

        float activeHours = SessionHandler.getHoursInDays(dur);

        salesPerActiveHour = salesLastDay / activeHours;

        established = true;
        if (pastPositions.size() == 0) {
            established = false;
        }
        for (MarketPosition pos : pastPositions) {
            if (!pos.isEstablished()) {
                established = false;
            }
        }

        if ( Math.abs(salesPerActiveHour - desiredQuantity) / ((salesPerActiveHour + desiredQuantity) / 2) < 0.1 ) {
            established = true;
        }

    }

    public float findBestPrice() {
        if (!aps) {
            newPrice = currentPrice;
            return currentPrice;
        }

        // If other values are present, find the line of best fit.
        if (pastPositions.size() > 1) {

            float[] sahValues = new float[pastPositions.size()];
            float[] priceValues = new float[pastPositions.size()];

            priceValues[0] = pastPositions.get(0).getPrice(); // Re-assign
            for (int i = 1; i < pastPositions.size(); i++) {
                priceValues[i] = pastPositions.get(i).getPrice();
                sahValues[i - 1] = pastPositions.get(i).getSah();
            }
            sahValues[sahValues.length - 1] = salesPerActiveHour;

            LinearRegression linReg = new LinearRegression(sahValues, priceValues);
            slope = linReg.slope();

            if (slope > 0) { // Shouldn't happen, people are messing with the algorithm (means people buy more when prices are high
                float percentage = DynamicEeconomy.getPluginConfig().getCorrectionPercent() / 100f;
                if (salesPerActiveHour < desiredQuantity) { // Price will lower
                    newPrice = (currentPrice * (1 - percentage));
                } else {
                    newPrice = (currentPrice * (1 + percentage));
                }
            } else {
                // Return result at desired quanitity on projected demand line
                newPrice = linReg.predict(desiredQuantity);
            }

            return newPrice;


        } else { // If none are present, raise or lower the price by a given amount.
            float percentage = DynamicEeconomy.getPluginConfig().getCorrectionPercent() / 100f;
            if (Math.abs(salesPerActiveHour - desiredQuantity) / ((salesPerActiveHour + desiredQuantity) / 2) < DynamicEeconomy.getPluginConfig().getEstablishPercent()) {
                established = true;
                newPrice = currentPrice;
            } else {
                if (salesPerActiveHour < desiredQuantity) { // Price will lower
                    newPrice = (currentPrice * (1 - percentage));
                } else {
                    newPrice = (currentPrice * (1 + percentage));
                }
            }
            return newPrice;
        }
    }

    /**
     *  Commits MarketPosition to database
     */
    public void commitToDatabase() {
        MarketPosition marketPosition = new MarketPosition(itemMarket.name(), newPrice,
                salesPerActiveHour, LocalDateTime.now(), slope, established );

        StoresConfig config = new StoresConfig();
        store.setPrice(newPrice);
        config.putStore(store.getName(), store);
        config.saveConfig();

        MarketPositionHandler.writeMarketPosition(marketPosition);
    }



}
