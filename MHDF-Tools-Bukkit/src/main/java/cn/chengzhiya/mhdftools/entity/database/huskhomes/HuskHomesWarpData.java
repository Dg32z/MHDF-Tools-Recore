package cn.chengzhiya.mhdftools.entity.database.huskhomes;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@DatabaseTable
public final class HuskHomesWarpData {
    @DatabaseField(canBeNull = false)
    private UUID uuid;
    @DatabaseField(canBeNull = false, columnName = "saved_position_id")
    private int positionInfoId;
}
