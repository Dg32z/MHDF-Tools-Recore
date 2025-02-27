package cn.chengzhiya.mhdftools.entity.data;

import cn.chengzhiya.mhdftools.entity.AbstractDao;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@DatabaseTable(tableName = "mhdftools_vanish")
public final class VanishStatus extends AbstractDao {
    public VanishStatus() {
        super(
                "vanishSettings.enable"
        );
    }

    @DatabaseField(id = true, canBeNull = false)
    private UUID player;
    @DatabaseField(canBeNull = false)
    private boolean isEnable;
}
