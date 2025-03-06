package cn.chengzhiya.mhdftools.menu.feature;

import cn.chengzhiya.mhdftools.menu.AbstractMenu;
import cn.chengzhiya.mhdftools.util.action.ActionUtil;
import cn.chengzhiya.mhdftools.util.action.RequirementUtil;
import cn.chengzhiya.mhdftools.util.config.CustomMenuConfigUtil;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
public final class CustomMenu extends AbstractMenu {
    private final YamlConfiguration config;
    private final String menu;

    public CustomMenu(Player player, String menu) {
        super(
                "customMenuSettings.enable",
                player
        );
        this.menu = menu;
        this.config = CustomMenuConfigUtil.getCustomMenuConfig(menu + ".yml");
    }

    @Override
    public @NotNull Inventory getInventory() {
        ActionUtil.runActionList(getPlayer(), getConfig().getStringList("openActions"));

        int size = getConfig().getInt("size");
        String title = getConfig().getString("title");

        Inventory menu = Bukkit.createInventory(this, size, ColorUtil.color(Objects.requireNonNull(title)));

        ConfigurationSection items = getConfig().getConfigurationSection("items");
        if (items == null) {
            return menu;
        }

        for (String key : items.getKeys(false)) {
            ConfigurationSection itemConfig = items.getConfigurationSection(key);

            if (itemConfig == null) {
                continue;
            }

            ItemStack item = ItemStackUtil.getItemStack(
                    getPlayer(),
                    itemConfig
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

        event.setCancelled(true);

        NBTItem nbtItem = new NBTItem(item);
        NBTCompound nbtCompound = nbtItem.getCompound("MHDF-Tools");
        if (nbtCompound == null) {
            return;
        }

        String key = nbtCompound.getString("key");

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

    @Override
    public void close(InventoryCloseEvent event) {
        ActionUtil.runActionList(getPlayer(), getConfig().getStringList("closeActions"));
    }
}
