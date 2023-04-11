package com.somemone.dynamiceeconomy.db.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@DatabaseTable(tableName = "sessions")
@AllArgsConstructor
public class Session {

    public static final String ID_COLUMN_NAME = "id";
    public static final String UUID_COLUMN_NAME = "player";
    public static final String START_TIME_COLUMN_NAME = "starttime";
    public static final String END_TIME_COLUMN_PRICE = "endtime";


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
    private Timestamp startTime;

    @Setter
    @Getter
    @Nullable
    @DatabaseField(columnName = END_TIME_COLUMN_PRICE)
    private Timestamp endTime;

    public Session() {

    }

    public Session(Player player, LocalDateTime startTime, LocalDateTime endTime) {
        this.uuid = player.getUniqueId().toString();
        this.startTime = Timestamp.valueOf( startTime );
        this.endTime = Timestamp.valueOf(endTime);
    }

    public Session(Player player, LocalDateTime startTime) {
        this.uuid = player.getUniqueId().toString();
        this.startTime = Timestamp.valueOf(startTime);
    }

    public Session(UUID uuid, LocalDateTime startTime) {
        this.uuid = uuid.toString();
        this.startTime = Timestamp.valueOf(startTime);
    }

    public LocalDateTime getStartTime() {
        return startTime.toLocalDateTime();
    }

    public LocalDateTime getEndTime() {
        return endTime.toLocalDateTime();
    }


}
