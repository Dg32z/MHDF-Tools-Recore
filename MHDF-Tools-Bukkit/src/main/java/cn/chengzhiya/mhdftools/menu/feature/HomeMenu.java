package cn.chengzhiya.mhdftools.menu.feature;

import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.entity.data.HomeData;
import cn.chengzhiya.mhdftools.menu.AbstractMenu;
import cn.chengzhiya.mhdftools.util.action.ActionUtil;
import cn.chengzhiya.mhdftools.util.config.MenuConfigUtil;
import cn.chengzhiya.mhdftools.util.database.HomeDataUtil;
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
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

@Getter
public final class HomeMenu extends AbstractMenu {
    private final YamlConfiguration config;
    private final int page;

    public HomeMenu(Player player, int page) {
        super(
                "homeSettings.enable",
                player
        );
        this.config = MenuConfigUtil.getMenuConfig("home.yml");
        this.page = page;
    }

    @Override
    public @NotNull Inventory getInventory() {
        ActionUtil.runActionList(getPlayer(), getConfig().getStringList("openActions"));

        int size = getConfig().getInt("size");
        int homeSize = getConfig().getInt("homeSize");
        String title = getConfig().getString("title");

        Inventory menu = Bukkit.createInventory(this, size, ColorUtil.color(Objects.requireNonNull(title)));

        ConfigurationSection items = getConfig().getConfigurationSection("items");
        if (items == null) {
            return menu;
        }

        for (String key : items.getKeys(false)) {
            ConfigurationSection item = items.getConfigurationSection(key);

            if (item == null) {
                continue;
            }

            List<HomeData> homeList = HomeDataUtil.getHomeDataList(getPlayer());
            int start = (page - 1) * homeSize;
            int maxEnd = page * homeSize;
            int end = Math.min(homeList.size(), maxEnd);

            String type = item.getString("type");
            String name = item.getString("name");
            List<String> lore = item.getStringList("lore");
            Integer amount = item.getInt("amount");
            Integer customModelData = item.getInt("customModelData");

            switch (key) {
                case "家" -> {
                    for (int i = start; i < end; i++) {
                        HomeData homeData = homeList.get(i);

                        ItemStack itemStack = ItemStackUtil.getItemStack(
                                getPlayer(),
                                type,
                                name != null ? name.replace("{name}", homeData.getHome()) : null,
                                lore.stream()
                                        .map(s -> s
                                                .replace("{server}", homeData.getServer())
                                                .replace("{world}", homeData.getWorld())
                                                .replace("{x}", String.valueOf(homeData.getX()))
                                                .replace("{y}", String.valueOf(homeData.getY()))
                                                .replace("{z}", String.valueOf(homeData.getZ()))
                                                .replace("{yaw}", String.valueOf(homeData.getYaw()))
                                                .replace("{pitch}", String.valueOf(homeData.getPitch()))
                                        )
                                        .toList(),
                                amount,
                                customModelData
                        );

                        NBTItem nbtItem = new NBTItem(itemStack);
                        NBTCompound nbtCompound = nbtItem.getOrCreateCompound("MHDF-Tools");
                        nbtCompound.setString("key", key);
                        nbtCompound.setString("home", homeData.getHome());

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
                    if (homeList.size() <= maxEnd) {
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
            case "家" -> {
                String home = nbtCompound.getString("home");
                HomeData homeData = HomeDataUtil.getHomeData(getPlayer(), home);

                Main.instance.getBungeeCordManager().teleportLocation(getPlayer().getName(), homeData.toBungeeCordLocation());
            }
            case "上一页" -> new HomeMenu(getPlayer(), getPage() - 1).openMenu();
            case "下一页" -> new HomeMenu(getPlayer(), getPage() + 1).openMenu();
        }

        MenuUtil.runItemClickAction(getPlayer(), getConfig(), key);
    }

    @Override
    public void close(InventoryCloseEvent event) {
        ActionUtil.runActionList(getPlayer(), getConfig().getStringList("closeActions"));
    }
}
