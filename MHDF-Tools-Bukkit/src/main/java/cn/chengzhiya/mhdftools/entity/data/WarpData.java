package cn.ChengZhiYa.MHDFTools.entity.data;

import cn.ChengZhiYa.MHDFTools.entity.AbstractDao;
import cn.ChengZhiYa.MHDFTools.entity.BungeeCordLocation;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@DatabaseTable(tableName = "mhdftools_warp")
public final class WarpData extends AbstractDao {
    @DatabaseField(id = true, canBeNull = false)
    private String warp;
    @DatabaseField(canBeNull = false)
    private String server;
    @DatabaseField(canBeNull = false)
    private String world;
    @DatabaseField(canBeNull = false)
    private Double x;
    @DatabaseField(canBeNull = false)
    private Double y;
    @DatabaseField(canBeNull = false)
    private Double z;
    @DatabaseField(canBeNull = false)
    private Float yaw;
    @DatabaseField(canBeNull = false)
    private Float pitch;

    /**
     * 转换为群组位置实例
     *
     * @return 群组位置实例
     */
    public BungeeCordLocation toBungeeCordLocation() {
        return new BungeeCordLocation(
                getServer(),
                getWorld(),
                getY(),
                getY(),
                getY(),
                getYaw(),
                getPitch()
        );
    }
}
