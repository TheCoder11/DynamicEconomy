package com.somemone.dynamiceeconomy.db.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Builder
@DatabaseTable(tableName = "marketpositions")
public class MarketPosition { // A structure of a market's sales/price on a certain day in a single item

    public static final String ID_COLUMN_NAME = "id";
    public static final String ITEM_COLUMN_NAME = "item";
    public static final String PRICE_COLUMN_NAME = "price";
    public static final String STARTTIME_COLUMN_NAME = "starttime";
    public static final String AHS_COLUMN_NAME = "sales";
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
    private LocalDateTime starttime;

    @Setter
    @Getter
    @DatabaseField(columnName = AHS_COLUMN_NAME)
    private float ahs;

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

    public MarketPosition(String item, float price, float ahs, LocalDateTime starttime, float slope, boolean established) {
        this.item = item;
        this.price = price;
        this.ahs = ahs;
        this.starttime = starttime;
        this.slope = slope;
        this.established = established;
    }

}
