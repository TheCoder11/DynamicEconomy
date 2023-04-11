package com.somemone.dynamiceeconomy.db.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@DatabaseTable(tableName = "marketpositions")
public class MarketPosition { // A structure of a market's sales/price on a certain day in a single item

    public static final String ID_COLUMN_NAME = "id";
    public static final String ITEM_COLUMN_NAME = "item";
    public static final String PRICE_COLUMN_NAME = "price";
    public static final String STARTTIME_COLUMN_NAME = "starttime";
    public static final String SAH_COLUMN_NAME = "sah"; // Sales per Active Hour
    public static final String DEMAND_SLOPE_COLUMN_NAME = "slope";
    public static final String ESTABLISHED_COLUMN_NAME = "established";

    @Setter
    @Getter
    @DatabaseField(generatedId = true, columnName = ID_COLUMN_NAME)
    private int id;

    @Setter
    @Getter
    @DatabaseField(columnName = ITEM_COLUMN_NAME)
    private String item;

    @Setter
    @Getter
    @DatabaseField(columnName = PRICE_COLUMN_NAME)
    private float price;

    @Setter
    @Getter
    @DatabaseField(columnName = STARTTIME_COLUMN_NAME)
    private Timestamp starttime;

    @Setter
    @Getter
    @DatabaseField(columnName = SAH_COLUMN_NAME)
    private float sah;

    @Setter
    @Getter
    @DatabaseField(columnName = DEMAND_SLOPE_COLUMN_NAME)
    private float slope;

    @Getter
    @Setter
    @DatabaseField(columnName = ESTABLISHED_COLUMN_NAME)
    private boolean established;

    MarketPosition() {

    }

    public MarketPosition(int id, String item, float price, float sah, LocalDateTime starttime, float slope, boolean established) {
        this.id = id;
        this.item = item;
        this.price = price;
        this.sah = sah;
        this.starttime = Timestamp.valueOf(starttime);
        this.slope = slope;
        this.established = established;
    }

    public MarketPosition(String item, float price, float sah, LocalDateTime starttime, float slope, boolean established) {
        this.item = item;
        this.price = price;
        this.sah = sah;
        this.starttime = Timestamp.valueOf(starttime);
        this.slope = slope;
        this.established = established;
    }

    public LocalDateTime getStarttime() {
        return starttime.toLocalDateTime();
    }

}
