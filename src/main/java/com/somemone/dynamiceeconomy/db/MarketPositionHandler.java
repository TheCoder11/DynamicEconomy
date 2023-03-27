package com.somemone.dynamiceeconomy.db;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.somemone.dynamiceeconomy.DynamicEeconomy;
import com.somemone.dynamiceeconomy.model.MarketPosition;
import org.bukkit.Material;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class MarketPositionHandler {

    public static void writeMarketPosition(MarketPosition transaction) {

        try {
            Dao<MarketPosition, Integer> accountDao = DaoManager.createDao(DynamicEeconomy.getConnectionSource(), MarketPosition.class);
            accountDao.create(transaction);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public static List<MarketPosition> getAllMarketPositions () {

        try {
            Dao<MarketPosition, Integer> accountDao = DaoManager.createDao(DynamicEeconomy.getConnectionSource(), MarketPosition.class);
            return accountDao.queryForAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public static List<MarketPosition> getAllPositionsFromDate (Date date) {
        try {
            Dao<MarketPosition, Integer> accountDao = DaoManager.createDao(DynamicEeconomy.getConnectionSource(), MarketPosition.class);
            return accountDao.query(accountDao.queryBuilder().where()
                            .eq(MarketPosition.DATETIME_COLUMN_NAME, date).prepare());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<MarketPosition> getAllPositionsFromItem (Material item) {
        try {
            Dao<MarketPosition, Integer> accountDao = DaoManager.createDao(DynamicEeconomy.getConnectionSource(), MarketPosition.class);
            return accountDao.query(accountDao.queryBuilder().where()
                    .eq(MarketPosition.ITEM_COLUMN_NAME, item.name()).prepare());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static MarketPosition getExactPosition (Date date, Material item) {
        try {
            Dao<MarketPosition, Integer> accountDao = DaoManager.createDao(DynamicEeconomy.getConnectionSource(), MarketPosition.class);
            return accountDao.query(accountDao.queryBuilder().where()
                    .eq(MarketPosition.DATETIME_COLUMN_NAME, date)
                    .eq(MarketPosition.ITEM_COLUMN_NAME, item.name()).prepare()).get(0); // Risky, but there will be one position per day
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
