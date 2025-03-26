package cn.chengzhiya.mhdftools.builder;

import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.util.message.ColorUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public final class ItemStackBuilder {
    private final String type;
    private String name;
    private List<String> lore;
    private Integer amount;
    private Integer customModelData;

    public ItemStackBuilder(String type) {
        this.type = type;
    }

    public ItemStackBuilder name(String name) {
        this.name = name;
        return this;
    }

    public ItemStackBuilder lore(List<String> lore) {
        this.lore = lore;
        return this;
    }

    public ItemStackBuilder amount(Integer amount) {
        this.amount = amount;
        return this;
    }

    public ItemStackBuilder customModelData(Integer customModelData) {
        this.customModelData = customModelData;
        return this;
    }

    /**
     * 获取随机床空间ID实例
     *
     * @return 空间ID实例
     */
    private Material getRandomBed() {
        List<Material> bedList = Arrays.asList(
                Material.BLACK_BED,
                Material.BLUE_BED,
                Material.BROWN_BED,
                Material.CYAN_BED,
                Material.GREEN_BED,
                Material.LIGHT_BLUE_BED,
                Material.LIGHT_GRAY_BED,
                Material.MAGENTA_BED,
                Material.ORANGE_BED,
                Material.LIME_BED,
                Material.PINK_BED,
                Material.PURPLE_BED,
                Material.RED_BED,
                Material.WHITE_BED,
                Material.YELLOW_BED
        );
        return bedList.get(new Random().nextInt(bedList.size()));
    }

    /**
     * 构建物品实例
     *
     * @param player 玩家实例
     * @return 物品实例
     */
    public ItemStack build(Player player) {
        if (type == null) {
            return new ItemStack(Material.AIR);
        }

        ItemStack item;
        if (type.startsWith("craftEngine-")) {
            item = Main.instance.getPluginHookManager().getCraftEngineHook().getItem(
                    type.replace("craftEngine-", ""),
                    player
            );
        } else if (type.startsWith("mythicMobs-")) {
            item = Main.instance.getPluginHookManager().getMythicMobsHook().getItem(
                    type.replace("mythicMobs-", "")
            );
        } else if (type.startsWith("head-")) {
            item = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta meta = (SkullMeta) item.getItemMeta();

            String playerName = type.replace("head-", "");
            meta.setOwningPlayer(Bukkit.getOfflinePlayer(playerName));

            item.setItemMeta(meta);
        } else if (type.equals("random_bed")) {
            item = new ItemStack(getRandomBed());
        } else {
            Material material = Material.matchMaterial(type);
            if (material == null) {
                return new ItemStack(Material.AIR);
            }

            item = new ItemStack(material);
        }

        ItemMeta meta = item.getItemMeta();

        if (name != null) {
            meta.displayName(ColorUtil.color(Main.instance.getPluginHookManager().getPlaceholderAPIHook().placeholder(player, name)));
        }

        if (lore != null && !lore.isEmpty()) {
            meta.lore(lore.stream()
                    .map(s -> Main.instance.getPluginHookManager().getPlaceholderAPIHook().placeholder(player, s))
                    .map(ColorUtil::color)
                    .toList()
            );
        }

        if (amount != null && amount > 0) {
            item.setAmount(amount);
        }

        if (customModelData != null && customModelData > 0) {
            meta.setCustomModelData(customModelData);
        }

        item.setItemMeta(meta);
        return item;
    }
}
