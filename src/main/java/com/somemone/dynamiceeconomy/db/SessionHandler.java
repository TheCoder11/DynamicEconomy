package com.somemone.dynamiceeconomy.db;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.PreparedQuery;
import com.somemone.dynamiceeconomy.DynamicEeconomy;
import com.somemone.dynamiceeconomy.model.Session;

import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SessionHandler {

    public static void writeSession(Session session) {

        try {
            Dao<Session, Integer> accountDao = DaoManager.createDao(DynamicEeconomy.getConnectionSource(), Session.class);
            accountDao.create(session);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public static List<Session> getAllTransactions () {

        try {
            Dao<Session, Integer> accountDao = DaoManager.createDao(DynamicEeconomy.getConnectionSource(), Session.class);
            return accountDao.queryForAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public static float getHoursInDays (Duration duration) {

        LocalDateTime desiredTime = LocalDateTime.now().minus(duration);
        float hours = 0f;
        List<Session> sessions = new ArrayList<>();
        try {
            Dao<Session, Integer> accountDao = DaoManager.createDao(DynamicEeconomy.getConnectionSource(), Session.class);
            PreparedQuery<Session> query = accountDao.queryBuilder().where()
                    .gt(Session.END_TIME_COLUMN_PRICE, desiredTime)
                    .prepare();
            sessions = accountDao.queryForAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        for (Session session : sessions) {
            hours += Duration.between(session.getEndTime(), session.getStartTime()).toHours();
        }

        return hours;

    }

}
