package cn.chengzhiya.mhdftools.menu;

import cn.chengzhiya.mhdfscheduler.scheduler.MHDFScheduler;
import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.interfaces.Menu;
import cn.chengzhiya.mhdftools.util.config.ConfigUtil;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.util.List;

@Getter
public abstract class AbstractMenu implements InventoryHolder, Menu {
    private final boolean enable;
    private final Player player;

    public AbstractMenu(Player player) {
        this.enable = true;
        this.player = player;
    }

    public AbstractMenu(String enableKey, Player player) {
        if (enableKey == null || enableKey.isEmpty()) {
            this.enable = true;
        } else {
            this.enable = ConfigUtil.getConfig().getBoolean(enableKey);
        }

        this.player = player;
    }

    public AbstractMenu(List<String> enableKeyList, Player player) {
        boolean enable = true;
        for (String enableKey : enableKeyList) {
            if (enableKey == null || enableKey.isEmpty()) {
                continue;
            }

            enable = ConfigUtil.getConfig().getBoolean(enableKey);
        }

        this.enable = enable;
        this.player = player;
    }

    public void onOpen(InventoryOpenEvent event) {
        if (!isEnable()) {
            return;
        }

        open(event);
    }

    public void onClick(InventoryClickEvent event) {
        if (!isEnable()) {
            return;
        }

        click(event);
    }

    public void onClose(InventoryCloseEvent event) {
        if (!isEnable()) {
            return;
        }

        close(event);
    }

    public void openMenu() {
        if (!isEnable()) {
            return;
        }

        MHDFScheduler.getAsyncScheduler().runTask(Main.instance, () -> {
            Inventory menu = getInventory();

            MHDFScheduler.getGlobalRegionScheduler().runTask(Main.instance, () -> player.openInventory(menu));
        });
    }
}
