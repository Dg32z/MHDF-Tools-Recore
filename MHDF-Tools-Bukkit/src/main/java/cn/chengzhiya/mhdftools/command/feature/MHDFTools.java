package cn.chengzhiya.mhdftools.command.feature;

import cn.chengzhiya.mhdftools.command.AbstractCommand;
import cn.chengzhiya.mhdftools.util.action.ActionUtil;
import cn.chengzhiya.mhdftools.util.config.ConfigUtil;
import cn.chengzhiya.mhdftools.util.config.LangUtil;
import cn.chengzhiya.mhdftools.util.config.SoundUtil;
import cn.chengzhiya.mhdftools.util.imports.HuskHomesUtil;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public final class MHDFTools extends AbstractCommand {
    public MHDFTools() {
        super(
                "梦之工具主命令",
                "mhdftools.commands.mhdftools",
                false,
                "mhdftools", "mt"
        );
    }

    @Override
    public void execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        switch (args.length) {
            case 1 -> {
                switch (args[0]) {
                    // 重载插件配置
                    case "reload" -> {
                        ConfigUtil.reloadConfig();
                        LangUtil.reloadLang();
                        SoundUtil.reloadSound();

                        ActionUtil.sendMessage(sender, LangUtil.i18n("commands.mhdftools.subCommands.reload.message")
                                .replace("{command}", label)
                        );
                        return;
                    }
                }
            }
            case 2 -> {
                if (args[0].equals("import")) {
                    switch (args[1]) {
                        case "huskhomes" -> HuskHomesUtil.importHuskHomesData(sender);
                        default ->
                                ActionUtil.sendMessage(sender, LangUtil.i18n("commands.mhdftools.subCommands.import.pluginNotSupport"));
                    }
                    return;
                }
            }
        }

        // 输出帮助信息
        {
            ActionUtil.sendMessage(sender, LangUtil.i18n("commands.mhdftools.subCommands.help.message")
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
        if (args.length == 2) {
            if (args[0].equals("import")) {
                return List.of("huskhomes");
            }
        }
        return new ArrayList<>();
    }
}
