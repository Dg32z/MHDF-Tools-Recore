package cn.chengzhiya.mhdftools.entity.data;

import cn.chengzhiya.mhdftools.entity.AbstractDao;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@DatabaseTable(tableName = "mhdftools_economy")
public final class EconomyData extends AbstractDao {
    @DatabaseField(id = true, canBeNull = false)
    private UUID player;
    @DatabaseField(format = "20,2", canBeNull = false)
    private BigDecimal bigDecimal;
}
