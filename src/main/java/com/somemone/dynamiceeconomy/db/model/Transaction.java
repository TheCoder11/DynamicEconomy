package com.somemone.dynamiceeconomy.db.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.somemone.dynamiceeconomy.economy.ItemStore;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@DatabaseTable(tableName = "transactions")
public class Transaction {

    public static final String ID_COLUMN_NAME = "id";
    public static final String MATERIAL_COLUMN_NAME = "material";
    public static final String AMOUNT_COLUMN_NAME = "amount";
    public static final String PRICE_COLUMN_NAME = "price";
    public static final String SELLER_COLUMN_NAME = "seller";
    public static final String DATETIME_COLUMN_NAME = "saletime";
    public static final String TYPE_COLUMN_NAME = "type";


    @Getter
    @Setter
    @DatabaseField(generatedId = true, columnName = ID_COLUMN_NAME)
    private int id;

    @Getter
    @Setter
    @DatabaseField(columnName = MATERIAL_COLUMN_NAME)
    private String material;

    @Getter
    @Setter
    @DatabaseField(columnName = AMOUNT_COLUMN_NAME)
    private int amount;

    @Getter
    @Setter
    @DatabaseField(columnName = PRICE_COLUMN_NAME)
    private float price;

    @Getter
    @Setter
    @DatabaseField(foreign = true, columnName = SELLER_COLUMN_NAME)
    private Seller seller;

    @Getter
    @Setter
    @DatabaseField(columnName = DATETIME_COLUMN_NAME)
    private Timestamp datetime;

    @Getter
    @Setter
    @DatabaseField(columnName = TYPE_COLUMN_NAME)
    private ItemStore.APSType type; // "buy" or "sell"

    public Transaction() {

    }
    public Transaction(int id, String material, int amount, float price, ItemStore.APSType type, Seller seller, LocalDateTime datetime) {
        this.id = id;
        this.material = material;
        this.amount = amount;
        this.price = price;
        this.seller = seller;
        this.datetime = Timestamp.valueOf(datetime);
        this.type = type;

    }

    public Transaction(String material, int amount, float price, ItemStore.APSType type, Seller seller, LocalDateTime datetime) {
        this.material = material;
        this.amount = amount;
        this.price = price;
        this.seller = seller;
        this.datetime = Timestamp.valueOf(datetime);
        this.type = type;

    }

    public LocalDateTime getDatetime() {
        return datetime.toLocalDateTime();
    }
}
