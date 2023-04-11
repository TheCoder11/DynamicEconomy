package com.somemone.dynamiceeconomy.db.model;

import com.j256.ormlite.field.DatabaseField;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

public class ObtainMaterial {

    public static final String ID_COLUMN_NAME = "id";
    public static final String MATERIAL_COLUMN_NAME = "material";
    public static final String DATETIME_COLUMN_NAME = "saletime";
    public static final String PLAYER_COLUMN_NAME = "uuid";


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
    @DatabaseField(columnName = PLAYER_COLUMN_NAME)
    private String playerUuid;

    @Getter
    @Setter
    @DatabaseField(columnName = DATETIME_COLUMN_NAME)
    private Timestamp datetime;

    public ObtainMaterial() {

    }

    public ObtainMaterial(Material material, UUID playerUuid) {
        this.material = material.name();
        this.playerUuid = playerUuid.toString();
    }

    public LocalDateTime getDatetime() {
        return datetime.toLocalDateTime();
    }

}
