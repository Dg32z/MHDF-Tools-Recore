package cn.ChengZhiYa.MHDFTools.menu.fastuse;

import cn.ChengZhiYa.MHDFTools.entity.data.HomeData;
import cn.ChengZhiYa.MHDFTools.menu.AbstractMenu;
import cn.ChengZhiYa.MHDFTools.util.ActionUtil;
import cn.ChengZhiYa.MHDFTools.util.BungeeCordUtil;
import cn.ChengZhiYa.MHDFTools.util.RequirementUtil;
import cn.ChengZhiYa.MHDFTools.util.config.ConfigUtil;
import cn.ChengZhiYa.MHDFTools.util.database.HomeDataUtil;
import cn.ChengZhiYa.MHDFTools.util.menu.ItemStackUtil;
import cn.ChengZhiYa.MHDFTools.util.menu.MenuUtil;
import cn.ChengZhiYa.MHDFTools.util.message.ColorUtil;
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

import java.io.File;
import java.util.ArrayList;
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
        this.config = YamlConfiguration.loadConfiguration(new File(ConfigUtil.getDataFolder(), "menu/home.yml"));
        this.page = page;
    }

    @Override
    public @NotNull Inventory getInventory() {
        ActionUtil.runActionList(getPlayer(), getConfig().getStringList("openActions"));

        int size = getConfig().getInt("size");
        int homeSize = getConfig().getInt("homeSize");
        String title = getConfig().getString("title");

        Inventory menu = Bukkit.createInventory(getPlayer(), size, ColorUtil.color(Objects.requireNonNull(title)));

        ConfigurationSection items = getConfig().getConfigurationSection("items");
        if (items == null) {
            return menu;
        }

        for (String key : items.getKeys(false)) {
            ConfigurationSection itemConfig = items.getConfigurationSection(key);

            if (itemConfig == null) {
                continue;
            }

            int start = (page - 1) * homeSize;
            int end = page * homeSize;
            List<HomeData> homeList = HomeDataUtil.getHomeDataList(getPlayer());

            String type = itemConfig.getString("type");
            String name = itemConfig.getString("name");
            List<String> lore = itemConfig.getStringList("lore");
            Integer amount = itemConfig.getInt("amount");
            Integer customModelData = itemConfig.getInt("customModelData");

            switch (key) {
                case "家" -> {
                    for (int i = start; i < end; i++) {
                        HomeData homeData = homeList.get(i);

                        ItemStack item = ItemStackUtil.getItemStack(
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

                        NBTItem nbtItem = new NBTItem(item);
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
                    if (homeList.size() <= end) {
                        continue;
                    }
                }
            }

            ItemStack item = ItemStackUtil.getItemStack(
                    getPlayer(),
                    type,
                    name,
                    lore,
                    amount,
                    customModelData
            );

            NBTItem nbtItem = new NBTItem(item);
            NBTCompound nbtCompound = nbtItem.getOrCreateCompound("MHDF-Tools");
            nbtCompound.setString("key", key);

            List<Integer> slotList = new ArrayList<>();
            if (!itemConfig.getStringList("slots").isEmpty()) {
                slotList.addAll(MenuUtil.getSlotList(itemConfig.getStringList("slots")));
            } else {
                slotList.addAll(MenuUtil.getSlotList(itemConfig.getString("slot")));
            }

            for (Integer slot : slotList) {
                menu.setItem(slot, nbtItem.getItem());
            }
        }

        return menu;
    }

    @Override
    public void click(InventoryClickEvent event) {
        ItemStack item = MenuUtil.getClickItem(event);
        if (item == null) {
            return;
        }

        NBTItem nbtItem = new NBTItem(item);
        NBTCompound nbtCompound = nbtItem.getCompound("MHDF-Tools");
        if (nbtCompound == null) {
            return;
        }

        String key = nbtCompound.getString("key");
        switch (key) {
            case "家" -> {
                String home = nbtCompound.getString("home");
                HomeData homeData = HomeDataUtil.getHomeData(getPlayer(), home);

                BungeeCordUtil.teleportLocation(getPlayer().getName(), homeData.toBungeeCordLocation());
            }
            case "上一页" -> {
                HomeMenu homeMenu = new HomeMenu(getPlayer(), getPage() - 1);
                homeMenu.openMenu();
            }
            case "下一页" -> {
                HomeMenu homeMenu = new HomeMenu(getPlayer(), getPage() + 1);
                homeMenu.openMenu();
            }
            default -> {
                ConfigurationSection items = getConfig().getConfigurationSection("items");
                if (items == null) {
                    return;
                }

                ConfigurationSection itemConfig = items.getConfigurationSection(key);
                if (itemConfig == null) {
                    return;
                }

                ConfigurationSection clickRequirementsConfig = getConfig().getConfigurationSection("clickRequirements");
                if (clickRequirementsConfig != null) {
                    List<String> denyAction = RequirementUtil.checkRequirements(getPlayer(), clickRequirementsConfig);
                    if (!denyAction.isEmpty()) {
                        ActionUtil.runActionList(getPlayer(), denyAction);
                        return;
                    }
                }

                List<String> clickAction = itemConfig.getStringList("clickAction");
                if (!clickAction.isEmpty()) {
                    ActionUtil.runActionList(getPlayer(), clickAction);
                }
            }
        }
    }

    @Override
    public void close(InventoryCloseEvent event) {
        ActionUtil.runActionList(getPlayer(), getConfig().getStringList("closeActions"));
    }
}
