package com.somemone.dynamiceeconomy.db;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.table.TableUtils;
import com.somemone.dynamiceeconomy.DynamicEeconomy;
import com.somemone.dynamiceeconomy.db.model.MarketPosition;
import com.somemone.dynamiceeconomy.db.model.Seller;
import com.somemone.dynamiceeconomy.db.model.Session;
import com.somemone.dynamiceeconomy.db.model.Transaction;
import org.bukkit.Material;

import java.sql.SQLException;
import java.util.List;

public class TransactionHandler {

    public static void initializeTables() {

        try {
            TableUtils.createTableIfNotExists(DynamicEeconomy.getConnectionSource(), Transaction.class);
            TableUtils.createTableIfNotExists(DynamicEeconomy.getConnectionSource(), Seller.class);
            TableUtils.createTableIfNotExists(DynamicEeconomy.getConnectionSource(), Session.class);
            TableUtils.createTableIfNotExists(DynamicEeconomy.getConnectionSource(), MarketPosition.class);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public static void writeTransaction(Transaction transaction) {

        try {
            Dao<Transaction, Integer> accountDao = DaoManager.createDao(DynamicEeconomy.getConnectionSource(), Transaction.class);
            accountDao.create(transaction);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public static List<Transaction> getAllTransactions () {

        try {
            Dao<Transaction, Integer> accountDao = DaoManager.createDao(DynamicEeconomy.getConnectionSource(), Transaction.class);
            return accountDao.queryForAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public static List<Transaction> getTransactionsWithItem (Material material) {
        try {
            Dao<Transaction, Integer> accountDao = DaoManager.createDao(DynamicEeconomy.getConnectionSource(), Transaction.class);
            PreparedQuery<Transaction> query = accountDao.queryBuilder().where().eq(Transaction.MATERIAL_COLUMN_NAME, material.name()).prepare();
            return accountDao.query(query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
