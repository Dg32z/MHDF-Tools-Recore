package cn.chengzhiya.mhdftools.command.feature;

import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.command.AbstractCommand;
import cn.chengzhiya.mhdftools.util.config.ConfigUtil;
import cn.chengzhiya.mhdftools.util.config.LangUtil;
import cn.chengzhiya.mhdftools.util.config.SoundUtil;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public final class MHDFTools extends AbstractCommand {
    public MHDFTools() {
        super(
                null,
                "梦之工具主命令",
                "mhdftools.commands.mhdftools",
                false,
                "mhdftools", "mt"
        );
    }

    @Override
    public void execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            // 查看插件信息
            if (args[0].equalsIgnoreCase("about")) {
                sender.sendMessage(LangUtil.i18n("commands.mhdftools.subCommands.about.message")
                        .replace("{version}", Main.instance.getDescription().getVersion())
                        .replace("{serverVersion}", Main.instance.getPluginHookManager().getPacketEventsHook().getServerVersion().getReleaseName())
                );
                return;
            }

            // 重载插件配置
            if (args[0].equalsIgnoreCase("reload")) {
                ConfigUtil.reloadConfig();
                LangUtil.reloadLang();
                SoundUtil.reloadSound();
                sender.sendMessage(LangUtil.i18n("commands.mhdftools.subCommands.reload.message")
                        .replace("{command}", label)
                );
                return;
            }
        }

        // 输出帮助信息
        {
            sender.sendMessage(LangUtil.i18n("commands.mhdftools.subCommands.help.message")
                    .replace("{helpList}", LangUtil.getHelpList("mhdftools"))
                    .replace("{command}", label)
            );
        }
    }

    @Override
    public List<String> tabCompleter(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            return new ArrayList<>(LangUtil.getKeys("commands.mhdftools.subCommands"));
        }
        return new ArrayList<>();
    }
}
