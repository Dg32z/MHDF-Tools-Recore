package cn.chengzhiya.mhdftools.entity.data;

import cn.chengzhiya.mhdftools.entity.AbstractDao;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@DatabaseTable(tableName = "mhdftools_fly")
public final class FlyStatus extends AbstractDao {
    @DatabaseField(id = true, canBeNull = false)
    private UUID player;
    @DatabaseField(canBeNull = false)
    private boolean isEnable;
    @DatabaseField(canBeNull = false)
    private long time;

    public FlyStatus() {
        super(
                "flySettings.enable"
        );
    }
}
