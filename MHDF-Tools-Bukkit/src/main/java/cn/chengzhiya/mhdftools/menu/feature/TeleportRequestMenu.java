package cn.chengzhiya.mhdftools.menu.feature;

import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.enums.TeleportRequestType;
import cn.chengzhiya.mhdftools.menu.AbstractMenu;
import cn.chengzhiya.mhdftools.util.action.ActionUtil;
import cn.chengzhiya.mhdftools.util.config.MenuConfigUtil;
import cn.chengzhiya.mhdftools.util.feature.TpaHereUtil;
import cn.chengzhiya.mhdftools.util.feature.TpaUtil;
import cn.chengzhiya.mhdftools.util.menu.MenuUtil;
import io.papermc.paper.persistence.PersistentDataContainerView;
import lombok.Getter;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Locale;

@Getter
public final class TeleportRequestMenu extends AbstractMenu {
    private final TeleportRequestType requestType;
    private final YamlConfiguration config;
    private final int page;

    public TeleportRequestMenu(Player player, TeleportRequestType requestType, int page) {
        super(
                "tpahereSettings.enable",
                player
        );
        this.requestType = requestType;
        this.config = MenuConfigUtil.getMenuConfig(requestType.name().toLowerCase(Locale.ROOT));
        this.page = page;
    }

    @Override
    public @NotNull Inventory getInventory() {
        Inventory menu = MenuUtil.createInventory(this, getConfig());

        ConfigurationSection items = getConfig().getConfigurationSection("items");
        if (items == null) {
            return menu;
        }

        List<String> playerList = Main.instance.getBungeeCordManager().getPlayerList();
        List<Integer> playerSlotList = MenuUtil.getSlotList(items.getConfigurationSection("玩家"));

        int start = (page - 1) * playerSlotList.size();
        int maxEnd = page * playerSlotList.size();
        int end = Math.min(playerList.size(), maxEnd);

        for (String key : items.getKeys(false)) {
            ConfigurationSection item = items.getConfigurationSection(key);
            if (item == null) {
                continue;
            }

            switch (key) {
                case "玩家" -> {
                    for (int i = start; i < end; i++) {
                        String target = playerList.get(i);

                        ItemStack itemStack = MenuUtil.getMenuItemStackBuilder(getPlayer(), item, s -> applyTpaDataString(s, getPlayer().getName(), target), key)
                                .persistentDataContainer("target", PersistentDataType.STRING, target)
                                .build();

                        menu.setItem(playerSlotList.get(i), itemStack);
                    }
                    continue;
                }
                case "上一页" -> {
                    if (page <= 1) {
                        continue;
                    }
                }
                case "下一页" -> {
                    if (playerList.size() <= maxEnd) {
                        continue;
                    }
                }
            }

            MenuUtil.setMenuItem(getPlayer(), menu, item, key);
        }

        return menu;
    }

    @Override
    public void open(InventoryOpenEvent event) {
        ActionUtil.runActionList(getPlayer(), getConfig().getStringList("openActions"));
    }

    @Override
    public void click(InventoryClickEvent event) {
        ItemStack itemStack = MenuUtil.getClickItem(event);
        if (itemStack == null) {
            return;
        }

        event.setCancelled(true);

        PersistentDataContainerView container = itemStack.getPersistentDataContainer();

        String key = container.get(new NamespacedKey(Main.instance, "key"), PersistentDataType.STRING);
        if (key == null) {
            return;
        }

        MenuUtil.runItemClickAction(getPlayer(), getConfig(), key);

        switch (key) {
            case "玩家" -> {
                String target = container.get(new NamespacedKey(Main.instance, "target"), PersistentDataType.STRING);
                if (target == null) {
                    return;
                }

                switch (getRequestType()) {
                    case TPAHERE -> TpaHereUtil.sendTpaHereRequest(getPlayer(), target);
                    case TPA -> TpaUtil.sendTpaRequest(getPlayer(), target);
                }
            }
            case "上一页" -> new TeleportRequestMenu(getPlayer(), getRequestType(), getPage() - 1).openMenu();
            case "下一页" -> new TeleportRequestMenu(getPlayer(), getRequestType(), getPage() + 1).openMenu();
        }
    }

    @Override
    public void close(InventoryCloseEvent event) {
        ActionUtil.runActionList(getPlayer(), getConfig().getStringList("closeActions"));
    }

    /**
     * 处理家数据实例文本
     *
     * @param message 文本
     * @param player  玩家ID
     * @param target  目标玩家ID
     * @return 处理后的文本
     */
    private String applyTpaDataString(String message, String player, String target) {
        if (message == null) {
            return null;
        }

        return message
                .replace("{player}", player)
                .replace("{target}", target);
    }
}
