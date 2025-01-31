package cn.ChengZhiYa.MHDFTools.entity.data;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@DatabaseTable(tableName = "mhdftools_vanish")
public final class VanishStatus {
    @DatabaseField(id = true, dataType = DataType.UUID, canBeNull = false)
    private UUID player;
    @DatabaseField(dataType = DataType.BOOLEAN, canBeNull = false)
    private boolean isEnable;
}
