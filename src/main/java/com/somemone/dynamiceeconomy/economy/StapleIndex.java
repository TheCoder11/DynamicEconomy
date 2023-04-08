package com.somemone.dynamiceeconomy.economy;

import com.somemone.dynamiceeconomy.db.ObtainHandler;
import com.somemone.dynamiceeconomy.db.SessionHandler;
import com.somemone.dynamiceeconomy.db.model.ObtainMaterial;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class StapleIndex {

    private List<ObtainMaterial> pastSales;

    private Duration timeToSearch;

    public StapleIndex(String material, Duration timeToSearch) {

        List<ObtainMaterial> allSales = ObtainHandler.getTransactionsWithItem(material);
        LocalDateTime cutoff = LocalDateTime.now().minus(timeToSearch);

        for (ObtainMaterial sales : allSales) {
            if (sales.getDatetime().isAfter(cutoff)) {
                pastSales.add(sales);
            } else {
                break;
            }
        }

    }

    public float getStapleIndexAHS () {

        float activeHours = SessionHandler.getHoursInDays(timeToSearch);

        return activeHours / ((float) pastSales.size());

    }

}
