package cn.chengzhiya.mhdftools.menu.feature;

import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.menu.AbstractMenu;
import cn.chengzhiya.mhdftools.util.action.ActionUtil;
import cn.chengzhiya.mhdftools.util.config.CustomMenuConfigUtil;
import cn.chengzhiya.mhdftools.util.menu.MenuUtil;
import cn.chengzhiya.mhdftools.util.message.ColorUtil;
import io.papermc.paper.persistence.PersistentDataContainerView;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

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
        int size = getConfig().getInt("size");
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

            ItemStack itemStack = MenuUtil.getItemStack(
                    getPlayer(),
                    item
            );

            ItemMeta itemMeta = itemStack.getItemMeta();

            PersistentDataContainer container = itemMeta.getPersistentDataContainer();
            container.set(new NamespacedKey(Main.instance, "key"), PersistentDataType.STRING, key);

            itemStack.setItemMeta(itemMeta);
            List<Integer> slotList = MenuUtil.getSlotList(item);
            for (Integer slot : slotList) {
                menu.setItem(slot, itemStack);
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

        PersistentDataContainerView container = itemStack.getPersistentDataContainer();

        String key = container.get(new NamespacedKey(Main.instance, "key"), PersistentDataType.STRING);
        if (key == null) {
            return;
        }

        MenuUtil.runItemClickAction(getPlayer(), getConfig(), key);
    }

    @Override
    public void close(InventoryCloseEvent event) {
        ActionUtil.runActionList(getPlayer(), getConfig().getStringList("closeActions"));
    }
}
