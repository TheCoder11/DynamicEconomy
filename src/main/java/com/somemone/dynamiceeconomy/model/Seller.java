package com.somemone.dynamiceeconomy.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@DatabaseTable(tableName = "sellers")
public class Seller {
    public static final String UUID_COLUMN_NAME = "uuid";
    public static final String BANNED_COLUMN_NAME = "banned";
    public static final String ISPRIVATE_COLUMN_NAME = "isprivate";

    @Getter
    @Setter
    @DatabaseField(id = true, columnName = UUID_COLUMN_NAME)
    private String uuid;                // UUID of seller

    @Getter
    @Setter
    @DatabaseField(columnName = BANNED_COLUMN_NAME)
    private boolean banned;             // Banning flag -- too many purchases?

    @Getter
    @Setter
    @DatabaseField(defaultValue = "true", columnName = ISPRIVATE_COLUMN_NAME)
    private boolean isPrivate;

    Seller () {

    }

    public Seller(String uuid, boolean banned, boolean isPrivate) {
        this.uuid = uuid;
        this.banned = banned;
        this.isPrivate = isPrivate;
    }

}
