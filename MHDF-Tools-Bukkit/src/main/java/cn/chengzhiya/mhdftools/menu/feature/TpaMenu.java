package cn.chengzhiya.mhdftools.menu.feature;

import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.menu.AbstractMenu;
import cn.chengzhiya.mhdftools.util.action.ActionUtil;
import cn.chengzhiya.mhdftools.util.config.MenuConfigUtil;
import cn.chengzhiya.mhdftools.util.feature.TpaUtil;
import cn.chengzhiya.mhdftools.util.menu.ItemStackUtil;
import cn.chengzhiya.mhdftools.util.menu.MenuUtil;
import cn.chengzhiya.mhdftools.util.message.ColorUtil;
import de.tr7zw.changeme.nbtapi.NBTCompound;
import de.tr7zw.changeme.nbtapi.NBTItem;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

@Getter
public final class TpaMenu extends AbstractMenu {
    private final YamlConfiguration config;
    private final int page;

    public TpaMenu(Player player, int page) {
        super(
                "tpaSettings.enable",
                player
        );
        this.config = MenuConfigUtil.getMenuConfig("tpa.yml");
        this.page = page;
    }

    @Override
    public @NotNull Inventory getInventory() {
        int size = getConfig().getInt("size");
        int playerSize = getConfig().getInt("playerSize");
        String title = getConfig().getString("title");

        Inventory menu = Bukkit.createInventory(this, size, ColorUtil.color(Objects.requireNonNull(title)));

        ConfigurationSection items = getConfig().getConfigurationSection("items");
        if (items == null) {
            return menu;
        }

        List<String> playerList = Main.instance.getBungeeCordManager().getPlayerList();
        int start = (page - 1) * playerSize;
        int maxEnd = page * playerSize;
        int end = Math.min(playerList.size(), maxEnd);

        for (String key : items.getKeys(false)) {
            ConfigurationSection item = items.getConfigurationSection(key);

            if (item == null) {
                continue;
            }

            String type = item.getString("type");
            String name = item.getString("name");
            List<String> lore = item.getStringList("lore");
            Integer amount = item.getInt("amount");
            Integer customModelData = item.getInt("customModelData");

            switch (key) {
                case "玩家" -> {
                    for (int i = start; i < end; i++) {
                        String target = playerList.get(i);

                        ItemStack itemStack = ItemStackUtil.getItemStack(
                                getPlayer(),
                                applyTpaDataString(type, getPlayer().getName(), target),
                                applyTpaDataString(name, getPlayer().getName(), target),
                                lore.stream()
                                        .map(s -> applyTpaDataString(s, getPlayer().getName(), target))
                                        .toList(),
                                amount,
                                customModelData
                        );

                        NBTItem nbtItem = new NBTItem(itemStack);
                        NBTCompound nbtCompound = nbtItem.getOrCreateCompound("MHDF-Tools");
                        nbtCompound.setString("key", key);
                        nbtCompound.setString("target", target);

                        menu.addItem(nbtItem.getItem());
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

            ItemStack itemStack = ItemStackUtil.getItemStack(
                    getPlayer(),
                    item
            );

            NBTItem nbtItem = new NBTItem(itemStack);
            NBTCompound nbtCompound = nbtItem.getOrCreateCompound("MHDF-Tools");
            nbtCompound.setString("key", key);

            List<Integer> slotList = MenuUtil.getSlotList(item);
            for (Integer slot : slotList) {
                menu.setItem(slot, nbtItem.getItem());
            }
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

        NBTItem nbtItem = new NBTItem(itemStack);
        NBTCompound nbtCompound = nbtItem.getCompound("MHDF-Tools");
        if (nbtCompound == null) {
            return;
        }

        String key = nbtCompound.getString("key");
        switch (key) {
            case "玩家" -> {
                String target = nbtCompound.getString("target");
                TpaUtil.sendTpaRequest(getPlayer(), target);
            }
            case "上一页" -> new TpaMenu(getPlayer(), getPage() - 1).openMenu();
            case "下一页" -> new TpaMenu(getPlayer(), getPage() + 1).openMenu();
        }

        MenuUtil.runItemClickAction(getPlayer(), getConfig(), key);
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
