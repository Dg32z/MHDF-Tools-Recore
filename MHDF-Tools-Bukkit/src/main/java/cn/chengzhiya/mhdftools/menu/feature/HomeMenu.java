package cn.chengzhiya.mhdftools.menu.feature;

import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.builder.ItemStackBuilder;
import cn.chengzhiya.mhdftools.entity.database.HomeData;
import cn.chengzhiya.mhdftools.menu.AbstractMenu;
import cn.chengzhiya.mhdftools.util.action.ActionUtil;
import cn.chengzhiya.mhdftools.util.config.LangUtil;
import cn.chengzhiya.mhdftools.util.config.MenuConfigUtil;
import cn.chengzhiya.mhdftools.util.database.HomeDataUtil;
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
import org.bukkit.persistence.PersistentDataType;
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
        this.config = MenuConfigUtil.getMenuConfig("home");
        this.page = page;
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

        List<HomeData> homeList = HomeDataUtil.getHomeDataList(getPlayer());
        List<Integer> homeSlotList = MenuUtil.getSlotList(items.getConfigurationSection("家"));

        int start = (page - 1) * homeSlotList.size();
        int maxEnd = page * homeSlotList.size();
        int end = Math.min(homeList.size(), maxEnd);

        for (String key : items.getKeys(false)) {
            ConfigurationSection item = items.getConfigurationSection(key);
            if (item == null) {
                continue;
            }

            switch (key) {
                case "家" -> {
                    String type = item.getString("type");
                    String name = item.getString("name");
                    List<String> lore = item.getStringList("lore");
                    Integer amount = item.getInt("amount");
                    Integer customModelData = item.getInt("customModelData");

                    for (int i = start; i < end; i++) {
                        HomeData homeData = homeList.get(i);

                        ItemStack itemStack = new ItemStackBuilder(getPlayer(), applyHomeDataString(type, homeData))
                                .name(applyHomeDataString(name, homeData))
                                .lore(lore.stream()
                                        .map(s -> applyHomeDataString(s, homeData))
                                        .toList())
                                .amount(amount)
                                .customModelData(customModelData)
                                .persistentDataContainer("key", PersistentDataType.STRING, key)
                                .persistentDataContainer("home", PersistentDataType.STRING, homeData.getHome())
                                .build();

                        menu.setItem(homeSlotList.get(i), itemStack);
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

        switch (key) {
            case "家" -> {
                String home = container.get(new NamespacedKey(Main.instance, "home"), PersistentDataType.STRING);
                HomeData homeData = HomeDataUtil.getHomeData(getPlayer(), home);

                Main.instance.getBungeeCordManager().teleportLocation(getPlayer(), homeData.toBungeeCordLocation());
                Main.instance.getBungeeCordManager().sendMessage(getPlayer(), LangUtil.i18n("commands.home.teleportMessage")
                        .replace("{home}", homeData.getHome())
                );
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

    /**
     * 处理家数据实例文本
     *
     * @param message  文本
     * @param homeData 家数据实例
     * @return 处理后的文本
     */
    private String applyHomeDataString(String message, HomeData homeData) {
        if (message == null) {
            return null;
        }

        return message
                .replace("{name}", homeData.getHome())
                .replace("{server}", homeData.getServer())
                .replace("{world}", homeData.getWorld())
                .replace("{x}", String.valueOf(homeData.getX()))
                .replace("{y}", String.valueOf(homeData.getY()))
                .replace("{z}", String.valueOf(homeData.getZ()))
                .replace("{yaw}", String.valueOf(homeData.getYaw()))
                .replace("{pitch}", String.valueOf(homeData.getPitch()));
    }
}
