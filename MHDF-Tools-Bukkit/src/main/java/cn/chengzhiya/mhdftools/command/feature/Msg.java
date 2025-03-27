package cn.chengzhiya.mhdftools.command.feature;

import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.command.AbstractCommand;
import cn.chengzhiya.mhdftools.util.action.ActionUtil;
import cn.chengzhiya.mhdftools.util.config.ConfigUtil;
import cn.chengzhiya.mhdftools.util.config.LangUtil;
import cn.chengzhiya.mhdftools.util.feature.ChatUtil;
import cn.chengzhiya.mhdftools.util.message.ColorUtil;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public final class Msg extends AbstractCommand {
    public Msg() {
        super(
                List.of("chatSettings.enable", "chatSettings.msg.enable"),
                "私聊",
                "mhdftools.commands.msg",
                false,
                ConfigUtil.getConfig().getStringList("chatSettings.msg.commands").toArray(new String[0])
        );
    }

    @Override
    public void execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        // 输出帮助信息
        if (args.length < 2) {
            ActionUtil.sendMessage(sender, LangUtil.i18n("usageError")
                    .replace("{usage}", LangUtil.i18n("commands.msg.usage"))
                    .replace("{command}", label)
            );
            return;
        }

        if (!Main.instance.getBungeeCordManager().ifPlayerOnline(args[0])) {
            ActionUtil.sendMessage(sender, LangUtil.i18n("playerOffline"));
            return;
        }

        StringBuilder messageBuilder = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            messageBuilder.append(args[i]);
            if (i < args.length - 1) {
                messageBuilder.append(" ");
            }
        }

        String message = messageBuilder.toString();

        // 聊天延迟
        if (ConfigUtil.getConfig().getBoolean("chatSettings.delay.enable")) {
            if (!sender.hasPermission("mhdftools.bypass.chat.delay")) {
                String delayData = Main.instance.getCacheManager().get(sender.getName() + "_delay");
                if (delayData != null) {
                    ActionUtil.sendMessage(sender, LangUtil.i18n("chat.delay")
                            .replace("{delay}", delayData)
                    );
                    return;
                }
            }
        }

        // 限制使用颜色符号
        if (!sender.hasPermission("mhdftools.bypass.chat.color")) {
            message = ChatColor.stripColor(ColorUtil.legacyColor(message));
        }

        // 限制使用miniMessage
        if (!sender.hasPermission("mhdftools.bypass.chat.minimessage")) {
            Pattern pattern = Pattern.compile("</?[a-zA-Z0-9_:-]+>");
            message = pattern.matcher(message).replaceAll("");
        }

        // 刷屏限制
        if (ConfigUtil.getConfig().getBoolean("chatSettings.spam.enable")) {
            if (!sender.hasPermission("mhdftools.bypass.chat.spam")) {
                String spamData = Main.instance.getCacheManager().get(sender.getName() + "_spam");
                if (spamData != null && spamData.equals(message)) {
                    ActionUtil.sendMessage(sender, LangUtil.i18n("chat.spam"));
                    return;
                }
            }
        }

        int delay = ConfigUtil.getConfig().getInt("chatSettings.delay.delay");
        Main.instance.getCacheManager().put(sender.getName() + "_delay", String.valueOf(delay));
        Main.instance.getCacheManager().put(sender.getName() + "_spam", message);

        // 替换词
        if (!sender.hasPermission("mhdftools.bypass.chat.replaceWord")) {
            message = ChatUtil.applyBlackWord(message);
        }

        ActionUtil.sendMessage(sender, LangUtil.i18n("commands.msg.send")
                .replace("{player}", sender.getName())
                .replace("{target}", args[0])
                .replace("{message}", message)
        );
        Main.instance.getBungeeCordManager().sendMessage(args[0], LangUtil.i18n("commands.msg.receive")
                .replace("{player}", sender.getName())
                .replace("{target}", args[0])
                .replace("{message}", message)
        );
    }

    @Override
    public List<String> tabCompleter(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            return Main.instance.getBungeeCordManager().getPlayerList();
        }
        return new ArrayList<>();
    }
}
