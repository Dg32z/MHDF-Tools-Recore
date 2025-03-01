package cn.chengzhiya.mhdftools.entity.data;

import cn.chengzhiya.mhdftools.entity.AbstractDao;
import cn.chengzhiya.mhdftools.entity.BungeeCordLocation;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@DatabaseTable(tableName = "mhdftools_home")
public final class HomeData extends AbstractDao {
    @DatabaseField(generatedId = true, canBeNull = false)
    private Integer id;
    @DatabaseField(canBeNull = false)
    private UUID player;
    @DatabaseField(canBeNull = false)
    private String home;
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
                getX(),
                getY(),
                getZ(),
                getYaw(),
                getPitch()
        );
    }
}
