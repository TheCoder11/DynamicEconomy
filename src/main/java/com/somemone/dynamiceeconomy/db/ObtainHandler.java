package com.somemone.dynamiceeconomy.db;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.PreparedQuery;
import com.somemone.dynamiceeconomy.DynamicEeconomy;
import com.somemone.dynamiceeconomy.db.model.ObtainMaterial;
import org.bukkit.Material;

import java.sql.SQLException;
import java.util.List;

public class ObtainHandler {

    public static void writeObtainal(ObtainMaterial obtainal) {

        try {
            Dao<ObtainMaterial, Integer> accountDao = DaoManager.createDao(DynamicEeconomy.getConnectionSource(), ObtainMaterial.class);
            accountDao.create(obtainal);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public static List<ObtainMaterial> getAllObtainals () {

        try {
            Dao<ObtainMaterial, Integer> accountDao = DaoManager.createDao(DynamicEeconomy.getConnectionSource(), ObtainMaterial.class);
            return accountDao.queryForAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public static List<ObtainMaterial> getTransactionsWithItem (String material) {
        try {
            Dao<ObtainMaterial, Integer> accountDao = DaoManager.createDao(DynamicEeconomy.getConnectionSource(), ObtainMaterial.class);
            PreparedQuery<ObtainMaterial> query = accountDao.queryBuilder().where().eq(ObtainMaterial.MATERIAL_COLUMN_NAME, material).prepare();
            return accountDao.query(query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
