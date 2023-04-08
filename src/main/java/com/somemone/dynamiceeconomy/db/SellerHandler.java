package com.somemone.dynamiceeconomy.db;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.PreparedQuery;
import com.somemone.dynamiceeconomy.DynamicEeconomy;
import com.somemone.dynamiceeconomy.db.model.Seller;

import java.sql.SQLException;
import java.util.List;

public class SellerHandler {

    public static void writeSeller(Seller seller) {

        try {
            Dao<Seller, Integer> accountDao = DaoManager.createDao(DynamicEeconomy.getConnectionSource(), Seller.class);
            accountDao.create(seller);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public static List<Seller> getAllObtainals () {

        try {
            Dao<Seller, Integer> accountDao = DaoManager.createDao(DynamicEeconomy.getConnectionSource(), Seller.class);
            return accountDao.queryForAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public static Seller findSeller (String uuid) {
        try {
            Dao<Seller, String> accountDao = DaoManager.createDao(DynamicEeconomy.getConnectionSource(), Seller.class);
            PreparedQuery<Seller> query = accountDao.queryBuilder().where().eq(Seller.UUID_COLUMN_NAME, uuid).prepare();
            return accountDao.queryForId(uuid);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
