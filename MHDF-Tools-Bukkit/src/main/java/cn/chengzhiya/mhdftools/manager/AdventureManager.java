package cn.chengzhiya.mhdftools.manager;

import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.interfaces.Init;
import lombok.Getter;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;

@Getter
public final class AdventureManager implements Init {
    private BukkitAudiences adventure;

    @Override
    public void init() {
        this.adventure = BukkitAudiences.create(Main.instance);
    }

    public void close() {
        if (this.adventure != null) {
            this.adventure.close();
        }

        this.adventure = null;
    }
}
