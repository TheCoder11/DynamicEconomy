package com.somemone.dynamiceeconomy.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.time.LocalDateTime;

@Builder
@DatabaseTable(tableName = "sessions")
public class Session {

    public static final String ID_COLUMN_NAME = "id";
    public static final String UUID_COLUMN_NAME = "material";
    public static final String START_TIME_COLUMN_NAME = "amount";
    public static final String END_TIME_COLUMN_PRICE = "price";


    @Setter
    @Getter
    @DatabaseField(generatedId = true, columnName = ID_COLUMN_NAME)
    private int id;

    @Setter
    @Getter
    @DatabaseField(columnName = UUID_COLUMN_NAME)
    private String uuid;

    @Setter
    @Getter
    @DatabaseField(columnName = START_TIME_COLUMN_NAME)
    private LocalDateTime startTime;

    @Setter
    @Getter
    @Nullable
    @DatabaseField(columnName = END_TIME_COLUMN_PRICE)
    private LocalDateTime endTime;

    public Session() {

    }

    public Session(Player player, LocalDateTime startTime) {
        this.uuid = player.getUniqueId().toString();
        this.startTime = startTime;
    }


}
