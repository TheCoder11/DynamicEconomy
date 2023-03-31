package com.somemone.dynamiceeconomy.economy;

import com.somemone.dynamiceeconomy.DynamicEeconomy;
import com.somemone.dynamiceeconomy.db.MarketPositionHandler;
import com.somemone.dynamiceeconomy.db.SessionHandler;
import com.somemone.dynamiceeconomy.db.TransactionHandler;
import com.somemone.dynamiceeconomy.model.MarketPosition;
import com.somemone.dynamiceeconomy.model.Transaction;
import lombok.Getter;
import org.bukkit.Material;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AutomaticPriceStabilization {

    /*



     */

    @Getter
    private List<MarketPosition> pastPositions;

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

    public AutomaticPriceStabilization (float desiredQuantity, float currentPrice, Material itemMarket) {
        this.desiredQuantity = desiredQuantity;
        this.currentPrice = currentPrice;
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

    }

    public float findBestPrice() {
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
            float percentage = DynamicEeconomy.getConfig().getCorrectionPercent() / 100;
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
                activeHoursPerSale, LocalDateTime.now(), slope, false );

        MarketPositionHandler.writeMarketPosition(marketPosition);
    }



}
