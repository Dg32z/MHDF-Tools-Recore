package cn.chengzhiya.mhdftools.command.feature;

import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.command.AbstractCommand;
import cn.chengzhiya.mhdftools.util.action.ActionUtil;
import cn.chengzhiya.mhdftools.util.config.ConfigUtil;
import cn.chengzhiya.mhdftools.util.config.LangUtil;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class LookItem extends AbstractCommand {
    public LookItem() {
        super(
                List.of("chatSettings.enable", "chatSettings.showItem.enable"),
                "展示物品",
                "mhdftools.commands.lookItem",
                true,
                ConfigUtil.getConfig().getStringList("chatSettings.showItem.commands").toArray(new String[0])
        );
    }

    @Override
    public void execute(@NotNull Player sender, @NotNull String label, @NotNull String[] args) {
        if (args.length != 1) {
            ActionUtil.sendMessage(sender, LangUtil.i18n("usageError")
                    .replace("{usage}", LangUtil.i18n("commands.lookItem.usage"))
                    .replace("{command}", label)
            );
            return;
        }

        String dataString = Main.instance.getCacheManager().get("showItem", args[0]);
        byte[] data = dataString.getBytes();

        ItemStack itemStack = ItemStack.deserializeBytes(data);
    }
}
