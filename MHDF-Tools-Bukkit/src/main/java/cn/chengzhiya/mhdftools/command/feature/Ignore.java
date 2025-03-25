package cn.chengzhiya.mhdftools.command.feature;

import cn.chengzhiya.mhdftools.command.AbstractCommand;
import cn.chengzhiya.mhdftools.entity.data.ChatIgnoreData;
import cn.chengzhiya.mhdftools.util.action.ActionUtil;
import cn.chengzhiya.mhdftools.util.config.ConfigUtil;
import cn.chengzhiya.mhdftools.util.config.LangUtil;
import cn.chengzhiya.mhdftools.util.database.ChatIgnoreDataUtil;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public final class Ignore extends AbstractCommand {
    public Ignore() {
        super(
                List.of("chatSettings.enable", "chatSettings.ignore.enable"),
                "聊天屏蔽",
                "mhdftools.commands.ignore",
                true,
                ConfigUtil.getConfig().getStringList("chatSettings.ignore.commands").toArray(new String[0])
        );
    }

    @Override
    public void execute(@NotNull Player sender, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            // 屏蔽玩家列表
            if (args[0].equalsIgnoreCase("list")) {
                StringBuilder listStringBuilder = new StringBuilder();
                List<ChatIgnoreData> chatIgnoreDataList = ChatIgnoreDataUtil.getChatIgnoreDataList(sender);
                for (int i = 0; i < chatIgnoreDataList.size(); i++) {
                    ChatIgnoreData chatIgnoreData = chatIgnoreDataList.get(i);
                    OfflinePlayer ignorePlayer = Bukkit.getOfflinePlayer(chatIgnoreData.getIgnore());

                    listStringBuilder.append(ignorePlayer.getName());
                    if (i != chatIgnoreDataList.size() - 1) {
                        listStringBuilder.append(", ");
                    }
                }

                ActionUtil.sendMessage(sender, LangUtil.i18n("commands.ignore.subCommands.list.message")
                        .replace("{list}", listStringBuilder.toString())
                );
                return;
            }
        }

        if (args.length == 2) {
            // 增加屏蔽玩家
            if (args[0].equals("add")) {
                if (ConfigUtil.getConfig().getStringList("chatSettings.ignore.blacklist").contains(args[1])) {
                    ActionUtil.sendMessage(sender, LangUtil.i18n("commands.ignore.subCommands.add.blacklist"));
                    return;
                }

                OfflinePlayer ignorePlayer = Bukkit.getOfflinePlayer(args[1]);
                if (ChatIgnoreDataUtil.isChatIgnore(sender, ignorePlayer)) {
                    ActionUtil.sendMessage(sender, LangUtil.i18n("commands.ignore.subCommands.add.haveIgnore"));
                    return;
                }

                ChatIgnoreData chatIgnoreData = new ChatIgnoreData();
                chatIgnoreData.setPlayer(sender.getUniqueId());
                chatIgnoreData.setIgnore(ignorePlayer.getUniqueId());
                ChatIgnoreDataUtil.updateChatIgnoreData(chatIgnoreData);

                ActionUtil.sendMessage(sender, LangUtil.i18n("commands.ignore.subCommands.add.message")
                        .replace("{target}", ignorePlayer.getName())
                );
            }

            // 移除屏蔽玩家
            if (args[0].equals("remove")) {
                OfflinePlayer ignorePlayer = Bukkit.getOfflinePlayer(args[1]);
                if (!ChatIgnoreDataUtil.isChatIgnore(sender, ignorePlayer)) {
                    ActionUtil.sendMessage(sender, LangUtil.i18n("commands.ignore.subCommands.remove.noIgnore"));
                    return;
                }

                ChatIgnoreData chatIgnoreData = ChatIgnoreDataUtil.getChatIgnoreData(sender, ignorePlayer);
                ChatIgnoreDataUtil.removeChatIgnoreData(chatIgnoreData);

                ActionUtil.sendMessage(sender, LangUtil.i18n("commands.ignore.subCommands.remove.message")
                        .replace("{target}", ignorePlayer.getName())
                );
            }
        }

        // 输出帮助信息
        {
            ActionUtil.sendMessage(sender, LangUtil.i18n("commands.ignore.subCommands.help.message")
                    .replace("{helpList}", LangUtil.getHelpList("ignore"))
                    .replace("{command}", label)
            );
        }
    }

    @Override
    public List<String> tabCompleter(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            return new ArrayList<>(LangUtil.getKeys("commands.ignore.subCommands"));
        }
        return new ArrayList<>();
    }
}
